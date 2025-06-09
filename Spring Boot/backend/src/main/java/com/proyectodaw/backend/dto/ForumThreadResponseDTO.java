package com.proyectodaw.backend.dto;

// Anotación de Lombok que genera getters, setters, toString, equals y hashCode automáticamente
import lombok.Data;

@Data // Lombok genera métodos como get/set para todos los campos
public class ForumThreadResponseDTO {

    private Integer id;           // ID del hilo del foro
    private String titulo;        // Título del hilo
    private String creadoEn;      // Fecha de creación (en formato String)
    private String nombreUsuario; // Nombre del usuario que creó el hilo
    private String avatarUsuario; // Avatar del usuario creador
}
