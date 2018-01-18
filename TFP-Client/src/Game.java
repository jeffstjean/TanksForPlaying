
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

    static Handler handler;
    public static Game game;
    static JFrame frame;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static int levelSelect = 0;
    public final String TITLE = "Tanks For Playing";
    public static double TANK_SIZE = 64;
    public static HashMap<Integer, Key> keyBindings = new HashMap<>();

    public static boolean other[] = new boolean[256];
    private static int mouseX, mouseY;
    public static int NUM_PLAYERS;
    private ByteBuffer bb;
    private byte[] allBytes = new byte[256];
    private Tank tank1, tank2;
    private Turret turret1, turret2;
    public long maxMillis = 0;
    private static final Logger LOGGER = Logger.getLogger("ClientLog");
    private static LinkedList<Wall> walls;
    private LinkedList<Powerup> powerups;

    //config vars
    private static final Properties USER_SETTINGS = new Properties(), DEFAULT_SETTINGS = new Properties();
    private final File userSettingsLocation = new File("src/resources/config/config.properties"), defaultSettingsLocation = new File("src/resources/default_config/default_config.properties");

    public static int PLAYERNUMBER;
    private static Logger logger;

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
        if (!running)
            return;
        running = false;
        try {
            th.join();

        } catch (InterruptedException e) {
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
        System.out.println("started th");

    }

    public static void render(Graphics2D g) {
        if (handler != null)
            handler.render(g);
        // Has handler render all gameObjects
        // Checks to see if null to avoid NPE
    }

    @Override

    public void run() {

        frame.addKeyListener(this);
        frame.add(renderer);
        frame.setVisible(true);
        frame.addMouseMotionListener(this);
        frame.addMouseListener(this);
        frame.addKeyListener(this);
        renderer.requestFocus();
        renderer.addKeyListener(this);

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

    public void startGame() {
        start();
    }

    private void tick() {

        renderer.repaint(); // tells renderer to repaint if it hasn't already
        handler.tick(); // tells handler to tick all game objects

        for (int i = 0; i < powerups.size(); i++) {
            if (powerups.get(i).isAnimationComplete()) {
                // Delete any old powerups
                handler.removeObject(powerups.get(i));
                powerups.remove(i);
            }
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

    public void setTank(int num, Tank t) {
        if(num == 1){
            handler.removeObject(tank1);
            tank1 = t;
            handler.addObject(tank1);
            turret1.setTank(tank1);
            handler.removeObject(turret1);
            handler.addObject(turret1);
        }else{
            handler.removeObject(tank2);
            tank2 = t;
            handler.addObject(tank2);
            turret2.setTank(tank2);
            handler.removeObject(turret2);
            handler.addObject(turret2);
        }
    }

    public Turret getTurret(int num) {
        if(num == 1)
        return turret1;
        else return turret2;
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

    public void reset() {
        System.out.println("reset");
        handler.reset();
        if (levelSelect == 0) {
            System.out.println("No level selected. Defaulting to level 1.");
            levelSelect = 1;
        }
        switch (levelSelect) {
            case 1:
                generateLevel1();
                break;
            case 2:
                generateLevel2();
                break;
            case 3:
                generateLevel3();
                break;
            case 4:
                generateLevel4();
                break;
            default:
                System.out.println("Error");
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        //Only for testing mines - will be deleted

        //Proof of Concept code can be deleted
//        clickCounter++;
//        clickCounter = clickCounter % 4;
//        PowerupColor clr;
//        switch (clickCounter) {
//            case 0:
//                clr = PowerupColor.Red;
//                break;
//            case 1:
//                clr = PowerupColor.Green;
//                break;
//            case 2:
//                clr = PowerupColor.Yellow;
//                break;
//            default:
//                clr = PowerupColor.Blue;
//                break;
//        }
//        powerups.add(new Powerup((double) me.getX() - 16, (double) me.getY() - 39, 32, 32, ID.PowerUp, handler, clr));
//        handler.addObject(powerups.get(powerups.size() - 1));
        //End of POC
        mouseX = me.getX();
        mouseY = me.getY();
        // gets the mouse's x and y location
    }

    @Override
    public void mousePressed(MouseEvent me) {
        //if(MOUSECLICKTYPE == 0) {

        //sets the key shoot to be down when clicked
        //}
        mouseX = me.getX();
        mouseY = me.getY();
        //get the x and y location of the mouse for aiming
    }

    @Override
    public void mouseReleased(MouseEvent me) {

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
        if (!userSettingsLocation.exists())
            try {
                Files.copy(defaultSettingsLocation.toPath(), userSettingsLocation.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println("Couldn't copy default config file. Defaults will be used for all params.");
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

        TANK_SIZE = getDoubleUserPropertyThenDefault("tankSize", 64);
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

    public static double getDoubleUserPropertyThenDefault(String setting, double defaultIfError) {
        try {
            return Double.parseDouble(USER_SETTINGS.getProperty(setting));
        } catch (NumberFormatException e) {
            try {
                return Double.parseDouble(DEFAULT_SETTINGS.getProperty(setting));
            } catch (NumberFormatException ex) {
                return defaultIfError;
            }
        }
    }
//</editor-fold>

    public void init() {
        initConfigs();
        bind(KeyEvent.VK_W, Key.up1);
        bind(KeyEvent.VK_A, Key.left1);
        bind(KeyEvent.VK_S, Key.down1);
        bind(KeyEvent.VK_D, Key.right1);
        bind(KeyEvent.VK_SHIFT, Key.mine1);
        bind(KeyEvent.VK_H, Key.turretLeft1);
        bind(KeyEvent.VK_J, Key.turretRight1);
        bind(KeyEvent.VK_SPACE, Key.shoot1);

        bind(KeyEvent.VK_NUMPAD8, Key.up2);
        bind(KeyEvent.VK_NUMPAD4, Key.left2);
        bind(KeyEvent.VK_NUMPAD5, Key.down2);
        bind(KeyEvent.VK_NUMPAD6, Key.right2);
        bind(KeyEvent.VK_ENTER, Key.mine2);
        bind(KeyEvent.VK_LEFT, Key.turretLeft2);
        bind(KeyEvent.VK_RIGHT, Key.turretRight2);
        bind(KeyEvent.VK_UP, Key.shoot2);

        walls = new LinkedList<>();

        powerups = new LinkedList<>();

        // sets the keybindings
        handler = new Handler();
        
        
        // inits tank at 100 100 and gives it the game instance
        
            tank1 = new Tank(100, 100, TANK_SIZE, TANK_SIZE, ID.Tank, game, 1);
            turret1 = new Turret(tank1.getX(), tank1.getY(), 10, 10, ID.Turret, tank1);
            handler.addObject(tank1);
            handler.addObject(turret1);
            
            
            
            tank2= new Tank(1125, 525, TANK_SIZE, TANK_SIZE, ID.Tank, game, 2);
            turret2 = new Turret(tank2.getX(), tank2.getY(), 10, 10, ID.Turret, tank2);
            handler.addObject(tank2);
            handler.addObject(turret2); 
        

        
        frame.addKeyListener(this);
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

        // adds the renderer to the jFrame
        frame.setVisible(true);

        frame.addKeyListener(game);
        frame.addMouseMotionListener(game);
        frame.addMouseListener(game);
        game.start();
        MainMenu menu = new MainMenu(frame, true);
        menu.setVisible(true);

        walls.clear();
        generateOuterWalls();
        if (levelSelect == 0) {
            System.out.println("No level selected. Defaulting to level 1.");
            levelSelect = 1;
        }
        switch (levelSelect) {
            case 1:
                generateLevel1();
                break;
            case 2:
                generateLevel2();
                break;
            case 3:
                generateLevel3();
                break;
            case 4:
                generateLevel4();
                break;
            default:
                System.out.println("Error");
                break;
        }
        //walls.add(new Wall(200, 200, 100, 100, ID.BreakableWall));
    }

    public static void log(String log) {
        LOGGER.info(log);
    }

    public static void generateOuterWalls() {
        handler.addObject(new Wall(10, 10, 30, (double) HEIGHT - 70, ID.LeftWall));
        handler.addObject(new Wall(10, (double) HEIGHT - 90, (double) WIDTH - 50, 30, ID.BottomWall));
        handler.addObject(new Wall((double) WIDTH - 50, 10, 30, (double) HEIGHT - 70, ID.RightWall));
        handler.addObject(new Wall(10, 10, (double) WIDTH - 30, 30, ID.TopWall));
    }


    public static void generateLevel1() {
        //Left Side
        handler.addObject(new Wall(200, 100, 50, 50, ID.BreakableWall, true));
        handler.addObject(new Wall(200, 150, 50, 50, ID.BreakableWall, true));
        handler.addObject(new Wall(250, 150, 50, 50, ID.BreakableWall, true));
        for (int i = 200; i <= 400; i+=50) {
            handler.addObject(new Wall(250, i, 50, 50, ID.BreakableWall, false));
        }
        handler.addObject(new Wall(250, 450, 50, 50, ID.BreakableWall, true));
        handler.addObject(new Wall(200, 450, 50, 50, ID.BreakableWall, true));
        handler.addObject(new Wall(200, 500, 50, 50, ID.BreakableWall, true));
        
        //Right Side
        handler.addObject(new Wall(1000, 100, 50, 50, ID.BreakableWall, true));
        handler.addObject(new Wall(1000, 150, 50, 50, ID.BreakableWall, true));
        handler.addObject(new Wall(950, 150, 50, 50, ID.BreakableWall, true));
        for (int i = 200; i <= 400; i+=50) {
            handler.addObject(new Wall(950, i, 50, 50, ID.BreakableWall, false));
        }
        handler.addObject(new Wall(950, 450, 50, 50, ID.BreakableWall, true));
        handler.addObject(new Wall(1000, 450, 50, 50, ID.BreakableWall, true));
        handler.addObject(new Wall(1000, 500, 50, 50, ID.BreakableWall, true));
        
        //Top
        handler.addObject(new Wall(500, 150, 50, 50, ID.BreakableWall, true));
        for (int i = 550; i <= 650; i+=50) {
            handler.addObject(new Wall(i, 150, 50, 50, ID.BreakableWall, false));
        }
        handler.addObject(new Wall(700, 150, 50, 50, ID.BreakableWall, true));
        
        //Bottom
        handler.addObject(new Wall(500, 450, 50, 50, ID.BreakableWall, true));
        for (int i = 550; i <= 650; i+=50) {
            handler.addObject(new Wall(i, 450, 50, 50, ID.BreakableWall, false));
        }
        handler.addObject(new Wall(700, 450, 50, 50, ID.BreakableWall, true));
        
        //Center Right
        handler.addObject(new Wall(800, 300, 50, 50, ID.BreakableWall, true));
        
        
        //Center Left
        handler.addObject(new Wall(400, 300, 50, 50, ID.BreakableWall, true));
    }
    
    public static void generateLevel2() {
        boolean breakable;

        //Left Line
        breakable = false;
        for (int i = 100; i <= 500; i += 50) {
            if (i >= 200)
                breakable = true;
            if (i >= 450)
                breakable = false;
            handler.addObject(new Wall(300, i, 50, 50, ID.BreakableWall, breakable));
        }

        //Center Line
        breakable = false;
        for (int i = 150; i <= 450; i += 50) {
            handler.addObject(new Wall(600, i, 50, 50, ID.BreakableWall, breakable));
        }

        //Right Line
        breakable = false;
        for (int i = 100; i <= 500; i += 50) {
            if (i >= 200)
                breakable = true;
            if (i >= 450)
                breakable = false;
            handler.addObject(new Wall(900, i, 50, 50, ID.BreakableWall, breakable));
        }
    }

    public static void generateLevel3() {
        boolean breakable;
        //Top Left
        handler.addObject(new Wall(250, 100, 50, 50, ID.BreakableWall, true));
        handler.addObject(new Wall(250, 150, 50, 50, ID.BreakableWall, false));
        handler.addObject(new Wall(250, 200, 50, 50, ID.BreakableWall, false));
        handler.addObject(new Wall(250, 250, 50, 50, ID.BreakableWall, true));

        //Bottom Left
        handler.addObject(new Wall(250, 350, 50, 50, ID.BreakableWall, true));
        handler.addObject(new Wall(250, 400, 50, 50, ID.BreakableWall, false));
        handler.addObject(new Wall(250, 450, 50, 50, ID.BreakableWall, false));
        handler.addObject(new Wall(250, 500, 50, 50, ID.BreakableWall, true));

        //Center Top
        breakable = false;
        for (int i = 450; i <= 750; i += 50) {
            if (i >= 550)
                breakable = true;
            if (i >= 700)
                breakable = false;
            handler.addObject(new Wall(i, 100, 50, 50, ID.BreakableWall, breakable));
        }

        //Center
        handler.addObject(new Wall(600, 250, 50, 50, ID.BreakableWall, false));
        handler.addObject(new Wall(600, 300, 50, 50, ID.BreakableWall, false));
        handler.addObject(new Wall(600, 350, 50, 50, ID.BreakableWall, false));

        //Center Bottom
        breakable = false;
        for (int i = 450; i <= 750; i += 50) {
            if (i >= 550)
                breakable = true;
            if (i >= 700)
                breakable = false;
            handler.addObject(new Wall(i, 500, 50, 50, ID.BreakableWall, breakable));
        }

        //Top Right
        handler.addObject(new Wall(950, 100, 50, 50, ID.BreakableWall, true));
        handler.addObject(new Wall(950, 150, 50, 50, ID.BreakableWall, false));
        handler.addObject(new Wall(950, 200, 50, 50, ID.BreakableWall, false));
        handler.addObject(new Wall(950, 250, 50, 50, ID.BreakableWall, true));

        //Bottom Right
        handler.addObject(new Wall(950, 350, 50, 50, ID.BreakableWall, true));
        handler.addObject(new Wall(950, 400, 50, 50, ID.BreakableWall, false));
        handler.addObject(new Wall(950, 450, 50, 50, ID.BreakableWall, false));
        handler.addObject(new Wall(950, 500, 50, 50, ID.BreakableWall, true));
    }

    public static void generateLevel4() {
        boolean place = true;
        boolean breakable = true;
        for (int i = 225; i < 1125; i+=150) {
            
            for (int j = 50; j < 600; j += 50) {
                if (place) {
                    handler.addObject(new Wall(i, j, 50, 50, ID.BreakableWall, breakable)); 
                    breakable = !breakable;
                }
                place = !place;
            }
        }
    }

    public static void setLevelSelect(int level) {
        levelSelect = level;
    }

}
