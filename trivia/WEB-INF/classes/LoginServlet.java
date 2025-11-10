import jakarta.servlet.http.*;
import jakarta.servlet.*;

import java.sql.*;
import java.io.*;

public class LoginServlet extends HttpServlet {
    private static final String JDBC_URL =
            "jdbc:mysql://localhost:3306/comp3940assignment1?useSSL=false" +
                    "&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String JDBC_USER = "test";
    private static final String JDBC_PASS = "123123";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException
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
                + "<input type=\"submit\" value=\"Sign in\"  />\n" + "</form>\n" +
                "<script>" +
                "function handleLogin() {" +
                "var password = document.getElementById(\"pswd\").value;" +
                "alert(password);" +
                "var hash = MD5.generate(password);" +
                "alert(hash);" +
                "document.getElementById(\"pswd\").value = hash;" +
                "}" +
                "</script>" +
                "</body>\n</html>\n"
        );
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException
    {
        System.out.println(">>Enetring Do Post\n\n");
        response.setContentType("text/html");
        String errMsg = "";
        Connection con;


        String userId = request.getParameter("user_id");
        String plainOrHashedPwd = request.getParameter("password");

        boolean authOk = false;

        try
        {
            try
            {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch(Exception ex)
            {
                System.out.println(">>Driver Exception\n\n");
            }
            con = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);

            System.out.println("user_id=" + userId + ", pwd=" + plainOrHashedPwd);
            String sql = "SELECT 1 FROM accounts WHERE userid = ? AND password = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, userId);
            ps.setString(2, plainOrHashedPwd);

            ResultSet rs = ps.executeQuery();
            authOk = rs.next();
            rs.close();
            ps.close();
            con.close();
            System.out.println("\n\n");
        } catch(SQLException ex)
        {
            errMsg = errMsg + "\n--- SQLException caught ---\n";
            while(ex != null)
            {
                errMsg += "Message: " + ex.getMessage();
                errMsg += "SQLState: " + ex.getSQLState();
                errMsg += "ErrorCode: " + ex.getErrorCode();
                ex = ex.getNextException();
                errMsg += "";
            }
            System.out.println(errMsg);

        }

        if(authOk)
        {
            HttpSession session = request.getSession(true);
            session.setAttribute("USER_ID", userId);
            response.sendRedirect("main");
        } else
        {
            response.sendRedirect("login?err=1");
        }
    }
}
