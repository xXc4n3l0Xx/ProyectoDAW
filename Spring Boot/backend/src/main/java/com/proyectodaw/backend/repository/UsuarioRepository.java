package com.proyectodaw.backend.repository;

import com.proyectodaw.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Para login (en una implementación inicial)
    Optional<Usuario> findByCorreoAndContrasenaAndEstado_Id(String correo, String contrasena, Integer estadoId);

    // Buscar por correo (por ejemplo para validación de existencia)
    Optional<Usuario> findByCorreo(String correo);

    // Buscar todos los usuarios activos
    Iterable<Usuario> findByEstado_Id(Integer estadoId);

    // Buscar usuarios por rol
    Iterable<Usuario> findByRol_Nombre(String nombreRol);

    Optional<Usuario> findByCorreoAndEstado_Id(String correo, Integer estadoId);

    Optional<Usuario> findByNombre(String nombre);
}
