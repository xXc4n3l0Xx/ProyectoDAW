package com.proyectodaw.backend.service;

import com.proyectodaw.backend.model.Estado;
import com.proyectodaw.backend.model.Usuario;
import com.proyectodaw.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.proyectodaw.backend.dto.AuthResponseDTO;
import com.proyectodaw.backend.security.JwtUtil;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Usuario registrarUsuario(Usuario u) {
        u.setContrasena(passwordEncoder.encode(u.getContrasena())); // 🔐 Encripta
        return usuarioRepository.save(u);
    }

    @Autowired
    private JwtUtil jwtUtil;

    public Optional<AuthResponseDTO> login(String correo, String contrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreoAndEstado_Id(correo, 1);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (passwordEncoder.matches(contrasena, usuario.getContrasena())) {
                String token = jwtUtil.generarToken(correo);
                return Optional.of(new AuthResponseDTO(token, usuario));
            }
        }

        return Optional.empty();
    }


    public Usuario actualizarUsuario(Usuario u) {
        return usuarioRepository.save(u); // JPA detecta si es update
    }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    public Optional<Usuario> buscarPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    public void eliminarUsuario(Integer id) {
        usuarioRepository.findById(id).ifPresent(usuario -> {
            usuario.setEstado(new Estado(2, "Inactivo"));
            usuarioRepository.save(usuario);
        });
    }

    public void activarUsuario(Integer id) {
        usuarioRepository.findById(id).ifPresent(usuario -> {
            usuario.setEstado(new Estado(1, "Activo"));
            usuarioRepository.save(usuario);
        });
    }

    public String encriptarContrasena(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

}
