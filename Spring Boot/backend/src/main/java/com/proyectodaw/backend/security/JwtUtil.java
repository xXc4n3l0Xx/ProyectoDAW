package com.proyectodaw.backend.security;

// Librerías de JJWT para crear, firmar y validar tokens JWT
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import java.security.Key; // Clave criptográfica para firmar el token
import java.util.Date;

@Component // Marca esta clase como componente de Spring (se puede inyectar con @Autowired)
public class JwtUtil {

    // Clave secreta usada para firmar los tokens (debe tener suficiente longitud para HS256)
    private final String secret = "12345678910111213141516171819202122232425";

    // Duración del token: 3 horas (en milisegundos)
    private final long expiracionMs = 1000 * 60 * 60 * 3;

    // Método privado para generar la clave HMAC a partir del secreto
    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Genera un token JWT con el correo como sujeto, firmado y con fecha de expiración
    public String generarToken(String correo) {
        return Jwts.builder()
                .setSubject(correo) // Asigna el correo como "subject" del token
                .setIssuedAt(new Date()) // Fecha de creación del token
                .setExpiration(new Date(System.currentTimeMillis() + expiracionMs)) // Fecha de expiración
                .signWith(getKey(), SignatureAlgorithm.HS256) // Firma con algoritmo HMAC SHA-256
                .compact(); // Devuelve el token como String
    }

    // Extrae el correo (subject) desde un token JWT válido
    public String getCorreoDesdeToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey()) // Usa la misma clave para validar la firma
                .build()
                .parseClaimsJws(token) // Valida y parsea el token
                .getBody()
                .getSubject(); // Devuelve el "subject" (correo)
    }

    // Valida si el token es correcto y no está expirado o mal formado
    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
            return true; // Token válido
        } catch (JwtException e) {
            return false; // Token inválido, expirado o manipulado
        }
    }
}
