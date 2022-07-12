package ChatApp;

import java.io.*;
import java.net.Socket;

public class ClientAlpha {
    public static void main(String[] TXT) {
        Socket socket = null;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        boolean flag = true;
        PrintWriter writer = null;
        BufferedReader reader = null;
        try {
            socket = new Socket("localhost", 9412);
            while (flag) {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader finalReader = reader;
                new Thread(() -> {
                    while (true) {
                        try {
                            System.out.println(finalReader.readLine());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();

                String message = input.readLine();
                while (true) {
                    System.out.println("Sent:" + message);
                    writer.println(message);
                    writer.flush();
                    message = input.readLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                input.close();
                reader.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
/*
 * This is the client side of the chat application.
 * It connects to the server and sends messages to it.
 * It also receives messages from the server and prints them.
 * The client can also send a message to the server by typing it in the console.
 * And there are another two clients beta and sigma with the same code in order
 * to imitate the real chat environment.
 */