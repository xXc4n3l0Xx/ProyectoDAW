package com.proyectodaw.backend.controller;

import com.proyectodaw.backend.dto.AuthResponseDTO;
import com.proyectodaw.backend.dto.LoginRequest;
import com.proyectodaw.backend.dto.UsuarioResponseDTO;
import com.proyectodaw.backend.dto.UsuarioTopDTO;
import com.proyectodaw.backend.model.Estado;
import com.proyectodaw.backend.model.Rol;
import com.proyectodaw.backend.model.Usuario;
import com.proyectodaw.backend.repository.UsuarioRepository;
import com.proyectodaw.backend.security.JwtUtil;
import com.proyectodaw.backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/registro")
    public UsuarioResponseDTO registrar(@RequestBody Usuario usuario) {

        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario con ese correo");
        }

        if (usuarioRepository.findByNombre(usuario.getNombre()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario con ese nombre");
        }

        usuario.setFechaRegistro(new Timestamp(System.currentTimeMillis()));
        usuario.setPuntuacion(0);
        usuario.setEstado(new Estado(1, "Activo"));
        usuario.setRol(new Rol(2, "usuario"));

        Usuario registrado = usuarioService.registrarUsuario(usuario);
        return construirDTO(registrado);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest login) {
        Optional<AuthResponseDTO> response = usuarioService.login(login.getCorreo(), login.getContrasena());
        return response
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Integer id,
            @RequestBody Usuario datos,
            @RequestHeader("Authorization") String token) {

        String correoDesdeToken = jwtUtil.getCorreoDesdeToken(token.replace("Bearer ", ""));
        Usuario usuarioAutenticado = usuarioRepository.findByCorreoAndEstado_Id(correoDesdeToken, 1)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no válido"));

        if (!usuarioAutenticado.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo puedes editar tu propio perfil");
        }

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        Optional<Usuario> correoRepetido = usuarioRepository.findByCorreo(datos.getCorreo());
        if (correoRepetido.isPresent() && !correoRepetido.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario con ese correo");
        }

        Optional<Usuario> nombreRepetido = usuarioRepository.findByNombre(datos.getNombre());
        if (nombreRepetido.isPresent() && !nombreRepetido.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario con ese nombre");
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setNombre(datos.getNombre());
        usuario.setCorreo(datos.getCorreo());
        usuario.setContrasena(usuarioService.encriptarContrasena(datos.getContrasena()));
        usuario.setAvatar(datos.getAvatar());

        Usuario actualizado = usuarioService.actualizarUsuario(usuario);
        return ResponseEntity.ok(construirDTO(actualizado));
    }

    private UsuarioResponseDTO construirDTO(Usuario u) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(u.getId());
        dto.setNombre(u.getNombre());
        dto.setCorreo(u.getCorreo());
        dto.setAvatar(u.getAvatar());
        dto.setFechaRegistro(u.getFechaRegistro().toString());
        dto.setPuntuacion(u.getPuntuacion());
        dto.setEstado(u.getEstado().getDescripcion());
        dto.setRol(u.getRol().getNombre());
        return dto;
    }

    @PostMapping("/score")
    public ResponseEntity<?> guardarScore(@RequestBody Map<String, Integer> body,
                                          @RequestHeader("Authorization") String token) {
        String correo = jwtUtil.getCorreoDesdeToken(token.replace("Bearer ", ""));
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));

        Integer nuevoScore = body.get("score");
        if (nuevoScore == null || nuevoScore < 0) {
            return ResponseEntity.badRequest().body("Score inválido");
        }

        if (nuevoScore > usuario.getPuntuacion()) {
            usuario.setPuntuacion(nuevoScore);
            usuarioRepository.save(usuario);
            return ResponseEntity.ok("Nueva mejor puntuación: " + nuevoScore);
        }

        return ResponseEntity.ok("Puntuación no actualizada (no es mayor a la actual)");
    }

    @GetMapping("/top")
    public List<UsuarioTopDTO> obtenerTop5Usuarios() {
        List<Usuario> topUsuarios = usuarioRepository
                .findTop5ByEstado_IdOrderByPuntuacionDescIdAsc(1);

        return topUsuarios.stream().map(u -> {
            UsuarioTopDTO dto = new UsuarioTopDTO();
            dto.setNombre(u.getNombre());
            dto.setAvatar(u.getAvatar());
            dto.setPuntuacion(u.getPuntuacion());
            return dto;
        }).toList();
    }

    @GetMapping("/todos")
    public List<UsuarioResponseDTO> obtenerTodos(@RequestHeader("Authorization") String token) {
        String correo = jwtUtil.getCorreoDesdeToken(token.replace("Bearer ", ""));
        Usuario admin = usuarioRepository.findByCorreoAndEstado_Id(correo, 1)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autorizado"));

        if (!"admin".equalsIgnoreCase(admin.getRol().getNombre())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo administradores pueden ver esta lista");
        }

        return usuarioRepository.findAll().stream()
                .map(this::construirDTO)
                .toList();
    }

    @PutMapping("/{id}/estado/{nuevoEstado}")
    public ResponseEntity<?> cambiarEstadoUsuario(@PathVariable Integer id, @PathVariable Integer nuevoEstado,
                                                  @RequestHeader("Authorization") String token) {
        String correo = jwtUtil.getCorreoDesdeToken(token.replace("Bearer ", ""));
        Usuario admin = usuarioRepository.findByCorreoAndEstado_Id(correo, 1)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autorizado"));

        if (!"admin".equalsIgnoreCase(admin.getRol().getNombre())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo administradores pueden cambiar el estado");
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setEstado(new Estado(nuevoEstado));
            usuarioRepository.save(usuario);
            return ResponseEntity.ok("Estado actualizado");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
    }

}