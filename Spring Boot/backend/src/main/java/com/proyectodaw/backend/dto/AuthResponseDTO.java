package com.proyectodaw.backend.dto;

import com.proyectodaw.backend.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private Usuario usuario;
}
