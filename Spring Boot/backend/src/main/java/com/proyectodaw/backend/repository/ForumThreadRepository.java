package com.proyectodaw.backend.repository;

import com.proyectodaw.backend.model.ForumThread;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ForumThreadRepository extends JpaRepository<ForumThread, Integer> {

    List<ForumThread> findByEstado_IdOrderByCreadoEnDesc(Integer estadoId);

    default List<ForumThread> findAllActivos() {
        return findByEstado_IdOrderByCreadoEnDesc(1);
    }

}
