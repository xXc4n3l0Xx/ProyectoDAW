package com.proyectodaw.backend.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String correo;
    private String contrasena;
}
