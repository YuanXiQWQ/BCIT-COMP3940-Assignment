import jakarta.servlet.http.*;
import jakarta.servlet.*;

import java.io.*;

public class MainServlet extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException
    {
        HttpSession session = request.getSession(false);
        if(session == null)
        {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");
        if(action != null)
        {
            switch(action)
            {
                case "UPLOAD":
                    response.sendRedirect("upload");
                    return;
                case "PLAY GALLERY":
                    response.sendRedirect("play");
                    return;
                case "LOGOUT":
                    response.sendRedirect("logout");
                    return;
            }
        }

        String title = "Logged in as: ";
        title += session.getAttribute("USER_ID");
        response.setContentType("text/html");
        String docType =
                "<!doctype html public \"-//w3c//dtd html 4.0 transitional//en\">\n";
        String html = docType + "<html>\n"
                + "<head><title>" + title + "</title></head>\n"
                + "<body bgcolor=\"#f0f0f0\">\n"
                + "<h1 align=\"center\">" + title + "</h1>\n"
                + "<div style=\"text-align: center;\">\n"
                + "  <form action=\"main\" method=\"GET\">\n"
                + "    <input type=\"submit\" name=\"action\" value=\"UPLOAD\" />\n"
                + "    <input type=\"submit\" name=\"action\" value=\"PLAY GALLERY\" />\n"
                + "    <input type=\"submit\" name=\"action\" value=\"LOGOUT\" />\n"
                + "  </form>\n"
                + "</div>\n"
                + "</body></html>";

        PrintWriter out = response.getWriter();
        out.println(html);
    }
}
