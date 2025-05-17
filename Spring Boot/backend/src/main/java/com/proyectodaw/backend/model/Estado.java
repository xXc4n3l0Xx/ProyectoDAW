package com.proyectodaw.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "estado")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Integer id;

    @Column(nullable = false, length = 20)
    private String descripcion;

    // ✅ Constructor para usar solo el ID
    public Estado(Integer id) {
        this.id = id;
    }
}
