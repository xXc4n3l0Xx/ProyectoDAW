package com.proyectodaw.backend.service;

// Modelo del usuario
import com.proyectodaw.backend.model.Usuario;
// Repositorio para interactuar con la base de datos de usuarios
import com.proyectodaw.backend.repository.UsuarioRepository;

// Inyección de dependencias y servicio Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Utilidades de seguridad para encriptar contraseñas
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// DTO para la respuesta del login (token + datos del usuario)
import com.proyectodaw.backend.dto.AuthResponseDTO;
// Clase para generar y validar tokens JWT
import com.proyectodaw.backend.security.JwtUtil;

import java.util.Optional;

@Service // Marca esta clase como un servicio para ser gestionado por Spring
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Codificador de contraseñas con el algoritmo BCrypt
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Guarda un nuevo usuario, encriptando su contraseña antes de persistirlo
    public Usuario registrarUsuario(Usuario u) {
        u.setContrasena(passwordEncoder.encode(u.getContrasena())); // Encripta la contraseña
        return usuarioRepository.save(u); // Guarda en la base de datos
    }

    @Autowired
    private JwtUtil jwtUtil; // Utilidad para generar el token JWT

    // Lógica de login: valida credenciales y retorna token si son correctas
    public Optional<AuthResponseDTO> login(String identificador, String contrasena) {
        // Busca al usuario por correo o nombre (solo si está activo)
        Optional<Usuario> usuarioOpt = usuarioRepository
                .findByCorreoOrNombreAndEstado_Id(identificador, identificador, 1);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // Verifica que la contraseña ingresada coincida con la guardada (encriptada)
            if (passwordEncoder.matches(contrasena, usuario.getContrasena())) {
                // Genera el token JWT y devuelve respuesta con token + datos del usuario
                String token = jwtUtil.generarToken(usuario.getCorreo());
                return Optional.of(new AuthResponseDTO(token, usuario));
            }
        }

        // Si no coincide o no existe, devuelve vacío (login fallido)
        return Optional.empty();
    }

    // Actualiza los datos de un usuario (JPA detecta si ya existe por ID)
    public Usuario actualizarUsuario(Usuario u) {
        return usuarioRepository.save(u);
    }

    // Busca un usuario por su ID
    public Optional<Usuario> buscarPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    // Método auxiliar para encriptar contraseñas (por ejemplo, al editar perfil)
    public String encriptarContrasena(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
