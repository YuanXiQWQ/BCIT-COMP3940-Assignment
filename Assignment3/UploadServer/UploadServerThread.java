import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class UploadServerThread extends Thread {
    private Socket socket = null;

    public UploadServerThread(Socket socket)
    {
        super("DirServerThread");
        this.socket = socket;
    }

    @Override
    public void run()
    {
        try
        {
            InputStream rawIn = socket.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(rawIn);
            InputStreamReader isr =
                    new InputStreamReader(bis, StandardCharsets.ISO_8859_1);
            BufferedReader reader = new BufferedReader(isr);

            String requestLine = reader.readLine();
            if(requestLine == null || requestLine.isEmpty())
            {
                socket.close();
                return;
            }

            String[] parts = requestLine.split(" ");
            String method = parts.length > 0
                            ? parts[0]
                            : "";
            String uri = parts.length > 1
                         ? parts[1]
                         : "/";

            Map<String, String> headers = new LinkedHashMap<>();
            String line;
            while((line = reader.readLine()) != null && line.length() > 0)
            {
                int idx = line.indexOf(':');
                if(idx > 0)
                {
                    String k = line.substring(0, idx).trim();
                    String v = line.substring(idx + 1).trim();
                    headers.put(k, v);
                }
            }

            int contentLength = 0;
            String cl = headers.get("Content-Length");
            if(cl == null)
            {
                cl = headers.get("content-length");
            }
            if(cl != null)
            {
                try
                {
                    contentLength = Integer.parseInt(cl);
                } catch(NumberFormatException ignored)
                {
                }
            }

            byte[] body = new byte[contentLength];
            int off = 0;
            while(off < contentLength)
            {
                int n = bis.read(body, off, contentLength - off);
                if(n < 0)
                {
                    break;
                }
                off += n;
            }
            ByteArrayInputStream bodyIn = new ByteArrayInputStream(body);

            HttpServletRequest req = new HttpServletRequest(method, uri, headers, bodyIn);
            ByteArrayOutputStream bodyOut = new ByteArrayOutputStream();
            HttpServletResponse res = new HttpServletResponse(bodyOut);

            HttpServlet httpServlet = new UploadServlet();
            if("GET".equalsIgnoreCase(method))
            {
                httpServlet.doGet(req, res);
            } else
            {
                httpServlet.doPost(req, res);
            }

            byte[] responseBody = bodyOut.toByteArray();
            String status = "HTTP/1.1 200 OK\r\n";
            String contentType = "text/html; charset=UTF-8";
            String respHeaders =
                    "Content-Type: " + contentType + "\r\n" + "Content-Length: " +
                            responseBody.length + "\r\n" + "Connection: close\r\n\r\n";

            OutputStream out = socket.getOutputStream();
            out.write(status.getBytes(StandardCharsets.ISO_8859_1));
            out.write(respHeaders.getBytes(StandardCharsets.ISO_8859_1));
            out.write(responseBody);
            out.flush();
            socket.close();
        } catch(Exception e)
        {
            e.printStackTrace();
        } finally
        {
            UploadServer.CONNECTION_SEM.release();
        }
    }
}
