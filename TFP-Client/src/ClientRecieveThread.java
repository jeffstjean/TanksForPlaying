
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;



public class ClientRecieveThread extends Thread{
    private DatagramSocket socket;
    private String  host = "35.202.196.54";//jeff 99.233.204.138 owen 192.168.0.10
   private InetAddress address;
   private DatagramPacket packet;
   private int PORT = 4448;
    private Game game;
    private byte[] buf = new byte[256];
    public ClientRecieveThread(Game g){
        game = g;
        
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPORT(int PORT) {
        this.PORT = PORT;
    }
    
    @Override
    public void run(){
            
        try{
            socket = new DatagramSocket();
            address = InetAddress.getByName(host);
            packet = new DatagramPacket(game.getAllBytes(), game.getAllBytes().length, address, PORT);
        }catch (Exception  e){
            System.out.println("shit server error");
        }
        
        
//        long lastTime = System.nanoTime();
//        double numberOfTicks = 60.0;
//        double ns;
//        double delta = 0;
//        int updates = 0;
//        int frames = 0;
//        long timer = System.currentTimeMillis();
//        long now;
        
        while(true){
                   

//            ns = 1000000000 / numberOfTicks;
//            now = System.nanoTime();
//            delta += (now - lastTime) / ns;
//            lastTime = now;
//            
//            if (delta >= 1) {
//               
                send();
//                delta--;
//                updates++;
//            }
//            frames++;
//
//            if (System.currentTimeMillis() - timer > 1000) {
//                timer += 1000;
//                System.out.println("Thread cRT Ticks: " + updates + "      Frames Per Second(FPS): " + frames);
//                updates = 0;
//                frames = 0;
//            }
        
            
          
       
        try {
            
            
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            
            game.decodeBytes(packet.getData());
        } catch (Exception ex) {
            System.out.println("servererrorspot1");
            ex.printStackTrace();
        }
        
        if(game.maxMillis - System.currentTimeMillis() > 500){
          //  numberOfTicks = 30.0;
        }else{
          //  numberOfTicks = 60.0;
        }
    }
}

    private void send(){
        
        
        try {
            packet = new DatagramPacket(game.getAllBytes(), game.getAllBytes().length, address, PORT);
            socket.send(packet);
            
            
        } catch (IOException ex) {
            System.out.println("servererrorspot2 could not send");
        }
        
        
    }
    
    
}