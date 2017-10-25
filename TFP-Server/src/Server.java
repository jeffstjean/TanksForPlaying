import java.io.*;

public class Server {
    public static Game game;
    public static void main(String[] args) throws IOException {
        game = new Game();
        game.start();
        new ServerRecieveThread().start();
        
    }
}

