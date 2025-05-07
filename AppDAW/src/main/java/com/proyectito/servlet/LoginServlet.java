package com.proyectito.servlet;

import com.proyectito.dao.UsuarioDAO;
import com.proyectito.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String correo = request.getParameter("correo");
        String contrasena = request.getParameter("contrasena");

        UsuarioDAO dao = new UsuarioDAO();
        try {
            Usuario usuario = dao.obtenerUsuarioPorCredenciales(correo, contrasena);
            if (usuario != null) {
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);
                response.sendRedirect("index.jsp");
            } else {
                response.sendRedirect("index.jsp?error=1");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
