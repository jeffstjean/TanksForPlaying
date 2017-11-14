
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;

public class Game implements Runnable, KeyListener, MouseInputListener {

    private static Renderer renderer;
    private boolean running = false;
    private Thread th;
    ClientRecieveThread cRT;
    static Handler handler;
    private static Game game;
    private static JFrame frame;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public final String TITLE = "Tanks For Playing";
    private int TANK_SIZE = 64;
    public HashMap<Integer, Key> keyBindings = new HashMap<>();
    
    public static boolean other[] = new boolean[256];
    private static int mouseX, mouseY;
    public static int NUM_PLAYERS;
    private ByteBuffer bb;
    private final byte[] allBytes = new byte[256];
    private Tank[] tank;
    private Turret[] turret;
    public long maxMillis = 0;
    private static final Logger LOGGER = Logger.getLogger("ClientLog");
    private LinkedList<Wall> walls;
    private LinkedList<Mine> mines;
    private LinkedList<Powerup> powerups;
    private ArrayList<Boolean> queuedShots;
    private ArrayList<Boolean> serverQueue;
    //config vars
    private static final Properties USER_SETTINGS = new Properties(), DEFAULT_SETTINGS = new Properties();
    private final File userSettingsLocation = new File("src/resources/config/config.properties"), defaultSettingsLocation = new File("src/resources/default_config/default_config.properties");

    public static int PLAYERNUMBER;
    private static Logger logger;
    private LinkedList<Wall> walls;
    private LinkedList<Mine> mines;
    private LinkedList<Powerup> powerups;

    
    //POC var for Powerups
    private int clickCounter = 0;
    

    
    //<editor-fold defaultstate="collapsed" desc=" Getters, setters, constructs and listeners">
    
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
    
    
        public static int getMouseX() {
        return mouseX;
    }

    public static int getMouseY() {
        return mouseY;
    }

    private synchronized void stop() {
        if (!running) {
            return;
        }
        running = false;
        try {
            th.join();
            cRT.join();
        } catch (InterruptedException e) {
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
        cRT = new ClientRecieveThread(game);

        th.start();
        System.out.println("started th");
        cRT.start();
        System.out.println("started crt");
    }

    public static void render(Graphics2D g) {
        if(handler!=null )handler.render(g);
        // Has handler render all gameObjects
        // Checks to see if null to avoid NPE
    }
    
    
    
    
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
                System.out.println("Main Thread Ticks: " + updates + "      Frames Per Second(FPS): " + frames);
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
        allBytes = Encoder.createBytes();
        for (int i = 0; i < mines.size(); i++) {
            if(mines.get(i).isAllAnimationsComplete()) // Delete any old mines
                mines.remove(i);
        }
        for (int i = 0; i < powerups.size(); i++) {
            if(powerups.get(i).isAnimationComplete()) // Delete any old mines
                powerups.remove(i);
        }
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

    public Tank[] getTank() {
        return tank;
    }

    public Turret[] getTurret() {
        return turret;
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
        //Only for testing mines - will be deleted
        
        mines.add(new Mine(tank[0].getX() + (tank[0].getSize()/4), tank[0].getX() + (tank[0].getSize()/4), 32, 32, ID.Mine, handler));
        handler.addObject(mines.get(mines.size() - 1));
        
        //Proof of Concept code can be deleted
        clickCounter++;
        clickCounter = clickCounter%4;
        PowerupColor clr;
        switch (clickCounter) {
            case 0:
                clr = PowerupColor.Red;
                break;
            case 1:
                clr = PowerupColor.Green;
                break;
            case 2:
                clr = PowerupColor.Yellow;
                break;
            default:
                clr = PowerupColor.Blue;
                break;
        }
        powerups.add(new Powerup(me.getX() - 16, me.getY() - 39, 32, 32, ID.PowerUp, handler, clr));
        handler.addObject(powerups.get(powerups.size() - 1));
        //End of POC
        
        
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

        Key.shoot.isDown = false;
        //sets the key binding of shoot to up 

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
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" Config Stuff ">
    
    private void initConfigs() {
        if (!userSettingsLocation.exists()) {
            try {
                Files.copy(defaultSettingsLocation.toPath(), userSettingsLocation.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println("Couldn't copy default config file. Defaults will be used for all params.");
            }
        }

        FileInputStream in;
        try {
            in = new FileInputStream(defaultSettingsLocation);
            DEFAULT_SETTINGS.load(in);
            in.close();
        } catch (IOException e) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            in = new FileInputStream(userSettingsLocation);
            USER_SETTINGS.load(in);
            in.close();
        } catch (IOException e) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, e);
        }

        NUM_PLAYERS = getIntUserPropertyThenDefault("numPlayers", 2);
        cRT.setPORT(getIntUserPropertyThenDefault("port", 4448));
        cRT.setHost(getStringUserPropertyThenDefault("ipAddress"));

        TANK_SIZE = getIntUserPropertyThenDefault("tankSize", 64);
        PLAYERNUMBER = getIntUserPropertyThenDefault("playerNumber", 0);

    }

    public static String getStringUserPropertyThenDefault(String setting) {
        return (USER_SETTINGS.getProperty(setting, DEFAULT_SETTINGS.getProperty(setting)));
    }

    public static int getIntUserPropertyThenDefault(String setting, int defaultIfError) {
        try {
            return Integer.parseInt(USER_SETTINGS.getProperty(setting));
        } catch (NumberFormatException e) {
            try {
                return Integer.parseInt(DEFAULT_SETTINGS.getProperty(setting));
            } catch (NumberFormatException ex) {
                return defaultIfError;
            }
        }
    }
//</editor-fold>
    
    public void init() {
        initConfigs();
        bind(KeyEvent.VK_W, Key.up);
        bind(KeyEvent.VK_A, Key.left);
        bind(KeyEvent.VK_S, Key.down);
        bind(KeyEvent.VK_D, Key.right);
        bind(KeyEvent.VK_SPACE, Key.mine);

        walls = new LinkedList<>();
        mines = new LinkedList<>();
        powerups = new LinkedList<>();
        queuedShots = new ArrayList<>();
        serverQueue = new ArrayList<>();
        
        // sets the keybindings
        handler = new Handler();
        tank = new Tank[NUM_PLAYERS];
        turret = new Turret[NUM_PLAYERS];
        // inits tank at 100 100 and gives it the game instance
        for (int i = 0; i < NUM_PLAYERS; i++) {
            tank[i] = new Tank(100 + 100 * i, 100, TANK_SIZE, TANK_SIZE, ID.Tank, game);
            turret[i] = new Turret(tank[i].getX(), tank[i].getY(), 10, 10, ID.Turret, tank[i]);
            handler.addObject(tank[i]);
            handler.addObject(turret[i]);
        }

        // creates a turret for the tank
        walls.add(new Wall(10, 10, 30, HEIGHT - 70, ID.LeftWall));
        walls.add(new Wall(10, HEIGHT - 90, WIDTH - 50, 30, ID.BottomWall));
        walls.add(new Wall(WIDTH - 50, 10, 30, HEIGHT - 70, ID.RightWall));
        walls.add(new Wall(10, 10, WIDTH - 30, 30, ID.TopWall));
        walls.add(new Wall(200, 200, 100, 100, ID.BreakableWall));
        for (int i = 0; i < walls.size(); i++) {
            handler.addObject(walls.get(i));
        }

// adds the two objects to the handler
    }



    public static void main(String[] args) {
        FileHandler fh;
        File file = new File("src/resources/logs/log.txt");
        
        try {
            // This block configure the logger with handler and formatter
            file.createNewFile();
            fh = new FileHandler(file.getPath());
            LOGGER.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            LOGGER.setUseParentHandlers(false);
            // the following statement is used to log any messages
            LOGGER.info("Logger started.");

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        game = new Game();

        frame = new JFrame(game.TITLE);
        frame.setIconImage(ImageLoader.imageLoader("./graphics/icon.png"));
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

    

  
    
    public static void log (String log) {
        LOGGER.info(log);
    }
    

    

}
