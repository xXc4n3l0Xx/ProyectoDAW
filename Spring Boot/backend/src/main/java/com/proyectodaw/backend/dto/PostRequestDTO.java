package com.proyectodaw.backend.dto;

// Anotación de Lombok que genera getters, setters, toString, equals y hashCode
import lombok.Data;

@Data // Lombok se encarga de generar automáticamente todos los métodos necesarios
public class PostRequestDTO {

    private Integer idThread;   // ID del hilo al que pertenece el post
    private Integer idUsuario;  // ID del usuario que escribe el post
    private String contenido;   // Contenido del mensaje o respuesta
}
