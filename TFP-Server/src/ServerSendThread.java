
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.BufferOverflowException;
import java.util.logging.Level;


public class ServerSendThread extends Thread{
    
    private InetAddress[] address;
    private int[] port;
    protected DatagramSocket socket1 = null;
    protected boolean moreQuotes = true;

    private DatagramPacket packet1;
    public ServerSendThread(DatagramSocket socket, InetAddress[] address, int[] port) throws IOException{
        super("ServerSendThread");
        
        this.address = address;
        
        this.port = port;
        
        socket1 = socket;
        
        
        
    }

    @Override
    public void run() {
        while(true){
            for (int i = 0; i < Game.NUMBER_OF_PLAYERS; i++) {
                try { 
               byte[] temp = Server.game.createBytes(i);
               packet1 = new DatagramPacket(temp, temp.length, address[i], port[i]);
                
                    socket1.send(packet1);
                    
                } catch (IOException ex) {
                    Server.logger.log(Level.INFO, "Could not send package to player{0}", i);
                    
                }catch(BufferOverflowException e){
                    Server.logger.info("had a buffer error");
                    System.out.println("buffer error");
                }
            }
            
        }
    }
    
    
    
}
