import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.*;

public class HitServlet extends HttpServlet {
  private int mCount;
  
  public void doGet(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {
      String message = "Hits: " + ++mCount + "." + request.getHeader("User-Agent");

      response.setContentType("text/plain");
    response.setContentLength(message.length());
    PrintWriter out = response.getWriter();
    out.println(message);
  }
}
