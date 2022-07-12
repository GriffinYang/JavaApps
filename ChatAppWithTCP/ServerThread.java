package ChatApp;

import java.io.*;
import java.net.Socket;

public class ServerThread extends MainServer implements Runnable {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private boolean loop=true;
    public ServerThread(Socket socket) {
    this.socket=socket;
    }

    @Override
    public void run() {
        try {
            reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Client@"+socket.getRemoteSocketAddress()+" has joined the chat!");
            sendToClients("Client@"+socket.getRemoteSocketAddress()+" has joined the chat!");
           while(loop){
               String message=reader.readLine();
               if(message==null){
                   loop=false;
                   continue;
               }
               System.out.println("Client@"+socket.getRemoteSocketAddress()+":"+message);
               sendToClients(message);
           }
            closeChat();
        } catch (IOException e) {
            try {
                closeChat();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    private void closeChat() throws IOException {
        synchronized (sockets){
            sockets.remove(socket);
        }
        System.out.println("Client@"+socket.getRemoteSocketAddress()+" has left the chatroom!");
        sendToClients("Client@"+socket.getRemoteSocketAddress()+" has left the chatroom!");
        socket.close();
    }

    private void sendToClients(String message) throws IOException {
       synchronized (sockets){
           for (Socket socket : sockets){
               writer=new PrintWriter(socket.getOutputStream());
               writer.println("Client@"+socket.getRemoteSocketAddress()+":"+message);
               writer.flush();
           }
       }
    }
}
