package com.proyectodaw.backend.dto;

// Anotación de Lombok que genera automáticamente getters, setters, toString, etc.
import lombok.Data;

@Data // Lombok genera los métodos get/set para todos los campos
public class PostResponseDTO {

    private Integer id;             // ID del post
    private String contenido;       // Contenido del post
    private String fechaCreacion;   // Fecha y hora de creación (en formato String)
    private String nombreUsuario;   // Nombre del usuario que escribió el post
    private String avatarUsuario;   // Avatar del usuario que escribió el post
}
