import java.io.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Server {
    public static Game game;
    public static Logger logger;
    public static ServerRecieveThread rec;
    
    public static void main(String[] args) throws IOException {
        
        logger = Logger.getLogger("ServerLog");
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
            logger.info("logger started.");

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        game = new Game();
        game.start();
        rec = new ServerRecieveThread();
        rec.start();
        
    }
}

