
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class ClientRecieveThread extends Thread{
    private DatagramSocket socket;
    private String [] host = {"192.168.0.15"};//jeff 99.233.204.138 owen 192.168.0.10
   private InetAddress address;
   private DatagramPacket packet;
   private final int PORT = 4447;
    private Game game;
    private byte[] buf = new byte[256];
    public ClientRecieveThread(Game g){
        game = g;
        try{
            socket = new DatagramSocket();
            address = InetAddress.getByName(host[0]);
            packet = new DatagramPacket(game.getAllBytes(), game.getAllBytes().length, address, PORT);
        }catch (Exception e){
            System.out.println("shit server error");
        }
    }
    
    @Override
    public void run(){
        while(true){
        packet = new DatagramPacket(game.getAllBytes(), game.getAllBytes().length, address, PORT);
        try {
            
            socket.send(packet);
            System.out.println("sent");
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            System.out.println("recived");
            game.decodeBytes(packet.getData());
        } catch (IOException ex) {
            System.out.println("servererrorspot1");
        }
    }
}

}