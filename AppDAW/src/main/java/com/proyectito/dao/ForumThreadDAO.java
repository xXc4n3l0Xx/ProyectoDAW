package com.proyectito.dao;

import com.proyectito.model.ForumThread;
import java.sql.*;
import java.util.*;

public class ForumThreadDAO {

    private final String url = "jdbc:postgresql://localhost:5432/proyectito";
    private final String user = "postgres";
    private final String password = "Cabj970423";

    public void insertarHilo(ForumThread hilo) throws SQLException {
        String sql = "INSERT INTO forum_thread (id_usuario, thread_title, thread_created_at) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, hilo.getIdUsuario());
            stmt.setString(2, hilo.getThreadTitle());
            stmt.setTimestamp(3, Timestamp.valueOf(hilo.getThreadCreatedAt()));
            stmt.executeUpdate();
        }
    }

    public List<ForumThread> obtenerTodosLosHilos() throws SQLException {
        List<ForumThread> hilos = new ArrayList<>();
        String sql = "SELECT * FROM forum_thread ORDER BY thread_created_at DESC";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ForumThread hilo = new ForumThread();
                hilo.setIdThread(rs.getInt("id_thread"));
                hilo.setIdUsuario(rs.getInt("id_usuario"));
                hilo.setThreadTitle(rs.getString("thread_title"));
                hilo.setThreadCreatedAt(rs.getTimestamp("thread_created_at").toLocalDateTime());
                hilos.add(hilo);
            }
        }

        return hilos;
    }
}
