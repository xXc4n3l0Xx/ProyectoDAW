package com.proyectodaw.backend.config;

// Filtro personalizado que valida el JWT en cada petición
import com.proyectodaw.backend.security.JwtAuthenticationFilter;

// Permite declarar beans de configuración para Spring
import org.springframework.context.annotation.Bean;

// Marca la clase como una clase de configuración
import org.springframework.context.annotation.Configuration;

// Habilita la configuración de seguridad web de Spring Security
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

// Clase principal para configurar seguridad HTTP
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

// Controla la política de manejo de sesiones (ej. sin sesión en modo JWT)
import org.springframework.security.config.http.SessionCreationPolicy;

// Define la cadena de filtros de seguridad para peticiones HTTP
import org.springframework.security.web.SecurityFilterChain;

// Permite inyectar beans automáticamente
import org.springframework.beans.factory.annotation.Autowired;

// Filtro de Spring que autentica usuarios con username/password (reemplazado por JWT)
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Configura los orígenes permitidos (CORS)
import org.springframework.web.cors.CorsConfiguration;

// Fuente de configuración CORS usada por Spring Security
import org.springframework.web.cors.CorsConfigurationSource;

// Implementación concreta de CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// Clase utilitaria para crear listas inmutables de métodos, orígenes, headers
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Inyecta el filtro que valida el token JWT en cada solicitud
    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    // Define la cadena de filtros y reglas de seguridad para las peticiones HTTP
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Habilita y configura CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Deshabilita CSRF (no se necesita para APIs con JWT)
                .csrf(csrf -> csrf.disable())
                // Usa sesiones sin estado (JWT, no sesiones de servidor)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configura rutas públicas y privadas
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/usuarios/login", "/api/usuarios/registro").permitAll()
                        .requestMatchers("/api/usuarios/top").permitAll()
                        .anyRequest().authenticated() // El resto requiere autenticación
                )
                // Agrega el filtro JWT antes del filtro estándar de autenticación
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build(); // Devuelve la configuración construida
    }

    // Configura los parámetros de CORS para permitir llamadas desde el frontend
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200","http://192.168.217.74:4200")); // Dominios permitidos
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Métodos permitidos
        configuration.setAllowedHeaders(List.of("*")); // Todos los headers permitidos
        configuration.setAllowCredentials(true); // Permitir cookies/autenticación

        // Aplica esta configuración a todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}