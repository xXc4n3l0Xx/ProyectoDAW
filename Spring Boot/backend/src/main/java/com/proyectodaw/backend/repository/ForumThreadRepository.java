package com.proyectodaw.backend.repository;

import com.proyectodaw.backend.model.ForumThread;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForumThreadRepository extends JpaRepository<ForumThread, Integer> {

    // Hilos activos ordenados por fecha descendente
    List<ForumThread> findByEstado_IdOrderByCreadoEnDesc(Integer estadoId);

    // O si solo quieres activos y más claro
    default List<ForumThread> findAllActivos() {
        return findByEstado_IdOrderByCreadoEnDesc(1);
    }

}
