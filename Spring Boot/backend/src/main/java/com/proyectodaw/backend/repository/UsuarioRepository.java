package com.proyectodaw.backend.repository;

// Importa el modelo Usuario
import com.proyectodaw.backend.model.Usuario;

// Interfaz base de Spring Data JPA que proporciona métodos CRUD y consulta por convención
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Busca un usuario por su correo (sin importar su estado)
    Optional<Usuario> findByCorreo(String correo);

    // Busca un usuario por su correo y estado (ej. activo)
    Optional<Usuario> findByCorreoAndEstado_Id(String correo, Integer estadoId);

    // Busca un usuario por su nombre de usuario
    Optional<Usuario> findByNombre(String nombre);

    // Obtiene el top 5 de usuarios con mayor puntuación, filtrando por estado
    // y ordenando por puntuación descendente y luego por ID ascendente
    List<Usuario> findTop5ByEstado_IdOrderByPuntuacionDescIdAsc(Integer estadoId);

    // Busca un usuario por correo o nombre, y por estado (uso para validar duplicados)
    Optional<Usuario> findByCorreoOrNombreAndEstado_Id(String correo, String nombre, Integer estadoId);
}
