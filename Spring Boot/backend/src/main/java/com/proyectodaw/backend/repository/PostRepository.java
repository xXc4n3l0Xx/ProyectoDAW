package com.proyectodaw.backend.repository;

import com.proyectodaw.backend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findByThread_IdAndEstado_IdOrderByFechaCreacionAsc(Integer idThread, Integer idEstado);
}
