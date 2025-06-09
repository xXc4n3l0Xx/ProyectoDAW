package com.proyectodaw.backend.model;

// Anotaciones de JPA para mapear la clase como entidad en base de datos
import jakarta.persistence.*;
// Anotaciones de Lombok para generar constructores, getters, setters, etc.
import lombok.*;

@Data // Lombok genera getters, setters, equals, hashCode y toString
@NoArgsConstructor // Constructor vacío requerido por JPA
@AllArgsConstructor // Constructor con todos los campos
@Entity // Marca la clase como una entidad de JPA (tabla en la base de datos)
@Table(name = "estado") // Mapea la entidad a la tabla "estado"
public class Estado {

    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
    @Column(name = "id_estado") // Nombre de columna en la base de datos
    private Integer id;

    @Column(nullable = false, length = 20) // Columna obligatoria de texto corto
    private String descripcion; // Ejemplo: "Activo", "Inactivo", "Eliminado", etc.

    // Constructor útil para crear instancias con solo el ID (cuando no necesitas la descripción)
    public Estado(Integer id) {
        this.id = id;
    }
}
