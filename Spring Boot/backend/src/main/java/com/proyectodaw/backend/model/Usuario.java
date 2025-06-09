package com.proyectodaw.backend.model;

// Anotaciones JPA para mapear esta clase como entidad de base de datos
import jakarta.persistence.*;
// Anotaciones de Lombok para generar constructores, getters, setters, etc.
import lombok.*;
import java.sql.Timestamp; // Tipo de dato para representar fecha y hora exacta

@Data // Lombok: genera getters, setters, equals, hashCode y toString
@NoArgsConstructor // Constructor sin argumentos (requerido por JPA)
@AllArgsConstructor // Constructor con todos los campos
@Entity // Indica que esta clase es una entidad JPA
@Table(name = "usuario") // Mapea esta clase a la tabla "usuario"
public class Usuario {

    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
    @Column(name = "id_usuario") // Nombre de columna en la BD
    private Integer id;

    @Column(nullable = false, unique = true) // Campo obligatorio y único
    private String nombre; // Nombre de usuario

    @Column(nullable = false, unique = true) // Campo obligatorio y único
    private String correo; // Correo electrónico del usuario

    private String contrasena; // Contraseña (normalmente se guarda encriptada)

    private String avatar; // Ruta o nombre de imagen de perfil

    @Column(name = "fecha_registro", insertable = false, updatable = false)
    private Timestamp fechaRegistro; // Fecha en que se registró el usuario (establecida por la BD)

    private Integer puntuacion; // Puntuación del usuario (usado en rankings o juegos)

    @ManyToOne // Relación muchos-a-uno con Estado
    @JoinColumn(name = "id_estado") // Clave foránea hacia tabla estado
    private Estado estado; // Estado del usuario (activo, inactivo, etc.)

    @ManyToOne // Relación muchos-a-uno con Rol
    @JoinColumn(name = "id_rol") // Clave foránea hacia tabla rol
    private Rol rol; // Rol del usuario (admin, usuario, etc.)

    // Constructor auxiliar que permite crear un usuario solo con su ID (útil para referencias rápidas)
    public Usuario(Integer id) {
        this.id = id;
    }
}
