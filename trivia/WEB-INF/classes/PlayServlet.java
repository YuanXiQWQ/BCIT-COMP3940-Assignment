import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.sql.*;
import java.io.*;

public class PlayServlet extends HttpServlet {
    private static final String JDBC_URL =
            "jdbc:mysql://localhost:3306/comp3940assignment1?useSSL=false" +
                    "&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String JDBC_USER = "test";
    private static final String JDBC_PASS = "123123";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException
    {
        String errMsg = "";
        String nav = request.getParameter("nav");

        HttpSession session = request.getSession(true);
        Integer idxObj = (Integer) session.getAttribute("VIDEO_IDX");
        int idx = (idxObj == null)
                  ? 0
                  : idxObj;

        String contentPath = "tgbNymZ7vqY";

        Connection con;
        try
        {
            try
            {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch(Exception ex)
            {
            }
            con = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);

            int total = 0;
            Statement stmtCount = con.createStatement();
            ResultSet rsc = stmtCount.executeQuery("SELECT COUNT(*) FROM videos");
            if(rsc.next())
            {
                total = rsc.getInt(1);
            }
            rsc.close();
            stmtCount.close();

            if(total > 0)
            {
                if("next".equals(nav))
                {
                    idx = (idx + 1) % total;
                } else if("prev".equals(nav))
                {
                    idx = (idx - 1 + total) % total;
                }
                session.setAttribute("VIDEO_IDX", idx);

                String sql = "SELECT url FROM videos ORDER BY id LIMIT ?,1";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, idx);
                ResultSet rs = ps.executeQuery();
                if(rs.next())
                {
                    contentPath = rs.getString("url");
                }
                rs.close();
                ps.close();
            }
            con.close();
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
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println(
                "<!DOCTYPE html>" +
                        "<meta charset='UTF-8'>" +
                        "<body>" +
                        "<div>" +
                        "<iframe id=\"Video\" width=\"420\" height=\"345\" " +
                        "src=https://www.youtube.com/embed/" +
                        contentPath + "?autoplay=1&mute=1&start=62&end=162>" +
                        "</iframe>" +
                        "</div>" +
                        "<div>" +
                        "<form action='/trivia/play' method='GET'>" +
                        "<br>" +
                        "<div class='button'>" +
                        "<button class='button' id='prev' name='nav' value='prev'>Prev</button>" +
                        "<button class='button' id='next' name='nav' value='next'>Next</button>" +
                        "</div>" +
                        "<br>" +
                        "</form>" +
                        "<div>" +
                        "<form action='main' method='GET'>" +
                        "<button class='button' id='main'>Main</button>" +
                        "</form>" +
                        "</div>" +
                        "<br>" +
                        "</body>" +
                        "</html>"
        );
    }
}