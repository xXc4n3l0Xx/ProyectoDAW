package com.proyectodaw.backend.security;

// Modelo de usuario autenticado
import com.proyectodaw.backend.model.Usuario;
// Repositorio para buscar usuarios por correo
import com.proyectodaw.backend.repository.UsuarioRepository;

// Clases para manejar filtros HTTP y autenticación
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Marca esta clase como un componente Spring (se carga automáticamente)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil; // Utilidad para validar y extraer datos del token JWT

    @Autowired
    private UsuarioRepository usuarioRepository; // Para buscar el usuario por correo

    // Método que se ejecuta automáticamente en cada petición HTTP
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath(); // Obtiene la ruta solicitada

        // Si es login o registro, se omite la verificación del token
        if (path.equals("/api/usuarios/login") || path.equals("/api/usuarios/registro")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization"); // Encabezado con el token

        // Si no hay token o no empieza con "Bearer ", se permite seguir sin autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extrae el token quitando el prefijo "Bearer "
        String token = authHeader.substring(7);
        // Extrae el correo electrónico del usuario desde el token
        String correo = jwtUtil.getCorreoDesdeToken(token);

        // Si el correo existe y no hay sesión autenticada aún
        if (correo != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Usuario usuario = usuarioRepository.findByCorreo(correo).orElse(null); // Busca al usuario

            // Si el token es válido y el usuario existe
            if (usuario != null && jwtUtil.validarToken(token)) {
                // Crea el objeto de autenticación sin credenciales ni roles (nulls)
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(usuario, null, null);

                // Asocia detalles de la petición (IP, agente de usuario, etc.)
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Establece el usuario autenticado en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continúa con el resto de filtros (si no hubo interrupción por error)
        filterChain.doFilter(request, response);
    }
}
