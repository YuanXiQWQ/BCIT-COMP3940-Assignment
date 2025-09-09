import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.sql.*;
import java.io.*;
public class LoginServlet extends HttpServlet {
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
out.println("<html>\n" + "<head><title>" + "Login" + "</title></head>\n" +
"<script src=\"https://cdn.jsdelivr.net/npm/md5-js-tools@1.0.2/lib/md5.min.js\"></script>" +
"<script src=\"passwordhash.js\"></script>" +
 "<body>\n"
				+ "<h1 align=\"center\">" + "Login" + "</h1>\n" + "<form action=\"login\" method=\"POST\" onsubmit=\"handleLogin()\">\n"
				+ "Username: <input type=\"text\" name=\"user_id\">\n" + "<br />\n"
				+ "Password: <input type=\"password\" name=\"password\" id=\"pswd\"/>\n" + "<br />\n"
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

   public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         System.out.println(">>Enetring Do Post\n\n");
      response.setContentType("text/html");
      String errMsg = "";
      Connection con = null;
      try {
         try { Class.forName("oracle.jdbc.OracleDriver"); } catch (Exception ex) {        System.out.println(">>Driver Exception\n\n"); }
         con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:49732:XE", "system", "oracle1"); //replace 49732 with the output of netstat -a -b for oracle.exe
         Statement stmt2 = con.createStatement();
         ResultSet rs = stmt2.executeQuery("select * from accounts");
        System.out.println(">>Reading Selectt\n\n");
         while (rs.next()) {
            String username = rs.getString("username");
            String password = rs.getString("password");
            System.out.println("   " + username + "  " + password); 
	 }
         stmt2.close();
         con.close();
         System.out.println("\n\n");
      } catch(SQLException ex) { 
         errMsg = errMsg + "\n--- SQLException caught ---\n"; 
         while (ex != null) { 
            errMsg += "Message: " + ex.getMessage (); 
            errMsg += "SQLState: " + ex.getSQLState (); 
            errMsg += "ErrorCode: " + ex.getErrorCode (); 
            ex = ex.getNextException(); 
            errMsg += "";
         } 
System.out.println(errMsg);

      } 
    PrintWriter out = response.getWriter();
      response.setContentType("text/html");


		String title = "Logged in as: ";
		String username = request.getParameter("user_id");
		String password = request.getParameter("password");
		HttpSession session = request.getSession(true);
		session.setAttribute("USER_ID", username);
		response.setStatus(302);
		response.sendRedirect("main");		
	}
}
