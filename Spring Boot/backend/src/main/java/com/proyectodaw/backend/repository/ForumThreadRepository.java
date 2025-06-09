package com.proyectodaw.backend.repository;

// Importa el modelo que representa los hilos del foro
import com.proyectodaw.backend.model.ForumThread;

// Interfaz base de Spring Data JPA que proporciona métodos CRUD genéricos
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForumThreadRepository extends JpaRepository<ForumThread, Integer> {
    // Método personalizado para obtener hilos filtrados por estado, ordenados por fecha de creación descendente
    List<ForumThread> findByEstado_IdOrderByCreadoEnDesc(Integer estadoId);

    // Método por defecto para obtener solo los hilos activos (estado ID = 1)
    default List<ForumThread> findAllActivos() {
        return findByEstado_IdOrderByCreadoEnDesc(1);
    }
}
