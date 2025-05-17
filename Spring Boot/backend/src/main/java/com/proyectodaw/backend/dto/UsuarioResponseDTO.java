package com.proyectodaw.backend.dto;

import lombok.Data;

@Data
public class UsuarioResponseDTO {
    private Integer id;
    private String nombre;
    private String correo;
    private String avatar;
    private String fechaRegistro;
    private Integer puntuacion;
    private String estado;
    private String rol;
}
