<%@ page import="com.proyectito.model.Usuario" %>
<%
    HttpSession sesion = request.getSession(false);
    Usuario usuario = (sesion != null) ? (Usuario) sesion.getAttribute("usuario") : null;
    if (usuario == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Editar Perfil</title>
    <style>
        body {
            background-image: url("images/Desenmascarar.png");
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
        button, input[type="submit"] {
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
    <h2>Editar datos de usuario</h2>
    <form action="editarUsuario" method="post">
        Nombre: <input type="text" name="nombre" value="<%= usuario.getNombre() %>" required /><br/>
        Correo: <input type="email" name="correo" value="<%= usuario.getCorreo() %>" required /><br/>
        Contraseña: <input type="password" name="contrasena" value="<%= usuario.getContrasena() %>" required /><br/>
        <input type="submit" value="Actualizar" />
    </form>
    <form action="index.jsp" method="get">
        <button type="submit">Cancelar</button>
    </form>
</div>

</body>
</html>
