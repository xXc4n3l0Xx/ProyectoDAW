package com.proyectodaw.backend.dto;

// Anotación de Lombok que genera getters, setters, toString, equals y hashCode
import lombok.Data;

@Data // Lombok genera automáticamente los métodos getCorreo(), setContrasena(), etc.
public class LoginRequest {

    private String correo;      // Correo del usuario que desea iniciar sesión
    private String contrasena;  // Contraseña del usuario (sin encriptar, se validará en backend)
}
