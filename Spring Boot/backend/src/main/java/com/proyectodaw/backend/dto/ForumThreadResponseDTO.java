package com.proyectodaw.backend.dto;

import lombok.Data;

@Data
public class ForumThreadResponseDTO {
    private Integer id;
    private String titulo;
    private String creadoEn;
    private String nombreUsuario;
    private String avatarUsuario;
}
