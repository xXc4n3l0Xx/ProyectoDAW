package com.proyectodaw.backend.repository;

// Importa el modelo que representa un post del foro
import com.proyectodaw.backend.model.Post;

// Interfaz base de Spring Data JPA que proporciona métodos CRUD genéricos
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    // Método personalizado que obtiene los posts de un hilo específico y con cierto estado,
    // ordenados por fecha de creación de forma ascendente (más antiguos primero)
    List<Post> findByThread_IdAndEstado_IdOrderByFechaCreacionAsc(Integer idThread, Integer idEstado);
}
