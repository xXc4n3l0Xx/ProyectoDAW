package com.proyectito.dao;

import com.proyectito.model.Post;
import java.sql.*;
import java.util.*;

public class PostDAO {

    private final String url = "jdbc:postgresql://localhost:5432/ProyectoDAW";
    private final String user = "postgres";
    private final String password = "Cabj970423";

    public void insertarPost(Post post) throws SQLException {
        String sql = "INSERT INTO post (id_thread, id_usuario, post_content, post_created_at) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, post.getIdThread());
            stmt.setInt(2, post.getIdUsuario());
            stmt.setString(3, post.getPostContent());
            stmt.setTimestamp(4, Timestamp.valueOf(post.getPostCreatedAt()));
            stmt.executeUpdate();
        }
    }

    public List<Post> obtenerPostsPorThread(int idThread) throws SQLException {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT p.*, u.nombre, u.avatar " +
                "FROM post p " +
                "JOIN usuario u ON p.id_usuario = u.id_usuario " +
                "WHERE p.id_thread = ? " +
                "ORDER BY p.post_created_at ASC";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idThread);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Post post = new Post();
                    post.setIdPost(rs.getInt("id_post"));
                    post.setIdThread(rs.getInt("id_thread"));
                    post.setIdUsuario(rs.getInt("id_usuario"));
                    post.setPostContent(rs.getString("post_content"));
                    post.setPostCreatedAt(rs.getTimestamp("post_created_at").toLocalDateTime());
                    post.setUsuarioNombre(rs.getString("nombre"));
                    post.setUsuarioAvatar(rs.getString("avatar"));
                    posts.add(post);
                }
            }
        }

        return posts;
    }

    public void eliminarPost(int idPost, int idUsuario) throws SQLException {
        String sql = "DELETE FROM post WHERE id_post = ? AND id_usuario = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPost);
            stmt.setInt(2, idUsuario);
            stmt.executeUpdate();
        }
    }
}
