import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.sql.*;
import java.io.*;

public class PlayServlet extends HttpServlet {
    private static final String JDBC_URL =
            "jdbc:mysql://localhost:3306/testdb?useSSL=false&serverTimezone=UTC";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "YOUR_PASSWORD";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html; charset=UTF-8");
        HttpSession session = request.getSession(true);

        // Use session to record current video index (0-based)
        Integer idx = (Integer) session.getAttribute("VID_IDX");
        if(idx == null)
        {
            idx = 0;
        }

        // Move index based on button click
        if(request.getParameter("prev") != null && idx > 0)
        {
            idx--;
        } else if(request.getParameter("next") != null)
        {
            idx++;
        }

        // Get video URL from database
        String videoUrl = null;
        try(Connection con = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS))
        {
            try(PreparedStatement ps = con.prepareStatement(
                    "SELECT url FROM videos ORDER BY id LIMIT 1 OFFSET ?"))
            {
                ps.setInt(1, idx);
                try(ResultSet rs = ps.executeQuery())
                {
                    if(rs.next())
                    {
                        videoUrl = rs.getString(1);
                    }
                }
            }
            if(videoUrl == null)
            {
                idx = 0;
                try(PreparedStatement ps0 = con.prepareStatement(
                        "SELECT url FROM videos ORDER BY id LIMIT 1 OFFSET 0");
                    ResultSet rs0 = ps0.executeQuery())
                {
                    if(rs0.next())
                    {
                        videoUrl = rs0.getString(1);
                    }
                }
            }
        } catch(SQLException e)
        {
            // Never! Gonna! Give! You! Up!
            videoUrl = "https://youtu.be/dQw4w9WgXcQ?si=959xB4npzjgrNRtK";
        }

        session.setAttribute("VID_IDX", idx);

        PrintWriter out = response.getWriter();
        out.println(
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<meta charset='UTF-8'>" +
                        "<body>" +
                        "<div>" +
                        "<iframe id=\"Video\" width=\"420\" height=\"345\" " +
                        "src=" + videoUrl +
                        ">" +
                        "</iframe>" +
                        "</div>" +
                        "<div>" +
                        "<form action='/trivia/play' method='GET'>" +
                        "<br>" +
                        "<div class='button'>" +
                        "<button class='button' id='prev' name='prev'>Prev</button>" +
                        "<button class='button' id='next' name='next'>Next</button>" +
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