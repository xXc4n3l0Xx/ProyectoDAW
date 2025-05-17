package com.proyectodaw.backend.service;

import com.proyectodaw.backend.model.Estado;
import com.proyectodaw.backend.model.Post;
import com.proyectodaw.backend.model.Usuario;
import com.proyectodaw.backend.repository.PostRepository;
import com.proyectodaw.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Post insertarPost(Post post) {
        return postRepository.save(post);
    }

    public List<Post> obtenerPostsPorThread(Integer idThread) {
        return postRepository.findByThread_IdAndEstado_IdOrderByFechaCreacionAsc(idThread, 1);
    }

    public boolean eliminarPost(Integer idPost, Integer idUsuario) {
        Optional<Post> postOpt = postRepository.findById(idPost);
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

        if (postOpt.isPresent() && usuarioOpt.isPresent()) {
            Post post = postOpt.get();
            Usuario usuario = usuarioOpt.get();

            boolean esAdmin = "admin".equalsIgnoreCase(usuario.getRol().getNombre());
            boolean esAutor = post.getUsuario().getId().equals(idUsuario);

            if (esAdmin || esAutor) {
                post.setEstado(new Estado(2));
                postRepository.save(post);
                return true;
            }
        }
        return false;
    }

    public boolean activarPost(Integer idPost, Integer idUsuario) {
        Optional<Post> postOpt = postRepository.findById(idPost);
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

        if (postOpt.isPresent() && usuarioOpt.isPresent()) {
            Post post = postOpt.get();
            Usuario usuario = usuarioOpt.get();

            boolean esAdmin = "admin".equalsIgnoreCase(usuario.getRol().getNombre());
            boolean esAutor = post.getUsuario().getId().equals(idUsuario);

            if (esAdmin || esAutor) {
                post.setEstado(new Estado(1));
                postRepository.save(post);
                return true;
            }
        }
        return false;
    }

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

    public List<Post> obtenerPostsPorThreadYEstado(Integer idThread, int estadoId) {
        return postRepository.findByThread_IdAndEstado_IdOrderByFechaCreacionAsc(idThread, estadoId);
    }
}
