<%@ page import="com.proyectito.model.Usuario" %>
<%
    HttpSession sesion = request.getSession(false);
    Usuario usuario = (sesion != null) ? (Usuario) sesion.getAttribute("usuario") : null;
%>

<!DOCTYPE html>
<html>
<head>
    <title>Inicio</title>
    <style>
        body {
            background-image: url("images/inicio.jpg");
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
    <% if (usuario != null) { %>
    <h2>Bienvenido, <%= usuario.getNombre() %>!</h2>
    <img src="<%= usuario.getAvatar() %>" alt="Avatar" style="width:100px; height:100px; border-radius:50%; border:2px solid white; margin-bottom: 20px;"/>
    <form action="foro" method="get">
        <button type="submit">Ir al foro</button>
    </form>
    <form action="editar_usuario.jsp" method="post">
        <button type="submit">Editar cuenta</button>
    </form>
    <form action="logout.jsp" method="post">
        <button type="submit">Cerrar sesion</button>
    </form>
    <% } else { %>
    <h2>Iniciar sesion</h2>

    <% String error = request.getParameter("error"); %>
    <% if (error != null) { %>
    <p style="color:red;">Correo o contrasena incorrectos</p>
    <% } %>
    <form action="login" method="post">
        Correo: <input type="email" name="correo" required /><br/>
        Contrasena: <input type="password" name="contrasena" required /><br/>
        <input type="submit" value="Ingresar" />
    </form>
    <p><a href="formulario.jsp">No tienes cuenta? Registrate aqui</a></p>
    <% } %>
</div>

</body>
</html>
