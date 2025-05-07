package com.proyectito.servlet;

import com.proyectito.dao.ForumThreadDAO;
import com.proyectito.model.ForumThread;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/foro")
public class ForoServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ForumThreadDAO threadDAO = new ForumThreadDAO();
        try {
            List<ForumThread> hilos = threadDAO.obtenerTodosLosHilos();
            request.setAttribute("hilos", hilos);
            request.getRequestDispatcher("foro.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error al obtener los hilos", e);
        }
    }
}
