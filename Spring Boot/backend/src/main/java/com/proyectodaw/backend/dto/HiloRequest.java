package com.proyectodaw.backend.dto;

// Anotación de Lombok que genera automáticamente getters, setters, toString, etc.
import lombok.Data;

@Data // Lombok genera métodos como getIdUsuario(), setTitulo(), etc.
public class HiloRequest {

    private Integer idUsuario; // ID del usuario que está creando el hilo
    private String titulo;     // Título del nuevo hilo
}
