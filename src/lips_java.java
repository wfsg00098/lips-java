import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class lips_java {
    static int count = 0;

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(10404);
            System.out.println("Listening on port 10404...");
            while (true) {
                Socket io = ss.accept();
                new Connection(io).start();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}

class Connection extends Thread {
    private final String FileAddress = "/var/www/lips/user_upload";
    private Socket socket;

    Connection(Socket io) {
        socket = io;
    }

    @Override
    public void run() {
        int number = ++lips_java.count;
        String now = Integer.toString(number) + "_" + (new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()));
        try {
            InputStream in = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String username = br.readLine();
            System.out.println(Integer.toString(number) + ": User:" + username + " connected on " + now + " from " + socket.getInetAddress().getHostAddress());
            now = username + now;
            OutputStream out = socket.getOutputStream();
            FileOutputStream fos = new FileOutputStream(FileAddress + now);
            byte[] temp = new byte[1024];
            int len;
            while ((len = in.read(temp)) != -1) {
                fos.write(temp, 0, len);
                fos.flush();
            }
            fos.close();
            out.write(("https://lips.guaiqihen.com/user_upload/" + now).getBytes());
            out.flush();
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
            System.out.println(Integer.toString(number) + ": User:" + username + " disconnected");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}

