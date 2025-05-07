package com.proyectito.dao;

import com.proyectito.model.Usuario;
import java.sql.*;

public class UsuarioDAO {

    private final String url = "jdbc:postgresql://localhost:5432/ProyectoDAW";
    private final String user = "postgres";
    private final String password = "Cabj970423";

    public void insertarUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (nombre, correo, contrasena, avatar, fecha_registro, id_estado) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getCorreo());
            stmt.setString(3, usuario.getContrasena());
            stmt.setString(4, usuario.getAvatar());
            stmt.setTimestamp(5, usuario.getFechaRegistro());
            stmt.setInt(6, usuario.getIdEstado());  // <<< ahora también inserta el id_estado
            stmt.executeUpdate();
        }
    }

    public Usuario obtenerUsuarioPorCredenciales(String correo, String contrasena) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE correo = ? AND contrasena = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correo);
            stmt.setString(2, contrasena);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setAvatar(rs.getString("avatar"));
                usuario.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                usuario.setIdEstado(rs.getInt("id_estado")); // <<< también recuperamos el estado
                return usuario;
            }
            return null;
        }
    }

    public void actualizarUsuario(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuario SET nombre = ?, correo = ?, contrasena = ?, id_estado = ? WHERE id_usuario = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getCorreo());
            stmt.setString(3, usuario.getContrasena());
            stmt.setInt(4, usuario.getIdEstado()); // <<< actualizar estado también
            stmt.setInt(5, usuario.getIdUsuario());
            stmt.executeUpdate();
        }
    }
}
