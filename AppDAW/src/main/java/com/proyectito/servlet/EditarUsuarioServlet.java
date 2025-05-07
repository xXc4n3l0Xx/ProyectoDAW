package com.proyectito.servlet;

import com.proyectito.dao.UsuarioDAO;
import com.proyectito.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/editarUsuario")
public class EditarUsuarioServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuario != null) {
            String nombre = request.getParameter("nombre");
            String correo = request.getParameter("correo");
            String contrasena = request.getParameter("contrasena");

            usuario.setNombre(nombre);
            usuario.setCorreo(correo);
            usuario.setContrasena(contrasena);

            UsuarioDAO dao = new UsuarioDAO();
            try {
                dao.actualizarUsuario(usuario);
                session.setAttribute("usuario", usuario);
                response.sendRedirect("index.jsp?actualizado=1");
            } catch (Exception e) {
                throw new ServletException(e);
            }
        } else {
            response.sendRedirect("index.jsp");
        }
    }
}
