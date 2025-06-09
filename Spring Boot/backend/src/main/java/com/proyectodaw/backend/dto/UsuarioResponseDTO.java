package com.proyectodaw.backend.dto;

// Anotación de Lombok para generar automáticamente getters, setters, toString, etc.
import lombok.Data;

@Data // Lombok genera todos los métodos básicos (get/set, equals, hashCode, toString)
public class UsuarioResponseDTO {

    private Integer id;            // ID único del usuario
    private String nombre;         // Nombre de usuario visible
    private String correo;         // Correo electrónico (único)
    private String avatar;         // URL o nombre del avatar asignado
    private String fechaRegistro;  // Fecha en que se registró el usuario (como String)
    private Integer puntuacion;    // Puntuación obtenida (por ejemplo, del juego)
    private String estado;         // Estado del usuario (ej. "Activo", "Inactivo")
    private String rol;            // Rol del usuario (ej. "admin", "usuario")
}
