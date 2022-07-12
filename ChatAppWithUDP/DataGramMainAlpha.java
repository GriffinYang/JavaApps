package DatagramMain;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class DataGramMainAlpha {
    public static void main(String[] TXT) throws IOException {
        new Thread(()->{
            try {
                sentMessage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(DataGramMainAlpha::receive).start();
}
public static void sentMessage() throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    while (true){
        String message = reader.readLine();
        DatagramPacket packet=new DatagramPacket(message.getBytes(StandardCharsets.UTF_8),0,message.length(), InetAddress.getByName("192.168.8.86"),9412);
        new DatagramSocket().send(packet);
    }
}
public static void receive(){
    DatagramPacket receive=new DatagramPacket(new byte[1024],1024);
    DatagramSocket socket= null;
    try {
        socket = new DatagramSocket(9412);
    } catch (SocketException e) {
        throw new RuntimeException(e);
    }
    try {
        while (true){
            socket.receive(receive);
            String message=new String(receive.getData(),0,receive.getLength());
            System.out.println(message);
        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
}
