package com.proyectodaw.backend.dto;

import lombok.Data;

@Data
public class UsuarioUpdateDTO {
    private String nombre;
    private String correo;
    private String contrasena;
    private String avatar;
}
