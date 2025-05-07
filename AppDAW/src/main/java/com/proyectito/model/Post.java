package com.proyectito.model;

import java.time.LocalDateTime;

public class Post {
    private int idPost;
    private int idThread;
    private int idUsuario;
    private String postContent;
    private LocalDateTime postCreatedAt;
    private String usuarioNombre;
    private String usuarioAvatar;

    public Post() {}

    public int getIdPost() { return idPost; }
    public void setIdPost(int idPost) { this.idPost = idPost; }

    public int getIdThread() { return idThread; }
    public void setIdThread(int idThread) { this.idThread = idThread; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getPostContent() { return postContent; }
    public void setPostContent(String postContent) { this.postContent = postContent; }

    public LocalDateTime getPostCreatedAt() { return postCreatedAt; }
    public void setPostCreatedAt(LocalDateTime postCreatedAt) { this.postCreatedAt = postCreatedAt; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }

    public String getUsuarioAvatar() { return usuarioAvatar; }
    public void setUsuarioAvatar(String usuarioAvatar) { this.usuarioAvatar = usuarioAvatar; }

}
