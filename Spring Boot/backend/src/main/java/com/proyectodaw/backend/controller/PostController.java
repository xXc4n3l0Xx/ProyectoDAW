package com.proyectodaw.backend.controller;

import com.proyectodaw.backend.dto.PostRequestDTO;
import com.proyectodaw.backend.dto.PostResponseDTO;
import com.proyectodaw.backend.dto.PostUpdateDTO;
import com.proyectodaw.backend.model.Estado;
import com.proyectodaw.backend.model.ForumThread;
import com.proyectodaw.backend.model.Post;
import com.proyectodaw.backend.model.Usuario;
import com.proyectodaw.backend.repository.ForumThreadRepository;
import com.proyectodaw.backend.repository.UsuarioRepository;
import com.proyectodaw.backend.security.JwtUtil;
import com.proyectodaw.backend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ForumThreadRepository threadRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public PostResponseDTO crearPost(@RequestBody PostRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario()).orElseThrow();
        ForumThread thread = threadRepository.findById(dto.getIdThread()).orElseThrow();

        Post post = new Post();
        post.setContenido(dto.getContenido());
        post.setUsuario(usuario);
        post.setThread(thread);
        post.setEstado(new Estado(1, "Activo"));
        post.setFechaCreacion(LocalDateTime.now());

        Post guardado = postService.insertarPost(post);

        return construirPostResponseDTO(guardado);
    }

    @GetMapping("/thread/{idThread}")
    public List<PostResponseDTO> obtenerPorThread(@PathVariable Integer idThread) {
        return postService.obtenerPostsPorThread(idThread)
                .stream()
                .map(this::construirPostResponseDTO)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{idPost}")
    public void eliminarPost(@PathVariable Integer idPost,
                             @RequestHeader("Authorization") String authHeader) {
        Integer idUsuario = obtenerIdDesdeToken(authHeader);
        boolean resultado = postService.eliminarPost(idPost, idUsuario);
        if (!resultado) {
            throw new RuntimeException("No se pudo eliminar el post (no autorizado o no existe)");
        }
    }

    @PutMapping("/{idPost}/activar")
    public void activarPost(@PathVariable Integer idPost,
                            @RequestHeader("Authorization") String authHeader) {
        Integer idUsuario = obtenerIdDesdeToken(authHeader);
        boolean resultado = postService.activarPost(idPost, idUsuario);
        if (!resultado) {
            throw new RuntimeException("No se pudo activar el post (no autorizado o no existe)");
        }
    }

    @PutMapping("/{idPost}")
    public PostResponseDTO editarPost(@PathVariable Integer idPost,
                                      @RequestBody PostUpdateDTO dto,
                                      @RequestHeader("Authorization") String authHeader) {
        Integer idUsuario = obtenerIdDesdeToken(authHeader);
        Post actualizado = postService.editarPost(idPost, idUsuario, dto.getContenido());
        return construirPostResponseDTO(actualizado);
    }

    private PostResponseDTO construirPostResponseDTO(Post post) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setContenido(post.getContenido());
        dto.setFechaCreacion(post.getFechaCreacion().toString());
        dto.setNombreUsuario(post.getUsuario().getNombre());
        dto.setAvatarUsuario(post.getUsuario().getAvatar());
        return dto;
    }

    private Integer obtenerIdDesdeToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String correo = jwtUtil.getCorreoDesdeToken(token);
            Usuario usuario = usuarioRepository.findByCorreo(correo).orElseThrow();
            return usuario.getId();
        }
        throw new RuntimeException("Token inválido o ausente");
    }

    @GetMapping("/thread/{idThread}/eliminados")
    public List<PostResponseDTO> obtenerPostsEliminados(@PathVariable Integer idThread) {
        return postService.obtenerPostsPorThreadYEstado(idThread, 2)
                .stream()
                .map(this::construirPostResponseDTO)
                .collect(Collectors.toList());
    }
}