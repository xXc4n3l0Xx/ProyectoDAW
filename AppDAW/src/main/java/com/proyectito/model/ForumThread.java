package com.proyectito.model;

import java.time.LocalDateTime;

public class ForumThread {
    private int idThread;
    private int idUsuario;
    private String threadTitle;
    private LocalDateTime threadCreatedAt;

    public ForumThread() {}

    public int getIdThread() { return idThread; }
    public void setIdThread(int idThread) { this.idThread = idThread; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getThreadTitle() { return threadTitle; }
    public void setThreadTitle(String threadTitle) { this.threadTitle = threadTitle; }

    public LocalDateTime getThreadCreatedAt() { return threadCreatedAt; }
    public void setThreadCreatedAt(LocalDateTime threadCreatedAt) { this.threadCreatedAt = threadCreatedAt; }
}