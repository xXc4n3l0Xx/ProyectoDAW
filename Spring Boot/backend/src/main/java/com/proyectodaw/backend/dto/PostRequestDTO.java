package com.proyectodaw.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostRequestDTO {
    private Integer idThread;
    private Integer idUsuario;
    private String contenido;
}
