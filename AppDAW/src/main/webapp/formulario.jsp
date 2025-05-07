<!DOCTYPE html>
<html>
<head>
    <title>Registro Usuario</title>
    <style>
        body {
            background-image: url("images/Registro.png");
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
        .avatars {
            display: flex;
            justify-content: space-around;
            margin: 15px 0;
        }
        .avatar-option {
            cursor: pointer;
            border: 2px solid transparent;
            border-radius: 50%;
            transition: border-color 0.3s;
            width: 70px;
            height: 70px;
        }
        .avatar-option.selected {
            border-color: #4CAF50;
        }
    </style>
    <script>
        function seleccionarAvatar(valor) {
            document.getElementById("avatar").value = valor;

            let opciones = document.getElementsByClassName("avatar-option");
            for (let i = 0; i < opciones.length; i++) {
                opciones[i].classList.remove("selected");
            }

            let imagen = document.querySelector("img[data-valor='" + valor + "']");
            if (imagen) imagen.classList.add("selected");
        }
    </script>
</head>
<body>
<div class="contenido">
    <h2>Registrar nuevo usuario</h2>
    <form action="registro" method="post">
        Nombre: <input type="text" name="nombre" required /><br/>
        Correo: <input type="email" name="correo" required /><br/>
        Contrasena: <input type="password" name="contrasena" required /><br/>

        <p>Selecciona un avatar:</p>
        <div class="avatars">
            <img src="images/icons/icono1.png" class="avatar-option" data-valor="images/icons/icono1.png" onclick="seleccionarAvatar('images/icons/icono1.png')" />
            <img src="images/icons/icono2.png" class="avatar-option" data-valor="images/icons/icono2.png" onclick="seleccionarAvatar('images/icons/icono2.png')" />
            <img src="images/icons/icono3.png" class="avatar-option" data-valor="images/icons/icono3.png" onclick="seleccionarAvatar('images/icons/icono3.png')" />
            <img src="images/icons/icono4.png" class="avatar-option" data-valor="images/icons/icono4.png" onclick="seleccionarAvatar('images/icons/icono4.png')" />
            <img src="images/icons/icono5.png" class="avatar-option" data-valor="images/icons/icono5.png" onclick="seleccionarAvatar('images/icons/icono5.png')" />
            <img src="images/icons/icono6.png" class="avatar-option" data-valor="images/icons/icono6.png" onclick="seleccionarAvatar('images/icons/icono6.png')" />
            <img src="images/icons/icono7.png" class="avatar-option" data-valor="images/icons/icono7.png" onclick="seleccionarAvatar('images/icons/icono7.png')" />
            <img src="images/icons/icono8.png" class="avatar-option" data-valor="images/icons/icono8.png" onclick="seleccionarAvatar('images/icons/icono8.png')" />
            <img src="images/icons/icono9.png" class="avatar-option" data-valor="images/icons/icono9.png" onclick="seleccionarAvatar('images/icons/icono9.png')" />
        </div>
        <input type="hidden" name="avatar" id="avatar" required />

        <input type="submit" value="Registrar"/>
    </form>
    <form action="index.jsp" method="get">
        <button type="submit">Volver al inicio</button>
    </form>
</div>
</body>
</html>
