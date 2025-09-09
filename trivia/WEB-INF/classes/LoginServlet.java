import jakarta.servlet.http.*;
import jakarta.servlet.*;

import java.sql.*;
import java.io.*;

public class LoginServlet extends HttpServlet {
    private static final String JDBC_URL =
            "jdbc:mysql://localhost:3306/testdb?useSSL=false&serverTimezone=UTC";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "your_password";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>\n" + "<head><title>" + "Login" + "</title></head>\n" +
                "<script src=\"https://cdn.jsdelivr.net/npm/md5-js-tools@1.0.2/lib/md5" +
                ".min.js\"></script>" +
                "<script src=\"passwordhash.js\"></script>" +
                "<body>\n"
                + "<h1 align=\"center\">" + "Login" + "</h1>\n" +
                "<form action=\"login\" method=\"POST\" onsubmit=\"handleLogin()\">\n"
                + "Username: <input type=\"text\" name=\"user_id\">\n" + "<br />\n"
                + "Password: <input type=\"password\" name=\"password\" id=\"pswd\"/>\n" +
                "<br />\n"
                + "<input type=\"submit\" value=\"Sign in\"  />\n" + "</form>\n"
                + "</form>\n" +
                "<script>" +
                "function handleLogin() {" +
                "var password = document.getElementById(\"pswd\").value;" +
                "alert(password);" +
                "var hash = MD5.generate(password);" +
                "alert(hash);" +
                "document.getElementById(\"pswd\").value = hash;" +
                "}" +
                "</script>" +
                "</body>\n</html\n"
        );
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("user_id");
        String password = request.getParameter("password");

        boolean ok = false;
        try(Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT 1 FROM accounts WHERE username=? AND password=?"))
        {
            ps.setString(1, username);
            ps.setString(2, password);
            try(ResultSet rs = ps.executeQuery())
            {
                ok = rs.next();
            }
        } catch(SQLException e)
        {
            ok = false;
        }

        if(ok)
        {
            HttpSession session = request.getSession(true);
            session.setAttribute("USER_ID", username);
            response.sendRedirect("main");
        } else
        {
            response.sendRedirect("login?err=1");
        }
    }
}
