<!DOCTYPE html>
<html>
<head><title>Iniciar Sesión</title></head>
<body>
<h2>Iniciar sesión</h2>
<form action="login" method="post">
    Correo: <input type="email" name="correo" required /><br/>
    Contraseña: <input type="password" name="contrasena" required /><br/>
    <input type="submit" value="Ingresar" />
</form>

<c:if test="${param.error == '1'}">
    <p style="color:red;">Correo o contraseña incorrectos</p>
</c:if>
</body>
</html>
