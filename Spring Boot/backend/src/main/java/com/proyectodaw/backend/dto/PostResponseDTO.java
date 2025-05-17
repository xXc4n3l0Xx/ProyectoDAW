package com.proyectodaw.backend.dto;

import lombok.Data;

@Data
public class PostResponseDTO {
    private Integer id;
    private String contenido;
    private String fechaCreacion;
    private String nombreUsuario;
    private String avatarUsuario;
}
