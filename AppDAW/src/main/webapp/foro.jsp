<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.proyectito.model.ForumThread" %>
<html>
<head>
    <title>Foro</title>
    <style>
        body {
            background-image: url("images/Foro.jpg");
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
            height: 100vh;
            margin: 0;
            font-family: Arial, sans-serif;
            color: white;

            display: flex;
            justify-content: center;
            align-items: center;
        }
        .contenido {
            background-color: rgba(0, 0, 0, 0.6);
            padding: 40px;
            width: 60%;
            max-width: 600px;
            border-radius: 15px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.5);
        }
        input, button {
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
    </style>
</head>
<body>
<div class="contenido">
<h1>Hilos del Foro</h1>

<%
    List<ForumThread> hilos = (List<ForumThread>) request.getAttribute("hilos");
    if (hilos != null && !hilos.isEmpty()) {
%>
<ul>
    <% for (ForumThread hilo : hilos) { %>
    <li>
        <a href="verPosts?id_thread=<%= hilo.getIdThread() %>">
            <%= hilo.getThreadTitle() %> - creado el <%= hilo.getThreadCreatedAt() %>
        </a>
    </li>
    <% } %>
</ul>
<%
} else {
%>
<p>No hay hilos en el foro.</p>
<%
    }
%>

<h2>Crear nuevo hilo</h2>
<form action="crearHilo" method="post">
    <input type="text" name="thread_title" placeholder="Título del hilo" required><br>
    <input type="submit" value="Crear hilo">
</form>

<form action="index.jsp" method="get">
    <button type="submit">Volver al inicio</button>
</form>
</div>
</body>
</html>
