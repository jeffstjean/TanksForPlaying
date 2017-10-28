
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
            try{
            packet1 = new DatagramPacket(buf, buf.length);
            Server.logger.info("Packet made original.");

            socket1.receive(packet1);
            Server.logger.info("Recieved 1 original.");
            buf = null;
            buf = new byte[256];
            Server.game.portMap.put(packet1.getAddress(), i);
            port[i] = packet1.getPort();
            address[i] = packet1.getAddress();
            Server.game.decodeBytes(packet1);
            }catch(Exception e){
                e.printStackTrace();
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
                Server.logger.info("Packet made.");

                socket1.receive(packet1);
                Server.game.decodeBytes(packet1);
                Server.logger.info("Recieved 1.");

            } catch (IOException e) {
                e.printStackTrace();
                moreQuotes = false;
            }
        }

        socket1.close();

    }

}
