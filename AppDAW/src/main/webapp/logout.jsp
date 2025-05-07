<%
    HttpSession sesion = request.getSession(false);
    if (sesion != null) {
        sesion.invalidate();
    }
    response.sendRedirect("index.jsp");
%>
