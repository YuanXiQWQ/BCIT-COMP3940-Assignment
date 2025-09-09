import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.time.LocalDate;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.StringBuilder;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
@MultipartConfig
public class Trivia extends HttpServlet {
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
      HttpSession session = request.getSession(false);
      boolean isLoggedIn = isLoggedIn(request);
      if (!isLoggedIn) {
         response.setStatus(302);
         response.sendRedirect("login");
      } else {
         response.setContentType("text/plain");
         response.setCharacterEncoding("UTF-8");
         //Use the path info and parameter map to find out the details of the get request
         System.out.println(request.getPathInfo());
         System.out.println(request.getParameterMap());
         //use the resource Values to find out the details of the request and respond accordingly
         File dir = new File("c:\\tomcat\\webapps\\triviaapi\\images");
         String[] fileList = dir.list();
         //use jason-io or Gson as opposed to buildng your own json array.
         if (fileList != null && fileList.length > 0) {
            String jsonArray = "[";
            for (int i  = 0; i < fileList.length; i++) {
               jsonArray += fileList[i];
               jsonArray += ",";
            }
            jsonArray += "]";
            PrintWriter out = response.getWriter();  
            out.println(jsonArray); 
         }
         response.setStatus(200);   
      }
   }
   protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         //Use the path info and parameter map to find out the details of the PUT request
         System.out.println(request.getPathInfo());
         System.out.println(request.getParameterMap());
         response.setStatus(200);
   }
   protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         //Use the path info and parameter map to find out the details of the DELETE request
         System.out.println(request.getPathInfo());
         System.out.println(request.getParameterMap());
         response.setStatus(200);
   }
    
   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
      System.out.println("In Do Post");
      Part filePart = request.getPart("fileName");
      String captionName = request.getParameter("caption");
      String formDate = request.getParameter("date");
      String fileName = filePart.getSubmittedFileName();

      if(fileName.equals("")){
         response.setStatus(302);
         response.sendRedirect("upload");
         return;
      }
      if(formDate.equals("")) formDate = "2020-10-10";
      if(captionName.equals("")) captionName = "No caption"; 
      filePart.write(System.getProperty("catalina.base") + "/webapps/triviaapi/images/" + fileName);
      response.setStatus(200);
   } 
   private boolean isLoggedIn(HttpServletRequest req) {
      HttpSession session = req.getSession(false);
      if (session == null || !req.isRequestedSessionIdValid()) {
         return false;
      }else{
         return true;
      }
   }   
    
}


