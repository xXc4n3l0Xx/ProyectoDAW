package com.proyectodaw.backend.service;

// Modelos
import com.proyectodaw.backend.model.Estado;
import com.proyectodaw.backend.model.Post;
import com.proyectodaw.backend.model.Usuario;

// Repositorios
import com.proyectodaw.backend.repository.PostRepository;
import com.proyectodaw.backend.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service // Marca esta clase como un servicio manejado por Spring
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Guarda un nuevo post en la base de datos
    public Post insertarPost(Post post) {
        return postRepository.save(post);
    }

    // Obtiene los posts activos de un hilo, ordenados por fecha ascendente
    public List<Post> obtenerPostsPorThread(Integer idThread) {
        return postRepository.findByThread_IdAndEstado_IdOrderByFechaCreacionAsc(idThread, 1); // Estado 1 = Activo
    }

    // Marca un post como eliminado si lo hace el autor o un administrador
    public boolean eliminarPost(Integer idPost, Integer idUsuario) {
        Optional<Post> postOpt = postRepository.findById(idPost);
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

        if (postOpt.isPresent() && usuarioOpt.isPresent()) {
            Post post = postOpt.get();
            Usuario usuario = usuarioOpt.get();

            boolean esAdmin = "admin".equalsIgnoreCase(usuario.getRol().getNombre());
            boolean esAutor = post.getUsuario().getId().equals(idUsuario);

            if (esAdmin || esAutor) {
                post.setEstado(new Estado(2)); // Estado 2 = Eliminado
                postRepository.save(post);
                return true;
            }
        }
        return false;
    }

    // Reactiva un post eliminado si lo hace el autor o un administrador
    public boolean activarPost(Integer idPost, Integer idUsuario) {
        Optional<Post> postOpt = postRepository.findById(idPost);
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

        if (postOpt.isPresent() && usuarioOpt.isPresent()) {
            Post post = postOpt.get();
            Usuario usuario = usuarioOpt.get();

            boolean esAdmin = "admin".equalsIgnoreCase(usuario.getRol().getNombre());
            boolean esAutor = post.getUsuario().getId().equals(idUsuario);

            if (esAdmin || esAutor) {
                post.setEstado(new Estado(1)); // Estado 1 = Activo
                postRepository.save(post);
                return true;
            }
        }
        return false;
    }

    // Edita el contenido de un post si lo hace el autor o un administrador
    public Post editarPost(Integer idPost, Integer idUsuario, String nuevoContenido) {
        Optional<Post> postOpt = postRepository.findById(idPost);
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

        if (postOpt.isPresent() && usuarioOpt.isPresent()) {
            Post post = postOpt.get();
            Usuario usuario = usuarioOpt.get();

            boolean esAdmin = "admin".equalsIgnoreCase(usuario.getRol().getNombre());
            boolean esAutor = post.getUsuario().getId().equals(idUsuario);

            if (esAdmin || esAutor) {
                post.setContenido(nuevoContenido);
                return postRepository.save(post);
            }
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes editar este post");
    }

    // Obtiene todos los posts de un hilo por su estado (activo, eliminado, etc.)
    public List<Post> obtenerPostsPorThreadYEstado(Integer idThread, int estadoId) {
        return postRepository.findByThread_IdAndEstado_IdOrderByFechaCreacionAsc(idThread, estadoId);
    }
}
