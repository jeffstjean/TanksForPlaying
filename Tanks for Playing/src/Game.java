
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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
    public HashMap<Integer, Key> keyBindings = new HashMap<Integer, Key>();
    private Turret turret;
    public static boolean other[] = new boolean[256];
    private static int mouseX, mouseY;
    private final int MOUSECLICKTYPE = 0; // 0 = pressed, 1 = released, 2 = clicked
    private LinkedList<Wall> walls;
    
    
    @Override
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
        renderer.repaint(); // tells renderer to repaint if it hasn't already
        handler.tick(); // tells handler to tick all game objects
    }

    @Override
    public void keyTyped(KeyEvent ke) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        other[ke.getExtendedKeyCode()] = true;
        keyBindings.get(ke.getKeyCode()).isDown = true;
        //updates the key bindings
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        other[ke.getExtendedKeyCode()] = false;
        keyBindings.get(ke.getKeyCode()).isDown = false;
        //updates the key bindings
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        if (MOUSECLICKTYPE == 2) {

        }
        
        mouseX = me.getX();
        mouseY = me.getY();
        // gets the mouse's x and y location
    }

    @Override
    public void mousePressed(MouseEvent me) {
        //if(MOUSECLICKTYPE == 0) {
        Key.shoot.isDown = true;
        //sets the key shoot to be down when clicked
        //}
        mouseX = me.getX();
        mouseY = me.getY();
        //get the x and y location of the mouse for aiming
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        // if(MOUSECLICKTYPE == 1) {
        Key.shoot.isDown = false;
        //sets the key binding of shoot to up 
        // }
        mouseX = me.getX();
        mouseY = me.getY();
        //gets the x and y location of the mouse
    }
    

    @Override
    public void mouseEntered(MouseEvent me) {
        mouseX = me.getX();
        mouseY = me.getY();
        //gets the x and y location of the mouse
    }

    @Override
    public void mouseExited(MouseEvent me) {
        mouseX = me.getX();
        mouseY = me.getY();
        // gets the x and y location of the mouse
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        mouseX = me.getX();
        mouseY = me.getY();
        // gets the x and y location of the mouse
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        mouseX = me.getX();
        mouseY = me.getY();
//gets the x and y location of the mouse
    }

    public static enum STATE {
        MENU, GAME, PAUSE, CONTROLS, WIN
    };

    public Game() {
        renderer = new Renderer();
        // initiallizes the renderer
    }

    public void init() {
        bind(KeyEvent.VK_W, Key.up);
        bind(KeyEvent.VK_A, Key.left);
        bind(KeyEvent.VK_S, Key.down);
        bind(KeyEvent.VK_D, Key.right);
        walls = new LinkedList<Wall>();
        // sets the keybindings
        handler = new Handler();
        tank = new Tank(100, 100, 64, 64, ID.Tank, this);
        // inits tank at 100 100 and gives it the game instance
        turret = new Turret(tank.getX(), tank.getY(), 10,10,ID.Turret, tank);
        // creates a turret for the tank
        walls.add(new Wall(10, 100, 20, 500, ID.Wall));
        
        for (int i = 0; i < walls.size(); i++) {
            handler.addObject(walls.get(i));
        }
        
        handler.addObject(tank);
        handler.addObject(turret);
// adds the two objects to the handler
    }

    public static int getMouseX() {
        return mouseX;
    }

    public static int getMouseY() {
        return mouseY;
    }

    private final synchronized void stop() {
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

    private final synchronized void start() {
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
        handler.render(g);
        // has handler render all gameObjects
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
        // adds the renderer to the jFrame
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

    public static Handler getHandler() {
        return handler;
    }

}
