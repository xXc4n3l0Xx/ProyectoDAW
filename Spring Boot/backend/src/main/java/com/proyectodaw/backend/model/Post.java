package com.proyectodaw.backend.model;

// Anotaciones JPA para mapear esta clase como entidad de base de datos
import jakarta.persistence.*;
// Anotaciones de Lombok para generar constructores, getters, setters, etc.
import lombok.*;
import java.time.LocalDateTime; // Para manejar la fecha y hora del post

@Data // Lombok: genera getters, setters, equals, hashCode y toString
@NoArgsConstructor // Constructor sin argumentos (requerido por JPA)
@AllArgsConstructor // Constructor con todos los campos
@Entity // Marca la clase como una entidad JPA
@Table(name = "post") // Mapea esta clase a la tabla "post"
public class Post {

    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
    @Column(name = "id_post") // Nombre de la columna en la base de datos
    private Integer id;

    @ManyToOne // Relación muchos-a-uno: varios posts pueden pertenecer a un mismo hilo
    @JoinColumn(name = "id_thread", nullable = false) // Clave foránea hacia ForumThread
    private ForumThread thread;

    @ManyToOne // Relación muchos-a-uno: varios posts pueden ser escritos por un mismo usuario
    @JoinColumn(name = "id_usuario", nullable = false) // Clave foránea hacia Usuario
    private Usuario usuario;

    @Column(name = "post_content", nullable = false) // Campo obligatorio
    private String contenido; // Contenido del post o mensaje

    @Column(name = "post_created_at", nullable = false) // Campo obligatorio
    private LocalDateTime fechaCreacion; // Fecha y hora en que se creó el post

    @ManyToOne // Relación muchos-a-uno con Estado (activo/inactivo/eliminado)
    @JoinColumn(name = "id_estado") // Clave foránea hacia Estado
    private Estado estado;
}
