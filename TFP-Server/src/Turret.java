
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Turret extends GameObject{

    private Tank tank;
    private Rectangle bounds;
    private Graphics2D g2d;
    private double xd,yd,rotate;
    private double mouseX, mouseY;
    private BufferedImage turret;
    private int turretShootCounter = 7;
    private boolean shooting = false;
    
    private int coolDown = 20, coolDownCounter = 20;

    public Turret(int x, int y, int width, int height, ID id, Tank t) {
        super(x, y, width, height, id);

        tank = t;
        bounds = new Rectangle(x,y, 10, 10);
       
    }

    @Override
    public void tick() {
        shooting = false;
        
        turretShootCounter --;
        coolDownCounter++; // increases the time since last shot by 1
        
        
        x = tank.getX() + tank.getSize() / 2;
        y = tank.getY() + tank.getSize() / 2;
        // sets x and y based on x and y of the tank it is attached to
        xd = (double) x;
        yd = (double ) y;
        
        bounds.setLocation(x, y);
        
        // sets the amount the turret needs to rotate based on the mouse location
        if(Server.game.key[tank.getIndex()].shoot && coolDownCounter > coolDown) {
            shoot();
            
            coolDownCounter = 0;
            // shoots and sets timer back to 0 if conditions are met
        }
        
     
    }

    public void setRotate(double rotate) {
        this.rotate = rotate;
    }

    

    public double getRotate() {
        return rotate;
    }

    public boolean isShooting() {
        return shooting;
    }
    
    
    
    private void shoot(){

        shooting = true;
        turretShootCounter = 10;
        double subX = -(tank.getSize() / 2 * Math.sin(rotate));
        double subY = (tank.getSize() / 2 * Math.cos(rotate));
        Game.getHandler().addObject(new Bullet(x + (int)subX, y + (int)subY, 15, 15, ID.Bullet, 5, rotate));

    }
    
}
