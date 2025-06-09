package com.proyectodaw.backend.controller;

// DTO con respuesta tras login: token + datos del usuario
import com.proyectodaw.backend.dto.AuthResponseDTO;
// DTO para recibir las credenciales al iniciar sesión
import com.proyectodaw.backend.dto.LoginRequest;
// DTO para enviar al frontend los datos de un usuario
import com.proyectodaw.backend.dto.UsuarioResponseDTO;
// DTO para mostrar top 5 usuarios por puntuación
import com.proyectodaw.backend.dto.UsuarioTopDTO;
// Modelo que representa el estado del usuario (activo, inactivo)
import com.proyectodaw.backend.model.Estado;
// Modelo de rol del usuario (admin, usuario)
import com.proyectodaw.backend.model.Rol;
// Modelo de usuario del sistema
import com.proyectodaw.backend.model.Usuario;
// Repositorio para acceder y manipular usuarios en la base
import com.proyectodaw.backend.repository.UsuarioRepository;
// Utilidad para trabajar con el token JWT (extraer correo, etc.)
import com.proyectodaw.backend.security.JwtUtil;
// Servicio con la lógica del negocio relacionada al usuario
import com.proyectodaw.backend.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired; // Inyección automática de dependencias
import org.springframework.http.HttpStatus; // Codificación de respuestas HTTP (200, 404, etc.)
import org.springframework.http.ResponseEntity; // Tipo de respuesta HTTP con cuerpo y estado
import org.springframework.web.bind.annotation.*; // Anotaciones REST
import org.springframework.web.server.ResponseStatusException; // Excepciones con códigos HTTP
import java.util.Map; // Para recibir datos como pares clave-valor
import java.sql.Timestamp; // Para guardar fecha de registro del usuario
import java.util.Optional; // Para manejar posibles nulos
import java.util.List; // Para listas de usuarios

@RestController
@RequestMapping("/api/usuarios") // Ruta base para todo lo relacionado con usuarios
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService; // Servicio con lógica de registro, login, etc.

    @Autowired
    private JwtUtil jwtUtil; // Utilidad para leer datos del token JWT

    @Autowired
    private UsuarioRepository usuarioRepository; // Acceso directo a la base de usuarios

    // Endpoint POST para registrar un nuevo usuario
    @PostMapping("/registro")
    public UsuarioResponseDTO registrar(@RequestBody Usuario usuario) {
        // Validación: correo y nombre únicos
        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario con ese correo");
        }
        if (usuarioRepository.findByNombre(usuario.getNombre()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario con ese nombre");
        }

        // Setea campos por defecto al registrarse
        usuario.setFechaRegistro(new Timestamp(System.currentTimeMillis()));
        usuario.setPuntuacion(0);
        usuario.setEstado(new Estado(1, "Activo")); // Estado 1 = Activo
        usuario.setRol(new Rol(2, "usuario")); // Rol 2 = usuario normal

        Usuario registrado = usuarioService.registrarUsuario(usuario);
        return construirDTO(registrado);
    }

    // Endpoint POST para iniciar sesión
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest login) {
        Optional<AuthResponseDTO> response = usuarioService.login(login.getCorreo(), login.getContrasena());
        return response
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas"));
    }

    // Endpoint PUT para actualizar datos del usuario autenticado
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Integer id,
            @RequestBody Usuario datos,
            @RequestHeader("Authorization") String token) {

        // Extrae correo desde el token y busca el usuario autenticado
        String correoDesdeToken = jwtUtil.getCorreoDesdeToken(token.replace("Bearer ", ""));
        Usuario usuarioAutenticado = usuarioRepository.findByCorreoAndEstado_Id(correoDesdeToken, 1)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no válido"));

        // Solo el propio usuario puede modificar su información
        if (!usuarioAutenticado.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo puedes editar tu propio perfil");
        }

        // Validación: correo o nombre no se repitan en otros usuarios
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

        // Actualiza los datos permitidos
        Usuario usuario = usuarioOpt.get();
        usuario.setNombre(datos.getNombre());
        usuario.setCorreo(datos.getCorreo());
        usuario.setContrasena(usuarioService.encriptarContrasena(datos.getContrasena()));
        usuario.setAvatar(datos.getAvatar());

        Usuario actualizado = usuarioService.actualizarUsuario(usuario);
        return ResponseEntity.ok(construirDTO(actualizado));
    }

    // Método auxiliar para construir un DTO a partir de un Usuario
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

    // Endpoint POST para guardar una nueva puntuación (score) si es mayor a la actual
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

        // Solo guarda si es una mejor puntuación
        if (nuevoScore > usuario.getPuntuacion()) {
            usuario.setPuntuacion(nuevoScore);
            usuarioRepository.save(usuario);
            return ResponseEntity.ok("Nueva mejor puntuación: " + nuevoScore);
        }

        return ResponseEntity.ok("Puntuación no actualizada (no es mayor a la actual)");
    }

    // Endpoint GET para obtener el top 5 usuarios con mejor puntuación
    @GetMapping("/top")
    public List<UsuarioTopDTO> obtenerTop5Usuarios() {
        List<Usuario> topUsuarios = usuarioRepository
                .findTop5ByEstado_IdOrderByPuntuacionDescIdAsc(1); // Solo usuarios activos

        return topUsuarios.stream().map(u -> {
            UsuarioTopDTO dto = new UsuarioTopDTO();
            dto.setNombre(u.getNombre());
            dto.setAvatar(u.getAvatar());
            dto.setPuntuacion(u.getPuntuacion());
            return dto;
        }).toList();
    }

    // Endpoint GET para obtener todos los usuarios (solo admins)
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

    // Endpoint PUT para cambiar el estado de un usuario (activo/inactivo) – solo admins
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
