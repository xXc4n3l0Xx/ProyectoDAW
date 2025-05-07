<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.proyectito.model.Post" %>
<%@ page import="com.proyectito.model.Usuario" %>
<html>
<head>
    <title>Posts del Hilo</title>
    <style>
        body {
            background-image: url("images/Foro.jpg");
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
            margin: 0;
            font-family: Arial, sans-serif;
            color: white;
            padding: 40px 0;
        }
        .contenido {
            background-color: rgba(0, 0, 0, 0.6);
            padding: 40px;
            width: 60%;
            max-width: 600px;
            border-radius: 15px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.5);
            margin: 0 auto;
        }
        input, button, textarea {
            margin: 10px 0;
            padding: 8px;
            width: 100%;
            font-size: 1rem;
            border: none;
            border-radius: 5px;
        }
        button {
            background-color: #4CAF50;
            color: white;
            cursor: pointer;
        }
        a {
            color: lightblue;
        }
        .post {
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 1px solid #ccc;
            display: flex;
            align-items: center;
        }
        .avatar {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            margin-right: 10px;
        }
        .eliminar-btn {
            background-color: #e53935;
            margin-top: 5px;
            padding: 4px 8px;
            font-size: 0.8rem;
            width: auto;
        }
    </style>
</head>
<body>
<div class="contenido">
    <h1>Posts del Hilo</h1>

    <%
        List<Post> posts = (List<Post>) request.getAttribute("posts");
        int idThread = (Integer) request.getAttribute("id_thread");
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (posts != null && !posts.isEmpty()) {
    %>
    <ul>
        <% for (Post post : posts) { %>
        <li class="post">
            <img src="<%= post.getUsuarioAvatar() %>" alt="Avatar" class="avatar" />
            <div>
                <p><strong><%= post.getUsuarioNombre() %></strong>: <%= post.getPostContent() %></p>
                <p><em><%= post.getPostCreatedAt() %></em></p>
                <% if (usuario != null && usuario.getIdUsuario() == post.getIdUsuario()) { %>
                <form action="eliminarPost" method="post" onsubmit="return confirm('¿Estás seguro de que quieres eliminar este post?');">
                    <input type="hidden" name="id_post" value="<%= post.getIdPost() %>">
                    <input type="hidden" name="id_thread" value="<%= idThread %>">
                    <input type="submit" value="Eliminar" class="eliminar-btn">
                </form>
                <% } %>
            </div>
        </li>
        <% } %>
    </ul>
    <% } else { %>
    <p>No hay posts en este hilo.</p>
    <% } %>

    <form action="crearPost" method="post">
        <input type="hidden" name="id_thread" value="<%= idThread %>">
        <textarea name="post_content" placeholder="Escribe tu respuesta" required></textarea><br>
        <input type="submit" value="Responder">
    </form>

    <form action="foro" method="get">
        <button type="submit">Volver al foro</button>
    </form>
    <form action="index.jsp" method="get">
        <button type="submit">Volver al inicio</button>
    </form>
</div>
</body>
</html>
