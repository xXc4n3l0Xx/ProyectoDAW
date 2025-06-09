package com.proyectodaw.backend.controller;

// DTO para enviar información resumida de un hilo al frontend
import com.proyectodaw.backend.dto.ForumThreadResponseDTO;
// DTO para recibir los datos necesarios para crear un hilo
import com.proyectodaw.backend.dto.HiloRequest;
// Modelo de estado del hilo (activo, eliminado, etc.)
import com.proyectodaw.backend.model.Estado;
// Modelo de hilo del foro
import com.proyectodaw.backend.model.ForumThread;
// Modelo de usuario creador del hilo
import com.proyectodaw.backend.model.Usuario;
// Repositorio de usuarios, para buscar datos desde la base
import com.proyectodaw.backend.repository.UsuarioRepository;
// Utilidad para extraer datos del JWT (correo, etc.)
import com.proyectodaw.backend.security.JwtUtil;
// Servicio que contiene la lógica de negocio de los hilos
import com.proyectodaw.backend.service.ForumThreadService;

// Inyección de dependencias
import org.springframework.beans.factory.annotation.Autowired;
// Para devolver códigos HTTP (como 200, 403, etc.)
import org.springframework.http.HttpStatus;
// Anotaciones para mapear peticiones REST
import org.springframework.web.bind.annotation.*;
// Excepción HTTP personalizada con código y mensaje
import org.springframework.web.server.ResponseStatusException;
// Fecha y hora de creación del hilo
import java.time.LocalDateTime;
// Lista de resultados para GET
import java.util.List;
// Mapa para recibir campos dinámicos del cuerpo (ej. solo "titulo")
import java.util.Map;

@RestController // Marca esta clase como un controlador REST
@RequestMapping("/api/hilos") // Ruta base para todos los endpoints de hilos
public class ForumThreadController {

    @Autowired
    private ForumThreadService threadService; // Servicio con la lógica de los hilos

    @Autowired
    private JwtUtil jwtUtil; // Utilidad para trabajar con el token JWT

    @Autowired
    private UsuarioRepository usuarioRepository; // Repositorio para acceder a usuarios

    // Endpoint POST para crear un nuevo hilo
    @PostMapping
    public ForumThreadResponseDTO crearHilo(@RequestBody HiloRequest hiloDTO) {
        ForumThread hilo = new ForumThread();
        hilo.setTitulo(hiloDTO.getTitulo()); // Asigna el título del DTO
        hilo.setUsuario(new Usuario(hiloDTO.getIdUsuario())); // Asigna el usuario por ID
        hilo.setEstado(new Estado(1, "Activo")); // Asigna estado "Activo"
        hilo.setCreadoEn(LocalDateTime.now()); // Fecha actual

        ForumThread hiloGuardado = threadService.crearHilo(hilo); // Guarda el hilo

        // Crea un DTO con la respuesta resumida para el frontend
        ForumThreadResponseDTO dto = new ForumThreadResponseDTO();
        dto.setId(hiloGuardado.getId());
        dto.setTitulo(hiloGuardado.getTitulo());
        dto.setCreadoEn(hiloGuardado.getCreadoEn().toString());
        dto.setNombreUsuario(hiloGuardado.getUsuario().getNombre());
        dto.setAvatarUsuario(hiloGuardado.getUsuario().getAvatar());

        return dto;
    }

    // Endpoint GET para obtener todos los hilos activos
    @GetMapping
    public List<ForumThreadResponseDTO> obtenerHilos() {
        return threadService.obtenerHilosActivos();
    }

    // Endpoint GET para obtener hilos eliminados (solo admin)
    @GetMapping("/eliminados")
    public List<ForumThreadResponseDTO> obtenerHilosEliminados(@RequestHeader("Authorization") String token) {
        // Extrae correo del token JWT
        String correo = jwtUtil.getCorreoDesdeToken(token.replace("Bearer ", ""));
        // Busca al usuario correspondiente
        Usuario usuario = usuarioRepository.findByCorreoAndEstado_Id(correo, 1).orElseThrow();

        // Solo permite el acceso si el usuario es administrador
        if (!"admin".equalsIgnoreCase(usuario.getRol().getNombre())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo administradores pueden ver esto");
        }

        return threadService.obtenerHilosPorEstado(2); // Estado 2 = Eliminado
    }

    // Endpoint DELETE para eliminar un hilo (cambia su estado)
    @DeleteMapping("/{idHilo}/usuario/{idUsuario}")
    public void eliminarHilo(@PathVariable Integer idHilo, @PathVariable Integer idUsuario) {
        threadService.eliminarHilo(idHilo, idUsuario);
    }

    // Endpoint PUT para reactivar un hilo eliminado (solo admin)
    @PutMapping("/{idHilo}/activar/usuario/{idUsuario}")
    public void activarHilo(@PathVariable Integer idHilo, @PathVariable Integer idUsuario) {
        threadService.activarHilo(idHilo, idUsuario);
    }

    // Endpoint PUT para editar el título de un hilo
    @PutMapping("/{idHilo}/usuario/{idUsuario}")
    public ForumThreadResponseDTO editarHilo(@PathVariable Integer idHilo,
                                             @PathVariable Integer idUsuario,
                                             @RequestBody Map<String, String> body) {
        String nuevoTitulo = body.get("titulo"); // Obtiene el nuevo título
        ForumThread hilo = threadService.editarHilo(idHilo, idUsuario, nuevoTitulo); // Edita el hilo

        // Prepara y devuelve la respuesta actualizada
        ForumThreadResponseDTO respuesta = new ForumThreadResponseDTO();
        respuesta.setId(hilo.getId());
        respuesta.setTitulo(hilo.getTitulo());
        respuesta.setNombreUsuario(hilo.getUsuario().getNombre());
        respuesta.setAvatarUsuario(hilo.getUsuario().getAvatar());
        respuesta.setCreadoEn(hilo.getCreadoEn().toString());

        return respuesta;
    }
}
