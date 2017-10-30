
import java.io.*;
import java.net.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ServerRecieveThread extends Thread {

    protected DatagramSocket socket1 = null;
    protected boolean moreQuotes = true;
    public InetAddress[] address;
    public int[] port;
    private DatagramPacket packet1;

    byte[] buf = new byte[256];

    public ServerRecieveThread() throws IOException {
        this("ServerThreadForTanks");
    }

    public ServerRecieveThread(String name) throws IOException {
        super(name);
        socket1 = new DatagramSocket(4447);
        address = new InetAddress[Game.NUMBER_OF_PLAYERS];
        port = new int[Game.NUMBER_OF_PLAYERS];

    }

    @Override
    public void run() {

       
        for (int i = 0; i < Game.NUMBER_OF_PLAYERS; i++) {
            try {

                packet1 = new DatagramPacket(buf, buf.length);
                Server.logger.info("Packet made original.");

                socket1.receive(packet1);
                Server.logger.info("recieved a packet");
                // removed for debug purposes if (!(Server.game.portMap.containsKey(packet1.getAddress()))) {
                    Server.logger.info("Recieved 1 original.");
                    buf = null;
                    buf = new byte[256];
                    Server.game.portMap.put(packet1.getAddress(), i);
                    port[i] = packet1.getPort();
                    address[i] = packet1.getAddress();
                    Server.game.decodeBytes(packet1);
                    Server.logger.log(Level.INFO, "Inet Address {0}is {1}", new Object[]{i, packet1.getAddress()});
                   

               // }
            } catch (Exception e) {
                Server.logger.log(Level.SEVERE, "something went wrong{0}", e.getLocalizedMessage());
                
            }
        } 
        try {
            ServerSendThread sST = new ServerSendThread(socket1, address, port);
            sST.start();
        } catch (IOException ex) {
            Server.logger.info("could not initialize server send thread");
        }

        while (moreQuotes) {
            try {
                buf = null;
                byte[] buf = new byte[256];

                // receive request
                packet1 = new DatagramPacket(buf, buf.length);
                

                socket1.receive(packet1);
                Server.game.decodeBytes(packet1);
                

            } catch (Exception e) {
                Server.logger.log(Level.SEVERE, "something went wrong{0}", e.getLocalizedMessage());
                
                
            }
        }

        socket1.close();

    }

}
