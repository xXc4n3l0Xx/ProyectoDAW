package com.proyectodaw.backend.dto;

// Anotación de Lombok que genera automáticamente getters, setters, toString, etc.
import lombok.Data;

@Data // Lombok genera métodos como getNombre(), setPuntuacion(), etc.
public class UsuarioTopDTO {

    private String nombre;       // Nombre del usuario
    private String avatar;       // Avatar del usuario
    private Integer puntuacion;  // Puntuación del usuario (para ranking o top)
}
