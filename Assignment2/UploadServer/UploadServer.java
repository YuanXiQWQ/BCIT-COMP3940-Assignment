import java.net.*;
import java.io.*;
import java.util.concurrent.Semaphore;

public class UploadServer {
    static final Semaphore CONNECTION_SEM = new Semaphore(3, true);
    public static void main(String[] args) throws IOException
    {
        ServerSocket serverSocket = null;
        try
        {
            serverSocket = new ServerSocket(8999);
        } catch(IOException e)
        {
            System.err.println("Could not listen on port: 8999.");
            System.exit(-1);
        }
        while(true)
        {
            try {
                CONNECTION_SEM.acquire();
                try {
                    Socket socket = serverSocket.accept();
                    new UploadServerThread(socket).start();
                } catch (Exception e) {
                    CONNECTION_SEM.release();
                    throw e;
                }
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
