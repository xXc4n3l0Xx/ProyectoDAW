package com.proyectodaw.backend.repository;

import com.proyectodaw.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByCorreoAndEstado_Id(String correo, Integer estadoId);

    Optional<Usuario> findByNombre(String nombre);

    List<Usuario> findTop5ByEstado_IdOrderByPuntuacionDescIdAsc(Integer estadoId);

    Optional<Usuario> findByCorreoOrNombreAndEstado_Id(String correo, String nombre, Integer estadoId);

}