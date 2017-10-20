

import java.io.*;
import java.net.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ServerThreadForTanks extends Thread {

    protected DatagramSocket socket1 = null, socket2 = null;
    protected BufferedReader in = null;
    protected boolean moreQuotes = true;

    public ServerThreadForTanks() throws IOException {
        this("ServerThreadForTanks");
    }

    public ServerThreadForTanks(String name) throws IOException {
        super(name);
        socket1 = new DatagramSocket(4447);
        socket2 = new DatagramSocket(4448);
       
    }

    @Override
    public void run() {
    	
		Logger logger = Logger.getLogger("Server Log");
		FileHandler fh;
		
	    try {  

	        // This block configure the logger with handler and formatter  
	        fh = new FileHandler("logs/log.txt");  
	        //fh = new FileHandler("/Users/jeff/Desktop/log.txt"); 
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  
	        logger.setUseParentHandlers(false);
	        // the following statement is used to log any messages  
	        logger.info("Server created.");  

	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  

        while (moreQuotes) {
            try {
                byte[] buf = new byte[256];
                byte[] buf2 = new byte[256];
                // receive request
				DatagramPacket packet1 = new DatagramPacket(buf, buf.length);
				logger.info("Packet made.");

				socket1.receive(packet1);
				logger.info("Recieved 1.");
				DatagramPacket packet2 = new DatagramPacket(buf2, buf2.length);
				socket2.receive(packet2);
				logger.info("Recieved 2.");
                
                
                
                InetAddress address1, address2;
                int port1, port2;
                
                
                address2 = packet2.getAddress();
                address1 = packet1.getAddress();
                
                
                port2 = packet2.getPort();
                port1 = packet1.getPort();
                
                packet1.setAddress(address2);
                packet1.setPort(port2);
                
                packet2.setAddress(address1);
                packet2.setPort(port1);

                
				socket1.send(packet2);
				logger.info("Sent 1.");
				socket2.send(packet1);
				logger.info("Sent 2.");
           


        }catch (IOException e) {
                e.printStackTrace();
                moreQuotes = false;
            }
    }

    socket1.close ();

    socket2.close ();
}
}
