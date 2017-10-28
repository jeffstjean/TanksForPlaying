
import java.net.DatagramPacket;
import java.net.InetAddress;

import java.nio.ByteBuffer;

import java.util.HashMap;
import java.util.LinkedList;

public class Game implements Runnable {

    private boolean running = false;
    private Thread th;
    static Handler handler;
    public Key[] key = new Key[2];
    public static final int NUMBER_OF_PLAYERS = 2;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public final String TITLE = "Tanks For Playing";
    private Tank[] tank = new Tank[NUMBER_OF_PLAYERS];
    private Turret[] turret = new Turret[NUMBER_OF_PLAYERS];
    private ByteBuffer bb;
    private LinkedList<Wall> walls;

    private long[] mostRecent = new long[NUMBER_OF_PLAYERS];
    
    public HashMap<InetAddress, Integer> portMap;

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
                Server.logger.info("Ticks: " + updates
                        + "      Frames Per Second(FPS): " + frames);
                updates = 0;
                frames = 0;
            }
        }
        stop();
    }

    private void tick() {

        handler.tick(); // tells handler to tick all game objects
      

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
        for (int i = 0; i < Game.NUMBER_OF_PLAYERS; i++) {
            tank[i] = new Tank(100, 100, 64, 64, ID.Tank, this, 0);
            turret[i] = new Turret(tank[0].getX(), tank[0].getY(), 10, 10, ID.Turret, tank[0]);
            key[i] = new Key();
            handler.addObject(tank[i]);

            handler.addObject(turret[i]);
        }

        // inits tank at 100 100 and gives it the game instance
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
        if (running) {
            return;
        }
        running = true;
        th = new Thread(this);

        th.start();

    }

    public static Handler getHandler() {
        return handler;
    }

    public byte[] createBytes(int n) {
        byte[] allBytes = new byte[256];
        byte[] temp;
        for (int j = 0; j < NUMBER_OF_PLAYERS; j++) {
            bb = ByteBuffer.allocate(4);
            bb.putInt(tank[j].getX());
            temp = bb.array();
            for (int i = 0; i < temp.length; i++) {
            allBytes[i + 1 +(j * 20)] = temp[i];
            
            }
            
            
            bb = ByteBuffer.allocate(4);
            bb.putInt(tank[j].getY());
            temp = bb.array();
            for (int i = 0; i < temp.length; i++) {
            allBytes[i + 5 +(j * 20)] = temp[i];
            }
            
            
            allBytes[9] = (byte)tank[j].getIndex();
            
            
            
            bb = ByteBuffer.allocate(8);
            bb.putDouble(turret[j].getRotate());
            temp = bb.array();
            for (int i = 0; i < temp.length; i++) {
            allBytes[i + 10 +(j * 20)] = temp[i];
            }
            if(turret[j].isShooting())
            allBytes[19 + (20 * j)] = 0;
            else
                allBytes[19 + (20*j)] = 1;
            
            allBytes[20 + (j*20)] = (byte)tank[j].getMoveDir().ordinal();
            
        }
        
        bb = ByteBuffer.allocate(8);
        bb.putLong(mostRecent[n]);
        temp = bb.array();
        for (int i = 0; i < temp.length; i++) {
            allBytes[i + 14] = temp[i];
        }
        return allBytes;
    }

    public void decodeBytes(DatagramPacket p) {
        byte[] bmain = p.getData();
        int index = portMap.get(p.getAddress());
        byte[] temp = new byte[8];
        for (int i = 0; i < 8; i++) {
                temp[i] = bmain[i + 14];
            }
            bb = ByteBuffer.wrap(temp);
        long tempL = bb.getLong();
        if(mostRecent[index] > tempL){
        Server.logger.info("got old data");
        }else{
        mostRecent[index] = tempL;
        key[index].up = bmain[1] == 0;

            key[index].down = bmain[2] == 0;

            key[index].left = bmain[3] == 0;

            key[index].right = bmain[4] == 0;
        
        for (int i = 0; i < 4; i++) {
                temp[i] = bmain[i + 5];
            }
            bb = ByteBuffer.wrap(temp);
            turret[index].setMouseX(bb.getInt());
            
            for (int i = 0; i < 4; i++) {
                temp[i] = bmain[i + 9];
            }
            bb = ByteBuffer.wrap(temp);
            turret[index].setMouseY(bb.getInt());
            
        

            key[index].shoot = bmain[13] == 0;

            
        }
        }

    }


