package ChatApp;

import java.io.*;
import java.net.Socket;

public class ClientCigma{
    public static void main(String[] TXT) {
        Socket socket = null;
        BufferedReader input=new BufferedReader(new InputStreamReader(System.in));
        boolean flag=true;
        PrintWriter writer=null;
        BufferedReader reader = null;
        try {
            socket=new Socket("localhost",9412);
            while (flag){
                reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader finalReader = reader;
                new Thread(()->{
                    while (true){
                        try {
                            System.out.println(finalReader.readLine());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();

                String message = input.readLine();
                while(true){
                    System.out.println("Sent:"+message);
                    writer.println(message);
                    writer.flush();
                    message=input.readLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
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
