/*
* Tanks For Playing
* Jeff St. Jean & Owen Hooper
* Mr. Martens ICS4UP - ISU
* January 24th, 2018 
*
* Objective: To eliminate enemny players
* Number of Players: 2
* Controls: Click on "controls" in main menu
*
*/


import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class Game implements Runnable, KeyListener {

    private static Renderer renderer; // Holds the renderer
    private boolean running = false; // Is the game running
    private Thread th; // Holds the thread
    static Handler handler; // Holds the handler
    public static Game game; // Holds the game
    static JFrame frame; // Holds the JFrame
    public static final int WIDTH = 1280; // The width of the JFrame
    public static final int HEIGHT = 720; // The height of the JFrame
    public static int levelSelect = 0; // Allows for changing of levels or "maps"
    public final String TITLE = "Tanks For Playing"; // The title
    public static double TANK_SIZE = 64; // Size of the tanks
    public static HashMap<Integer, Key> keyBindings = new HashMap<>(); // Hashmap that holds the keybindings (ex. WASD)
    public static boolean other[] = new boolean[256];
    private Tank tank1, tank2; // Holds Tanks
    private Turret turret1, turret2; // Holds turrets 

    // Properties
    private static final Properties USER_SETTINGS = new Properties(), DEFAULT_SETTINGS = new Properties();
    private final File userSettingsLocation = new File("src/resources/config/config.properties"), defaultSettingsLocation = new File("src/resources/default_config/default_config.properties");



    //<editor-fold defaultstate="collapsed" desc=" Getters, setters, constructs and listeners">
    
    // Binds a key to a action
    public void bind(Integer keyCode, Key key) {
        keyBindings.put(keyCode, key);
    }
    
    // Releases all keys
    public void releaseAll() {
        for (Key key : keyBindings.values()) {
            key.isDown = false;
        }
    }
    
    // Return handler
    public static Handler getHandler() {
        return handler;
    }
    
    // Get tank1
    public Tank getTank1() {
        return tank1;
    }
    
    // Get tank2
    public Tank getTank2() {
        return tank2;
    }
    
    // Code called on exit
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
    }

    public static void render(Graphics2D g) {
        if (handler != null)
            handler.render(g);
        // Has handler render all gameObjects
        // Checks to see if null to avoid NPE
    }
    
    // Run on start
    @Override
    public void run() {
        // Add Listeners
        frame.addKeyListener(this);
        frame.add(renderer);
        frame.setVisible(true);
        frame.addKeyListener(this);
        renderer.requestFocus();
        renderer.addKeyListener(this);

        init();
        
        // Timing control vars
        long lastTime = System.nanoTime();
        final double numberOfTicks = 60.0;
        double ns = 1000000000 / numberOfTicks;
        double delta = 0;
        int updates = 0;
        int frames = 0;
        long timer = System.currentTimeMillis();
        
        //Timing control
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
    
    // Called on every tick
    private void tick() {

        renderer.repaint(); // tells renderer to repaint if it hasn't already
        handler.tick(); // tells handler to tick all game objects
        
        if(Key.pause.isDown){
            PauseMenu pm = new PauseMenu(frame, true);
            pm.setVisible(true);
            releaseAll();
        }
            
        
    }

    @Override
    public void keyTyped(KeyEvent ke) {}

    @Override
    public void keyPressed(KeyEvent ke) {
        // Updates the key bindings
        other[ke.getExtendedKeyCode()] = true;
        try {
            keyBindings.get(ke.getKeyCode()).isDown = true;
        } catch (Exception e) {

        }
    }
    
    // Set/reset tank and tank type
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
    
    // Gets the turret
    public Turret getTurret(int num) {
        if(num == 1)
        return turret1;
        else return turret2;
    }
    
    // Updates keys
    @Override
    public void keyReleased(KeyEvent ke) {
        other[ke.getExtendedKeyCode()] = false;

        try {
            keyBindings.get(ke.getKeyCode()).isDown = false;
        } catch (Exception e) {

        }
    }

    // Run on game reset
    public void reset() {
        Handler.reset();
        
        // Level Selection
        if (levelSelect == 0) {
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


    // Game state
    public static enum STATE {
        MENU, GAME, PAUSE, CONTROLS, WIN
    };
    
    public Game() {
        // Initiallizes the renderer
        renderer = new Renderer();
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Config Methods ">
    
    // Loads configs
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

        TANK_SIZE = getDoubleUserPropertyThenDefault("tankSize", 64);

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
        
        // Bind keys
        // Player 1
        bind(KeyEvent.VK_W, Key.up1);
        bind(KeyEvent.VK_A, Key.left1);
        bind(KeyEvent.VK_S, Key.down1);
        bind(KeyEvent.VK_D, Key.right1);
        bind(KeyEvent.VK_SHIFT, Key.mine1);
        bind(KeyEvent.VK_H, Key.turretLeft1);
        bind(KeyEvent.VK_J, Key.turretRight1);
        bind(KeyEvent.VK_SPACE, Key.shoot1);
        // Player 2
        bind(KeyEvent.VK_NUMPAD8, Key.up2);
        bind(KeyEvent.VK_NUMPAD4, Key.left2);
        bind(KeyEvent.VK_NUMPAD5, Key.down2);
        bind(KeyEvent.VK_NUMPAD6, Key.right2);
        bind(KeyEvent.VK_ENTER, Key.mine2);
        bind(KeyEvent.VK_LEFT, Key.turretLeft2);
        bind(KeyEvent.VK_RIGHT, Key.turretRight2);
        bind(KeyEvent.VK_UP, Key.shoot2);
        
        // Pause
        bind(KeyEvent.VK_P, Key.pause);
        


        // Sets the keybindings
        handler = new Handler();
        
        
        // Inits tank1 at 100, 100 and gives it the game instance
        tank1 = new Tank(100, 100, TANK_SIZE, TANK_SIZE, ID.Tank, game, 1);
        turret1 = new Turret(tank1.getX(), tank1.getY(), 10, 10, ID.Turret, tank1);
        handler.addObject(tank1);
        handler.addObject(turret1);
            
        // Inits tank2 at 100, 100 and gives it the game instance
        tank2= new Tank(1125, 525, TANK_SIZE, TANK_SIZE, ID.Tank, game, 2);
        turret2 = new Turret(tank2.getX(), tank2.getY(), 10, 10, ID.Turret, tank2);
        handler.addObject(tank2);
        handler.addObject(turret2);
        
        // Adds the two objects to the handler
        frame.addKeyListener(this);
    }

    public static void main(String[] args) {

        game = new Game();
        frame = new JFrame(game.TITLE);
        frame.setIconImage(ImageLoader.imageLoader("src/resources/graphics/icon.png"));
        // Adds the instance of the game to the JFrame
        // frame.add(game);
        // Causes the window to be at preferred size initially
        frame.setSize(WIDTH, HEIGHT);
        // frame.pack();
        // Exits program on close
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Sets the program so it cannot be re-sizable
        frame.setResizable(false);

        // Adds the renderer to the jFrame
        frame.setVisible(true);

        frame.addKeyListener(game);
        game.start();
        MainMenu menu = new MainMenu(frame, true);
        menu.setVisible(true);

        generateOuterWalls();
        
        // Level selection
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
