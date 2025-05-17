package com.proyectodaw.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_post")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_thread", nullable = false)
    private ForumThread thread;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "post_content", nullable = false)
    private String contenido;

    @Column(name = "post_created_at", nullable = false) // ✅ Se puede insertar manualmente
    private LocalDateTime fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "id_estado")
    private Estado estado;
}
