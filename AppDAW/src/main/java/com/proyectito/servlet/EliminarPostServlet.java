package com.proyectito.servlet;

import com.proyectito.dao.PostDAO;
import com.proyectito.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/eliminarPost")
public class EliminarPostServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int idPost = Integer.parseInt(request.getParameter("id_post"));
        int idThread = Integer.parseInt(request.getParameter("id_thread"));
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("index.jsp?error=debes_iniciar_sesion");
            return;
        }

        try {
            PostDAO postDAO = new PostDAO();
            postDAO.eliminarPost(idPost, usuario.getIdUsuario());
            response.sendRedirect("verPosts?id_thread=" + idThread);
        } catch (SQLException e) {
            throw new ServletException("Error al eliminar el post", e);
        }
    }
}
