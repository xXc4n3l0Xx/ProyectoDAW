package com.proyectodaw.backend.dto;

// Importa el modelo Usuario para incluirlo en la respuesta del login
import com.proyectodaw.backend.model.Usuario;

// Anotación de Lombok: genera automáticamente constructor con todos los argumentos
import lombok.AllArgsConstructor;
// Anotación de Lombok: genera getters, setters, toString, equals y hashCode
import lombok.Data;

@Data // Lombok genera todos los métodos básicos (get/set, toString, etc.)
@AllArgsConstructor // Lombok genera un constructor con todos los campos
public class AuthResponseDTO {
    private String token;     // JWT generado al iniciar sesión
    private Usuario usuario; // Datos del usuario autenticado
}
