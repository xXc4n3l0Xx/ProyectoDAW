package com.proyectodaw.backend.service;

// DTO para devolver información resumida de hilos
import com.proyectodaw.backend.dto.ForumThreadResponseDTO;
// Modelo de estado (activo, inactivo, eliminado)
import com.proyectodaw.backend.model.Estado;
// Modelo de hilo de foro
import com.proyectodaw.backend.model.ForumThread;
// Modelo de usuario creador o editor del hilo
import com.proyectodaw.backend.model.Usuario;
// Repositorio de hilos para acceso a la base de datos
import com.proyectodaw.backend.repository.ForumThreadRepository;
// Repositorio de usuarios
import com.proyectodaw.backend.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service // Marca esta clase como un servicio de negocio para Spring
public class ForumThreadService {

    @Autowired
    private ForumThreadRepository forumThreadRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Crea un nuevo hilo de foro y lo guarda con el usuario completo
    public ForumThread crearHilo(ForumThread hilo) {
        Integer idUsuario = hilo.getUsuario().getId();

        Usuario completo = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario no encontrado"));

        hilo.setUsuario(completo); // Asigna el objeto completo
        return forumThreadRepository.save(hilo);
    }

    // Obtiene todos los hilos activos y los convierte a DTOs
    public List<ForumThreadResponseDTO> obtenerHilosActivos() {
        List<ForumThread> hilos = forumThreadRepository.findAllActivos();

        return hilos.stream().map(hilo -> {
            ForumThreadResponseDTO dto = new ForumThreadResponseDTO();
            dto.setId(hilo.getId());
            dto.setTitulo(hilo.getTitulo());
            dto.setCreadoEn(hilo.getCreadoEn().toString());

            Usuario usuario = hilo.getUsuario();
            if (usuario != null) {
                dto.setNombreUsuario(usuario.getNombre());
                dto.setAvatarUsuario(usuario.getAvatar());
            }

            return dto;
        }).toList();
    }

    // Cambia el estado de un hilo a "eliminado" si lo elimina el creador o un admin
    public void eliminarHilo(Integer idHilo, Integer idUsuario) {
        Optional<ForumThread> hiloOpt = forumThreadRepository.findById(idHilo);
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

        if (hiloOpt.isPresent() && usuarioOpt.isPresent()) {
            ForumThread hilo = hiloOpt.get();
            Usuario usuario = usuarioOpt.get();

            boolean esAdmin = "admin".equalsIgnoreCase(usuario.getRol().getNombre());
            boolean esCreador = hilo.getUsuario().getId().equals(idUsuario);

            if (esAdmin || esCreador) {
                hilo.setEstado(new Estado(2)); // Estado 2 = eliminado
                forumThreadRepository.save(hilo);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para eliminar este hilo");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hilo o usuario no encontrado");
        }
    }

    // Cambia el estado de un hilo a "activo" si lo reactiva el creador o un admin
    public void activarHilo(Integer idHilo, Integer idUsuario) {
        Optional<ForumThread> hiloOpt = forumThreadRepository.findById(idHilo);
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

        if (hiloOpt.isPresent() && usuarioOpt.isPresent()) {
            ForumThread hilo = hiloOpt.get();
            Usuario usuario = usuarioOpt.get();

            boolean esAdmin = "admin".equalsIgnoreCase(usuario.getRol().getNombre());
            boolean esCreador = hilo.getUsuario().getId().equals(idUsuario);

            if (esAdmin || esCreador) {
                hilo.setEstado(new Estado(1)); // Estado 1 = activo
                forumThreadRepository.save(hilo);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para reactivar este hilo");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hilo o usuario no encontrado");
        }
    }

    // Edita el título de un hilo si lo hace el creador o un admin
    public ForumThread editarHilo(Integer idHilo, Integer idUsuario, String nuevoTitulo) {
        Optional<ForumThread> hiloOpt = forumThreadRepository.findById(idHilo);
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

        if (hiloOpt.isPresent() && usuarioOpt.isPresent()) {
            ForumThread hilo = hiloOpt.get();
            Usuario usuario = usuarioOpt.get();

            boolean esAdmin = "admin".equalsIgnoreCase(usuario.getRol().getNombre());
            boolean esCreador = hilo.getUsuario().getId().equals(idUsuario);

            if (esAdmin || esCreador) {
                hilo.setTitulo(nuevoTitulo); // Asigna nuevo título
                return forumThreadRepository.save(hilo);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes editar este hilo");
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hilo o usuario no encontrado");
    }

    // Obtiene hilos por estado (activo, eliminado, etc.) y los convierte a DTOs
    public List<ForumThreadResponseDTO> obtenerHilosPorEstado(int estadoId) {
        List<ForumThread> hilos = forumThreadRepository.findByEstado_IdOrderByCreadoEnDesc(estadoId);

        return hilos.stream().map(hilo -> {
            ForumThreadResponseDTO dto = new ForumThreadResponseDTO();
            dto.setId(hilo.getId());
            dto.setTitulo(hilo.getTitulo());
            dto.setCreadoEn(hilo.getCreadoEn().toString());
            dto.setNombreUsuario(hilo.getUsuario().getNombre());
            dto.setAvatarUsuario(hilo.getUsuario().getAvatar());
            return dto;
        }).toList();
    }
}
