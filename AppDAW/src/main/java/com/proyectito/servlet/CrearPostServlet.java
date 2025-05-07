package com.proyectito.servlet;

import com.proyectito.dao.PostDAO;
import com.proyectito.model.Post;
import com.proyectito.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

@WebServlet("/crearPost")
public class CrearPostServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int idThread = Integer.parseInt(request.getParameter("id_thread"));
        String contenido = request.getParameter("post_content");
        HttpSession session = request.getSession();

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            response.sendRedirect("index.jsp?error=debes_iniciar_sesion");
            return;
        }
        int idUsuario = usuario.getIdUsuario();

        Post post = new Post();
        post.setIdThread(idThread);
        post.setIdUsuario(idUsuario);
        post.setPostContent(contenido);
        post.setPostCreatedAt(LocalDateTime.now());

        try {
            new PostDAO().insertarPost(post);
            response.sendRedirect("verPosts?id_thread=" + idThread);
        } catch (SQLException e) {
            throw new ServletException("Error al crear post", e);
        }
    }
}
