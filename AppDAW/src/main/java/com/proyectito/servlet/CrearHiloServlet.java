package com.proyectito.servlet;

import com.proyectito.dao.ForumThreadDAO;
import com.proyectito.model.ForumThread;
import com.proyectito.model.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

@WebServlet("/crearHilo")
public class CrearHiloServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String titulo = request.getParameter("thread_title");
        HttpSession session = request.getSession();

        Usuario usuario = (Usuario) session.getAttribute("usuario");

       if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int idUsuario = usuario.getIdUsuario();

        ForumThread hilo = new ForumThread();
        hilo.setIdUsuario(idUsuario);
        hilo.setThreadTitle(titulo);
        hilo.setThreadCreatedAt(LocalDateTime.now());

        try {
            new ForumThreadDAO().insertarHilo(hilo);
            response.sendRedirect("foro");
        } catch (SQLException e) {
            throw new ServletException("Error al crear hilo", e);
        }
    }
}
