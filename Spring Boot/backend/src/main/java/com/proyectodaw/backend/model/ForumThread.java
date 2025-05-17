package com.proyectodaw.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "forum_thread")
public class ForumThread {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_thread")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "thread_title", nullable = false)
    private String titulo;

    @Column(name = "thread_created_at", insertable = false, updatable = false)
    private LocalDateTime creadoEn;

    @ManyToOne
    @JoinColumn(name = "id_estado")
    private Estado estado;

    public ForumThread(Integer id) {
        this.id = id;
    }

}