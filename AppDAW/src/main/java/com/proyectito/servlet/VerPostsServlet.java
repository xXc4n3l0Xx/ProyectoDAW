package com.proyectito.servlet;

import com.proyectito.dao.PostDAO;
import com.proyectito.model.Post;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/verPosts")
public class VerPostsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int idThread = Integer.parseInt(request.getParameter("id_thread"));

        PostDAO postDAO = new PostDAO();
        try {
            List<Post> posts = postDAO.obtenerPostsPorThread(idThread);
            request.setAttribute("posts", posts);
            request.setAttribute("id_thread", idThread);
            request.getRequestDispatcher("ver_posts.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error al obtener los posts", e);
        }
    }
}
