package com.proyectodaw.backend.controller;

// DTO que recibe los datos para crear o editar un post
import com.proyectodaw.backend.dto.PostRequestDTO;
// DTO que se devuelve al frontend con los datos del post formateados
import com.proyectodaw.backend.dto.PostResponseDTO;
// Modelo que representa el estado (activo, eliminado, etc.)
import com.proyectodaw.backend.model.Estado;
// Modelo del hilo al que pertenece el post
import com.proyectodaw.backend.model.ForumThread;
// Modelo de post
import com.proyectodaw.backend.model.Post;
// Modelo de usuario que crea el post
import com.proyectodaw.backend.model.Usuario;
// Repositorio para buscar hilos por ID
import com.proyectodaw.backend.repository.ForumThreadRepository;
// Repositorio para acceder a los usuarios
import com.proyectodaw.backend.repository.UsuarioRepository;
// Utilidad para extraer datos del token JWT (ej. correo)
import com.proyectodaw.backend.security.JwtUtil;
// Servicio con la lógica del manejo de posts
import com.proyectodaw.backend.service.PostService;

import org.springframework.beans.factory.annotation.Autowired; // Inyección de dependencias
import org.springframework.web.bind.annotation.*; // Anotaciones REST
import java.time.LocalDateTime; // Fecha de creación de los posts
import java.util.List; // Para manejar listas de resultados
import java.util.Map; // Para recibir datos dinámicos del cuerpo (por ejemplo, contenido nuevo)
import java.util.stream.Collectors; // Para transformar listas de entidad a DTOs

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/posts") // Ruta base para todos los endpoints de posts
public class PostController {

    @Autowired
    private PostService postService; // Servicio que maneja lógica de negocio de los posts

    @Autowired
    private UsuarioRepository usuarioRepository; // Para buscar usuarios por ID o correo

    @Autowired
    private ForumThreadRepository threadRepository; // Para buscar hilos por ID

    @Autowired
    private JwtUtil jwtUtil; // Utilidad para extraer info del token JWT

    // Endpoint POST para crear un nuevo post
    @PostMapping
    public PostResponseDTO crearPost(@RequestBody PostRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario()).orElseThrow(); // Busca usuario
        ForumThread thread = threadRepository.findById(dto.getIdThread()).orElseThrow(); // Busca hilo

        Post post = new Post();
        post.setContenido(dto.getContenido());
        post.setUsuario(usuario);
        post.setThread(thread);
        post.setEstado(new Estado(1, "Activo")); // Estado 1 = Activo
        post.setFechaCreacion(LocalDateTime.now());

        Post guardado = postService.insertarPost(post); // Guarda el post

        return construirPostResponseDTO(guardado); // Devuelve DTO como respuesta
    }

    // Endpoint GET para obtener todos los posts activos de un hilo
    @GetMapping("/thread/{idThread}")
    public List<PostResponseDTO> obtenerPorThread(@PathVariable Integer idThread) {
        return postService.obtenerPostsPorThread(idThread)
                .stream()
                .map(this::construirPostResponseDTO) // Convierte a DTO
                .collect(Collectors.toList());
    }

    // Endpoint DELETE para marcar un post como eliminado (cambia su estado)
    @DeleteMapping("/{idPost}")
    public void eliminarPost(@PathVariable Integer idPost,
                             @RequestHeader("Authorization") String authHeader) {
        Integer idUsuario = obtenerIdDesdeToken(authHeader); // Extrae ID desde token
        boolean resultado = postService.eliminarPost(idPost, idUsuario);
        if (!resultado) {
            throw new RuntimeException("No se pudo eliminar el post (no autorizado o no existe)");
        }
    }

    // Endpoint PUT para reactivar un post eliminado (estado = Activo)
    @PutMapping("/{idPost}/activar")
    public void activarPost(@PathVariable Integer idPost,
                            @RequestHeader("Authorization") String authHeader) {
        Integer idUsuario = obtenerIdDesdeToken(authHeader);
        boolean resultado = postService.activarPost(idPost, idUsuario);
        if (!resultado) {
            throw new RuntimeException("No se pudo activar el post (no autorizado o no existe)");
        }
    }

    // Endpoint PUT para editar el contenido de un post
    @PutMapping("/{idPost}")
    public PostResponseDTO editarPost(@PathVariable Integer idPost,
                                      @RequestBody Map<String, String> body,
                                      @RequestHeader("Authorization") String authHeader) {
        Integer idUsuario = obtenerIdDesdeToken(authHeader);
        String nuevoContenido = body.get("contenido");
        Post actualizado = postService.editarPost(idPost, idUsuario, nuevoContenido);
        return construirPostResponseDTO(actualizado);
    }

    // Método auxiliar para construir el DTO de respuesta con los datos del post
    private PostResponseDTO construirPostResponseDTO(Post post) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setContenido(post.getContenido());
        dto.setFechaCreacion(post.getFechaCreacion().toString());
        dto.setNombreUsuario(post.getUsuario().getNombre());
        dto.setAvatarUsuario(post.getUsuario().getAvatar());
        return dto;
    }

    // Método auxiliar para extraer el ID del usuario desde el token JWT
    private Integer obtenerIdDesdeToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String correo = jwtUtil.getCorreoDesdeToken(token);
            Usuario usuario = usuarioRepository.findByCorreo(correo).orElseThrow();
            return usuario.getId();
        }
        throw new RuntimeException("Token inválido o ausente");
    }

    // Endpoint GET para obtener posts eliminados (estado = 2) de un hilo específico
    @GetMapping("/thread/{idThread}/eliminados")
    public List<PostResponseDTO> obtenerPostsEliminados(@PathVariable Integer idThread) {
        return postService.obtenerPostsPorThreadYEstado(idThread, 2)
                .stream()
                .map(this::construirPostResponseDTO)
                .collect(Collectors.toList());
    }
}
