package com.proyectodaw.backend.service;

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
        u.setContrasena(passwordEncoder.encode(u.getContrasena()));
        return usuarioRepository.save(u);
    }

    @Autowired
    private JwtUtil jwtUtil;

    public Optional<AuthResponseDTO> login(String identificador, String contrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository
                .findByCorreoOrNombreAndEstado_Id(identificador, identificador, 1);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (passwordEncoder.matches(contrasena, usuario.getContrasena())) {
                String token = jwtUtil.generarToken(usuario.getCorreo());
                return Optional.of(new AuthResponseDTO(token, usuario));
            }
        }

        return Optional.empty();
    }

    public Usuario actualizarUsuario(Usuario u) {
        return usuarioRepository.save(u); // JPA detecta si es update
    }

    public Optional<Usuario> buscarPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    public String encriptarContrasena(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

}