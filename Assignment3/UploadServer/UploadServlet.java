import java.io.*;
import java.time.Clock;
import java.util.concurrent.Semaphore;

public class UploadServlet extends HttpServlet {
    private static final Semaphore UPLOAD_SEM = new Semaphore(3, true);

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            UPLOAD_SEM.acquire();
            try
            {
                InputStream in = request.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] content = new byte[1];
                int bytesRead = -1;
                while((bytesRead = in.read(content)) != -1)
                {
                    baos.write(content, 0, bytesRead);
                }
                Clock clock = Clock.systemDefaultZone();
                long milliSeconds = clock.millis();
                OutputStream outputStream =
                        new FileOutputStream(new File(String.valueOf(milliSeconds) + ".png"));
                baos.writeTo(outputStream);
                outputStream.close();
                PrintWriter out = new PrintWriter(response.getOutputStream(), true);
                File dir = new File(".");
                String[] chld = dir.list();
                for(int i = 0; i < chld.length; i++)
                {
                    String fileName = chld[i];
                    out.println(fileName + "\n");
                    System.out.println(fileName);
                }
            } finally
            {
                UPLOAD_SEM.release();
            }
        } catch(InterruptedException ie)
        {
            Thread.currentThread().interrupt();
        } catch(Exception ex)
        {
            System.err.println(ex);
        }
    }
}