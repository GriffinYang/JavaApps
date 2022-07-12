package ChatApp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

public class MainServer extends Thread{
    protected static List<Socket> sockets=new Vector<>();
    private Socket socket=null;
    private ServerSocket serverSocket=null;
    boolean flag=true;
    @Override
    public void run() {
        try {
            serverSocket=new ServerSocket(9412);
            while(flag){
                socket=serverSocket.accept();
                synchronized (sockets){
                    sockets.add(socket);
                }
                new Thread(new ServerThread(socket)).start();
            }
        } catch (IOException e) {
            flag = false;
            e.printStackTrace();
        }finally {
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(serverSocket!=null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void main(String[] TXT) {
        new MainServer().start();
    }
}
