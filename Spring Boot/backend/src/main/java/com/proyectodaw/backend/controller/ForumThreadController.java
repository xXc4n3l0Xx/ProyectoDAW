package com.proyectodaw.backend.controller;

import com.proyectodaw.backend.dto.ForumThreadResponseDTO;
import com.proyectodaw.backend.dto.HiloRequest;
import com.proyectodaw.backend.model.Estado;
import com.proyectodaw.backend.model.ForumThread;
import com.proyectodaw.backend.model.Usuario;
import com.proyectodaw.backend.repository.UsuarioRepository;
import com.proyectodaw.backend.security.JwtUtil;
import com.proyectodaw.backend.service.ForumThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.proyectodaw.backend.dto.ForumThreadUpdateDTO;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/hilos")
public class ForumThreadController {

    @Autowired
    private ForumThreadService threadService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ForumThreadResponseDTO crearHilo(@RequestBody HiloRequest hiloDTO) {
        ForumThread hilo = new ForumThread();
        hilo.setTitulo(hiloDTO.getTitulo());
        hilo.setUsuario(new Usuario(hiloDTO.getIdUsuario()));
        hilo.setEstado(new Estado(1, "Activo"));
        hilo.setCreadoEn(LocalDateTime.now());

        ForumThread hiloGuardado = threadService.crearHilo(hilo);

        ForumThreadResponseDTO dto = new ForumThreadResponseDTO();
        dto.setId(hiloGuardado.getId());
        dto.setTitulo(hiloGuardado.getTitulo());
        dto.setCreadoEn(hiloGuardado.getCreadoEn().toString());
        dto.setNombreUsuario(hiloGuardado.getUsuario().getNombre());
        dto.setAvatarUsuario(hiloGuardado.getUsuario().getAvatar());

        return dto;
    }

    @GetMapping
    public List<ForumThreadResponseDTO> obtenerHilos() {
        return threadService.obtenerHilosActivos();
    }

    @GetMapping("/eliminados")
    public List<ForumThreadResponseDTO> obtenerHilosEliminados(@RequestHeader("Authorization") String token) {
        String correo = jwtUtil.getCorreoDesdeToken(token.replace("Bearer ", ""));
        Usuario usuario = usuarioRepository.findByCorreoAndEstado_Id(correo, 1).orElseThrow();

        if (!"admin".equalsIgnoreCase(usuario.getRol().getNombre())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo administradores pueden ver esto");
        }

        return threadService.obtenerHilosPorEstado(2);
    }

    @DeleteMapping("/{idHilo}/usuario/{idUsuario}")
    public void eliminarHilo(@PathVariable Integer idHilo, @PathVariable Integer idUsuario) {
        threadService.eliminarHilo(idHilo, idUsuario);
    }

    @PutMapping("/{idHilo}/activar/usuario/{idUsuario}")
    public void activarHilo(@PathVariable Integer idHilo, @PathVariable Integer idUsuario) {
        threadService.activarHilo(idHilo, idUsuario);
    }

    @PutMapping("/{idHilo}/usuario/{idUsuario}")
    public ForumThreadResponseDTO editarHilo(@PathVariable Integer idHilo,
                                             @PathVariable Integer idUsuario,
                                             @RequestBody ForumThreadUpdateDTO dto) {
        ForumThread hilo = threadService.editarHilo(idHilo, idUsuario, dto.getTitulo());

        ForumThreadResponseDTO respuesta = new ForumThreadResponseDTO();
        respuesta.setId(hilo.getId());
        respuesta.setTitulo(hilo.getTitulo());
        respuesta.setNombreUsuario(hilo.getUsuario().getNombre());
        respuesta.setAvatarUsuario(hilo.getUsuario().getAvatar());
        respuesta.setCreadoEn(hilo.getCreadoEn().toString());

        return respuesta;
    }
}
