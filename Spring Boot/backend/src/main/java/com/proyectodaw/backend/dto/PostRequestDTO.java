package com.proyectodaw.backend.dto;

import lombok.Data;

@Data
public class PostRequestDTO {
    private Integer idThread;
    private Integer idUsuario;
    private String contenido;
}
