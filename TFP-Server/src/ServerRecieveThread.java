
import java.io.*;
import java.net.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ServerRecieveThread extends Thread {

    protected DatagramSocket socket1 = null;
    protected BufferedReader in = null;
    protected boolean moreQuotes = true;
    private InetAddress[] address;
    private int[] port;
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

        Logger logger = Logger.getLogger("ServerLog");
        FileHandler fh;

        try {

            // This block configure the logger with handler and formatter
            File file = new File("./logs/log.txt");
            fh = new FileHandler(file.getPath());
            // fh = new FileHandler("/Users/jeff/Desktop/log.txt");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.setUseParentHandlers(false);
            // the following statement is used to log any messages
            logger.info("Server started.");

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < Game.NUMBER_OF_PLAYERS; i++) {
            try{
            packet1 = new DatagramPacket(buf, buf.length);
            logger.info("Packet made.");

            socket1.receive(packet1);
            logger.info("Recieved 1.");
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

        while (moreQuotes) {
            try {
                buf = null;
                byte[] buf = new byte[256];

                // receive request
                packet1 = new DatagramPacket(buf, buf.length);
                logger.info("Packet made.");

                socket1.receive(packet1);
                Server.game.decodeBytes(packet1);
                logger.info("Recieved 1.");

            } catch (IOException e) {
                e.printStackTrace();
                moreQuotes = false;
            }
        }

        socket1.close();

    }

}
