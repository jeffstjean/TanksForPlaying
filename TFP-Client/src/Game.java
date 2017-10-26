
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;

public class Game implements Runnable, KeyListener, MouseInputListener {

    private static Renderer renderer;
    private boolean running = false;
    private Thread th, cRT;
    static Handler handler;
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
    private ByteBuffer bb;
    private byte[] allBytes = new byte[256];
    private int enemyX, enemyY, enemyPointing;
    private double enemyRotate;
    private EnemyTank enemyTank;
    private EnemyTurret enemyTurret;
    private boolean enemyShooting;

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

    public byte[] getAllBytes() {
        return allBytes;
    }

    private void tick() {
        renderer.repaint(); // tells renderer to repaint if it hasn't already
        handler.tick(); // tells handler to tick all game objects
        createBytes();

        enemyTank.setVals(enemyX, enemyY, enemyPointing);
        enemyTurret.setVals(enemyShooting, enemyRotate);

    }

    @Override
    public void keyTyped(KeyEvent ke) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        other[ke.getExtendedKeyCode()] = true;
        try {
            keyBindings.get(ke.getKeyCode()).isDown = true;
        } catch (Exception e) {

        }
        //updates the key bindings
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        other[ke.getExtendedKeyCode()] = false;
        try {
            keyBindings.get(ke.getKeyCode()).isDown = false;
        } catch (Exception e) {

        }
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
        SettingsManager.init();
        System.out.println(SettingsManager.getSetting(Setting.PORT));
        
        bind(KeyEvent.VK_W, Key.up);
        bind(KeyEvent.VK_A, Key.left);
        bind(KeyEvent.VK_S, Key.down);
        bind(KeyEvent.VK_D, Key.right);

        walls = new LinkedList<Wall>();
        // sets the keybindings
        handler = new Handler();
        tank = new Tank(100, 100, 64, 64, ID.Tank, this);
        enemyTank = new EnemyTank(400, 100, 64, 64, ID.Tank, this);
        enemyTurret = new EnemyTurret(enemyTank.getX(), enemyTank.getY(), 10, 10, ID.Turret, enemyTank);
        // inits tank at 100 100 and gives it the game instance

        turret = new Turret(tank.getX(), tank.getY(), 10, 10, ID.Turret, tank);
        // creates a turret for the tank
        walls.add(new Wall(10, 10, 30, HEIGHT - 70, ID.LeftWall));
        walls.add(new Wall(10, HEIGHT - 90, WIDTH - 50, 30, ID.BottomWall));
        walls.add(new Wall(WIDTH - 50, 10, 30, HEIGHT - 70, ID.RightWall));
        walls.add(new Wall(10, 10, WIDTH - 30, 30, ID.TopWall));
        walls.add(new Wall(200, 200, 100, 100, ID.BreakableWall));
        for (int i = 0; i < walls.size(); i++) {
            handler.addObject(walls.get(i));
        }

        handler.addObject(tank);
        handler.addObject(enemyTank);
        handler.addObject(turret);
        handler.addObject(enemyTurret);

// adds the two objects to the handler
    }

    public static int getMouseX() {
        return mouseX;
    }

    public static int getMouseY() {
        return mouseY;
    }

    private final synchronized void stop() {
        if (!running)
            return;
        running = false;
        try {
            th.join();
            cRT.join();
        } catch (InterruptedException e) {
        }
        System.exit(1);
    }

    private final synchronized void start() {
        // If the program is already running then do nothing but if not running,
        // make it run and start the thread
        if (running)
            return;
        running = true;
        th = new Thread(this);
        cRT = new ClientRecieveThread(game);

        th.start();
        cRT.start();
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

    private void createBytes() {
        allBytes[0] = 1; // says its an in game byte

        //the x encoder start
        bb = ByteBuffer.allocate(4);
        bb.putInt(tank.getX());
        byte[] temp = bb.array();
        for (int i = 0; i < temp.length; i++) {
            allBytes[i + 1] = temp[i];
        }
        // the x encoder end
        // the y encoder start
        bb = ByteBuffer.allocate(4);
        bb.putInt(tank.getY());
        temp = bb.array();
        for (int i = 0; i < temp.length; i++) {
            allBytes[i + 5] = temp[i];
        }
        // y encoder end

        // the rotation encoder start
        bb = ByteBuffer.allocate(8);
        bb.putDouble(turret.getRotate());
        temp = bb.array();
        for (int i = 0; i < temp.length; i++) {
            allBytes[i + 10] = temp[i];
        }
        // rotation encoder end        

        allBytes[19] = (byte) tank.getMoveDir().ordinal();

        if (turret.isShooting())
            allBytes[20] = 0;
        else
            allBytes[20] = 1;

        if (Key.up.isDown)
            allBytes[21] = 0;
        else
            allBytes[21] = 1;

        if (Key.down.isDown)
            allBytes[22] = 0;
        else
            allBytes[22] = 1;

        if (Key.left.isDown)
            allBytes[23] = 0;
        else
            allBytes[23] = 1;

        if (Key.right.isDown)
            allBytes[24] = 0;
        else
            allBytes[24] = 1;
    }

    public void decodeBytes(byte[] bmain) {

        byte[] temp = new byte[4];
        if (bmain[0] == 1) {
            for (int i = 0; i < 4; i++) {
                temp[i] = bmain[i + 1];
            }
            bb = ByteBuffer.wrap(temp);
            enemyX = bb.getInt();

            for (int i = 0; i < 4; i++) {
                temp[i] = bmain[i + 5];
            }
            bb = ByteBuffer.wrap(temp);
            enemyY = bb.getInt();

            temp = new byte[8];
            for (int i = 0; i < 8; i++) {
                temp[i] = bmain[i + 10];
            }
            bb = ByteBuffer.wrap(temp);
            enemyRotate = bb.getDouble();

            enemyPointing = bmain[19];

            if (bmain[20] == 0)
                enemyShooting = true;
            else
                enemyShooting = false;
        }

    }

}
