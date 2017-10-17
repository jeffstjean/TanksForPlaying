
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;

public class Game implements Runnable, KeyListener, MouseInputListener {

    private static Renderer renderer;
    private boolean running = false;
    private Thread th;
    private static Handler handler;
    private static Game game;
    private static JFrame frame;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public final String TITLE = "Tanks For Playing";
    private Tank tank;
    private Bullet bullet;
    private static Controller controller;
    public HashMap<Integer, Key> keyBindings = new HashMap<Integer, Key>();
    private Turret turret;
    public static boolean other[] = new boolean[256];
    private static int mouseX, mouseY;
    private final int MOUSECLICKTYPE = 0; // 0 = pressed, 1 = released, 2 = clicked

    public void run() {
        init();
        long lastTime = System.nanoTime();
        final double numberOfTicks = 60.0;
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

    @Override
    public void keyTyped(KeyEvent ke) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        other[ke.getExtendedKeyCode()] = true;
        keyBindings.get(ke.getKeyCode()).isDown = true;
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        other[ke.getExtendedKeyCode()] = false;
        keyBindings.get(ke.getKeyCode()).isDown = false;
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        if(MOUSECLICKTYPE == 2) {
            bullet.shoot();
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
        if(MOUSECLICKTYPE == 0) {
            bullet.shoot();
        }
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        if(MOUSECLICKTYPE == 1) {
            bullet.shoot();
        }
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        
    }

    @Override
    public void mouseExited(MouseEvent me) {
        
    }

    @Override
    public void mouseDragged(MouseEvent me) {

    }

    @Override
    public void mouseMoved(MouseEvent me) {
        mouseX= me.getX();
        mouseY= me.getY();

    }

    public static enum STATE {
        MENU, GAME, PAUSE, CONTROLS, WIN
    };

    public Game() {
        renderer = new Renderer();
    }

    public void init() {
        bind(KeyEvent.VK_W, Key.up);
        bind(KeyEvent.VK_A, Key.left);
        bind(KeyEvent.VK_S, Key.down);
        bind(KeyEvent.VK_D, Key.right);
        handler = new Handler();
        tank = new Tank(100, 100, ID.Tank, this);
        turret = new Turret(tank.getX(), tank.getY(), ID.Turret, tank);
        bullet = new Bullet(200, 200, ID.Bullet, this);
        handler.addObject(tank);
        handler.addObject(turret);
        handler.addObject(bullet);

    }

    public static int getMouseX() {
        return mouseX;
    }

    public static int getMouseY() {
        return mouseY;
    }

    private synchronized void stop() {
        if (!running)
            return;
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
        if (running)
            return;
        running = true;
        th = new Thread(this);
        th.start();
    }

    public static void render(Graphics2D g) {
        handler.render(g);
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
        frame.addKeyListener(game);
        frame.addMouseMotionListener(game);
        frame.addMouseListener(game);
    }

    public void bind(Integer keyCode, Key key) {
        keyBindings.put(keyCode, key);
    }

    public void releaseAll() {
        for (Key key : keyBindings.values()) {
            key.isDown = false;
        }
    }

}
