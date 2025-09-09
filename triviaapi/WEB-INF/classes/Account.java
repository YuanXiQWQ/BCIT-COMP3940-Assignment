import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.*;
public class Account extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("user_id");
		String password = request.getParameter("password");
		HttpSession session = request.getSession(true);
		session.setAttribute("USER_ID", username);	
                System.out.println("Logged in as:" + username);
                response.setStatus(200);	
	}
}
