
import java.awt.Graphics2D;
import javax.swing.JFrame;

public class Game implements Runnable {

    private static Renderer renderer;
    private boolean running = false;
    private Thread th;
    private Handler handler;
    private static Game game;
    private static JFrame frame;
    public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public final String TITLE = "Tanke For Playing";
    
    public void run() {
        init();
        long lastTime = System.nanoTime();
        final double numberOfTicks = 120.0;
        double ns = 1000000000 / numberOfTicks;
        double delta = 0;
        int updates = 0;
        int frames = 0;
        long timer = System.currentTimeMillis();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                tick();
                delta--;
                updates++;
            }
            // ();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("Ticks: " + updates
                        + "      Frames Per Second(FPS): " + frames);
                updates = 0;
                frames = 0;
            }
        }
        stop();
    }

    private void tick() {
        renderer.repaint();
        handler.tick();
    }

    public static enum STATE {
        MENU, GAME, PAUSE, CONTROLS, WIN
    };

    public Game() {
        renderer = new Renderer();
    }

    public void init() {
        handler = new Handler();
    }

    private synchronized void stop() {
        if (!running) {
            return;
        }
        running = false;
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(1);
    }

    private synchronized void start() {
        // If the program is already running then do nothing but if not running,
        // make it run and start the thread
        if (running) {
            return;
        }
        running = true;
        th = new Thread(this);
        th.start();
    }

    public static void render(Graphics2D g) {

    }

    public static void main(String[] args) {
        game = new Game();

        frame = new JFrame(game.TITLE);
        // Ads the instance of the game to the JFrame
        // frame.add(game);
        // Causes the window to be at preferred size initially
        frame.setSize(WIDTH, HEIGHT);
        // frame.pack();
        // Exits program on close
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Sets the program so it cannot be re-sizable
        frame.setResizable(false);
        frame.add(renderer);
        frame.setVisible(true);

        game.start();
    }

}
