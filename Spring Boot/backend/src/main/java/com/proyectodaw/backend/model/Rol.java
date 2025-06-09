package com.proyectodaw.backend.model;

// Anotaciones JPA para mapear la clase como entidad en base de datos
import jakarta.persistence.*;
// Anotaciones de Lombok para generar código automáticamente
import lombok.*;

@Data // Lombok: genera getters, setters, equals, hashCode y toString
@NoArgsConstructor // Constructor sin argumentos (necesario para JPA)
@AllArgsConstructor // Constructor con todos los campos
@Entity // Marca la clase como una entidad JPA
@Table(name = "rol") // Mapea esta entidad a la tabla "rol"
public class Rol {

    @Id // Indica que este campo es la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Valor autoincremental
    @Column(name = "id_rol") // Nombre de la columna en la base de datos
    private Integer id;

    @Column(nullable = false, unique = true) // Campo obligatorio y único
    private String nombre; // Nombre del rol (por ejemplo: "admin", "usuario")
}
