
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServerSendThread extends Thread{
    
    private InetAddress[] address;
    private int[] port;
    protected DatagramSocket socket1 = null;
    protected boolean moreQuotes = true;

    private DatagramPacket packet1;
    public ServerSendThread(DatagramSocket socket, InetAddress[] address, int[] port) throws IOException{
        super("ServerSendThread");
        Server.logger.info("in serverSendThread");
        this.address = address;
        
        this.port = port;
        Server.logger.info("made duplicates");
        socket1 = socket;
        Server.logger.info("Made new DatagramSocket");
        
        
    }

    @Override
    public void run() {
        while(true){
            for (int i = 0; i < Game.NUMBER_OF_PLAYERS; i++) {
                try { 
               byte[] temp = Server.game.createBytes(i);
               packet1 = new DatagramPacket(temp, temp.length, address[i], port[i]);
                Server.logger.info("made packet");
                    socket1.send(packet1);
                    
                } catch (Exception ex) {
                    Server.logger.info("Could not send package to player" + i);
                    ex.printStackTrace();
                }
            }
            
        }
    }
    
    
    
}
