
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.net.DatagramPacket;
import java.net.InetAddress;


import java.nio.ByteBuffer;


import java.util.HashMap;
import java.util.LinkedList;


import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;


public class Game implements Runnable {


   
    private boolean running = false;
    private Thread th;
    static Handler handler;
    public Key[] key = new Key[2];
    public static final int NUMBER_OF_PLAYERS = 2;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public final String TITLE = "Tanks For Playing";
    private Tank[] tank = new Tank[2];
    private Turret[] turret = new Turret[2];
    private ByteBuffer bb;
    private LinkedList<Wall> walls;
    private byte[] allBytes;
    public HashMap<InetAddress,Integer> portMap;

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
       
        handler.tick(); // tells handler to tick all game objects
        createBytes();
       
      
       
    }

   

   


   

   

   
   
   
   
    

    public static enum STATE {
        MENU, GAME, PAUSE, CONTROLS, WIN
    };

    public Game() {
        this.portMap = new HashMap<>();
        
    }

    public void init() {
        
        
        
        
        
        walls = new LinkedList<Wall>();
        // sets the keybindings
        handler = new Handler();
        tank[0] = new Tank(100, 100, 64, 64, ID.Tank, this, 0);
        
        // inits tank at 100 100 and gives it the game instance
        
        turret[0] = new Turret(tank[0].getX(), tank[0].getY(), 10,10,ID.Turret, tank[0]);
        // creates a turret for the tank
        walls.add(new Wall(10, 10, 30, HEIGHT - 70, ID.LeftWall));
        walls.add(new Wall (10, HEIGHT - 90, WIDTH - 50, 30, ID.BottomWall));
        walls.add(new Wall (WIDTH - 50, 10, 30, HEIGHT - 70, ID.RightWall));
        walls.add(new Wall(10,10,WIDTH- 30,30, ID.TopWall));
        walls.add(new Wall(200,200,100,100,ID.BreakableWall));  
        for (int i = 0; i < walls.size(); i++) {
            handler.addObject(walls.get(i));
        }
        
        handler.addObject(tank[0]);
        
        handler.addObject(turret[0]);
        
        
        
       
        
        
        
// adds the two objects to the handler
    }

    

    


    private final synchronized void stop() {
        if (!running) {
            return;
        }
        running = false;
        try {
            th.join();
            
        } catch (InterruptedException e) {
        }
        System.exit(1);
    }

    final synchronized void start() {
        // If the program is already running then do nothing but if not running,
        // make it run and start the thread
        if (running)
            return;
        running = true;
        th = new Thread(this);
       
        
        th.start();
        
    }

    

    

    

    public static Handler getHandler() {
        return handler;
    }

    
    private void createBytes(){

    }
    
    public void decodeBytes(DatagramPacket p){
         byte[] bmain = p.getData();
         int index = portMap.get(p.getAddress());
        byte[] temp = new byte[4]; 
        if (bmain[0] == 1){
            for (int i = 0; i < 4; i++) {
                temp[i] = bmain[i+1];
            }
            bb = ByteBuffer.wrap(temp);
            tank[index].setX(bb.getInt());
            
            
            for (int i = 0; i < 4; i++) {
                temp[i] = bmain[i+5];
            }
            bb = ByteBuffer.wrap(temp);
            tank[index].setX(bb.getInt());
            
            temp = new byte[8];
            for (int i = 0; i < 8; i++) {
                temp[i] = bmain[i+10];
            }
            bb = ByteBuffer.wrap(temp);
            turret[index].setRotate(bb.getDouble());
            
            tank[index].setPointing(bmain[19]); 
            
            key[index].shoot.isDown = bmain[20] == 0;
            
            key[index].up.isDown = bmain[21] == 0;
            
            key[index].down.isDown = bmain[22] == 0;
            
            key[index].left.isDown = bmain[23] == 0;
            
            key[index].right.isDown = bmain[24] == 0;
            
        }
        
    }
    
}
