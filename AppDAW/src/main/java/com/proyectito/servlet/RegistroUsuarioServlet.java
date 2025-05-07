package com.proyectito.servlet;

import com.proyectito.dao.UsuarioDAO;
import com.proyectito.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/registro")
public class RegistroUsuarioServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getParameter("nombre"));
        usuario.setCorreo(request.getParameter("correo"));
        usuario.setContrasena(request.getParameter("contrasena"));
        usuario.setAvatar(request.getParameter("avatar"));
        usuario.setFechaRegistro(new Timestamp(System.currentTimeMillis()));

        UsuarioDAO dao = new UsuarioDAO();
        try {
            dao.insertarUsuario(usuario);
            response.sendRedirect("index.jsp");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
