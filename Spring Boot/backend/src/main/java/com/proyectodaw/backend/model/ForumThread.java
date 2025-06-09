package com.proyectodaw.backend.model;

// Anotaciones JPA para mapear la clase a la base de datos
import jakarta.persistence.*;
// Anotaciones de Lombok para generar código automáticamente
import lombok.*;
import java.time.LocalDateTime; // Representa fecha y hora sin zona horaria

@Data // Lombok: genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // Constructor vacío (requerido por JPA)
@AllArgsConstructor // Constructor con todos los campos
@Entity // Indica que esta clase es una entidad JPA
@Table(name = "forum_thread") // Mapea a la tabla "forum_thread" en la base de datos
public class ForumThread {

    @Id // Indica que este campo es la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
    @Column(name = "id_thread") // Nombre de columna en la base de datos
    private Integer id;

    @ManyToOne // Relación muchos-a-uno con la entidad Usuario
    @JoinColumn(name = "id_usuario", nullable = false) // Clave foránea a Usuario
    private Usuario usuario;

    @Column(name = "thread_title", nullable = false) // Campo obligatorio
    private String titulo; // Título del hilo

    @Column(name = "thread_created_at", insertable = false, updatable = false)
    private LocalDateTime creadoEn; // Fecha de creación, asignada por la BD

    @ManyToOne // Relación muchos-a-uno con Estado (activo/inactivo)
    @JoinColumn(name = "id_estado") // Clave foránea a Estado
    private Estado estado;

    // Constructor auxiliar que permite instanciar solo con el ID
    public ForumThread(Integer id) {
        this.id = id;
    }
}
