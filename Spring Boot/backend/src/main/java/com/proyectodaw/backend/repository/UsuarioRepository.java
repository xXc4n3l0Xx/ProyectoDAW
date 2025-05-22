package com.proyectodaw.backend.repository;

import com.proyectodaw.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByCorreoAndContrasenaAndEstado_Id(String correo, String contrasena, Integer estadoId);

    Optional<Usuario> findByCorreo(String correo);

    Iterable<Usuario> findByEstado_Id(Integer estadoId);

    Iterable<Usuario> findByRol_Nombre(String nombreRol);

    Optional<Usuario> findByCorreoAndEstado_Id(String correo, Integer estadoId);

    Optional<Usuario> findByNombre(String nombre);

    List<Usuario> findTop5ByEstado_IdOrderByPuntuacionDescIdAsc(Integer estadoId);

}
