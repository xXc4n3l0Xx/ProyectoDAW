package com.proyectodaw.backend.service;

import com.proyectodaw.backend.dto.ForumThreadResponseDTO;
import com.proyectodaw.backend.model.Estado;
import com.proyectodaw.backend.model.ForumThread;
import com.proyectodaw.backend.model.Usuario;
import com.proyectodaw.backend.repository.ForumThreadRepository;
import com.proyectodaw.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ForumThreadService {

    @Autowired
    private ForumThreadRepository forumThreadRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public ForumThread crearHilo(ForumThread hilo) {
        Usuario completo = usuarioRepository.findById(hilo.getUsuario().getId()).orElse(null);
        hilo.setUsuario(completo);
        return forumThreadRepository.save(hilo);
    }

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

    public void eliminarHilo(Integer idHilo, Integer idUsuario) {
        Optional<ForumThread> hiloOpt = forumThreadRepository.findById(idHilo);
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

        if (hiloOpt.isPresent() && usuarioOpt.isPresent()) {
            ForumThread hilo = hiloOpt.get();
            Usuario usuario = usuarioOpt.get();

            boolean esAdmin = "admin".equalsIgnoreCase(usuario.getRol().getNombre());
            boolean esCreador = hilo.getUsuario().getId().equals(idUsuario);

            if (esAdmin || esCreador) {
                hilo.setEstado(new Estado(2));
                forumThreadRepository.save(hilo);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para eliminar este hilo");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hilo o usuario no encontrado");
        }
    }

    public void activarHilo(Integer idHilo, Integer idUsuario) {
        Optional<ForumThread> hiloOpt = forumThreadRepository.findById(idHilo);
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

        if (hiloOpt.isPresent() && usuarioOpt.isPresent()) {
            ForumThread hilo = hiloOpt.get();
            Usuario usuario = usuarioOpt.get();

            boolean esAdmin = "admin".equalsIgnoreCase(usuario.getRol().getNombre());
            boolean esCreador = hilo.getUsuario().getId().equals(idUsuario);

            if (esAdmin || esCreador) {
                hilo.setEstado(new Estado(1));
                forumThreadRepository.save(hilo);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para reactivar este hilo");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hilo o usuario no encontrado");
        }
    }

    public ForumThread editarHilo(Integer idHilo, Integer idUsuario, String nuevoTitulo) {
        Optional<ForumThread> hiloOpt = forumThreadRepository.findById(idHilo);
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

        if (hiloOpt.isPresent() && usuarioOpt.isPresent()) {
            ForumThread hilo = hiloOpt.get();
            Usuario usuario = usuarioOpt.get();

            boolean esAdmin = "admin".equalsIgnoreCase(usuario.getRol().getNombre());
            boolean esCreador = hilo.getUsuario().getId().equals(idUsuario);

            if (esAdmin || esCreador) {
                hilo.setTitulo(nuevoTitulo);
                return forumThreadRepository.save(hilo);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes editar este hilo");
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hilo o usuario no encontrado");
    }

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
