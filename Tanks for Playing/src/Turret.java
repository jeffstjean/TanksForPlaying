
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
    private int coolDown = 20, coolDownCounter = 20, turretShootCounter = 8;
    public Turret(int x, int y, ID id, Tank t) {
        super(x, y, id);
        tank = t;
        bounds = new Rectangle(x,y, 10, 10);
        turret = ImageLoader.imageLoader("./graphics/TurretGreen.png");
    }

    @Override
    public void tick() {
        if(turretShootCounter == 0)
            turret = ImageLoader.imageLoader("./graphics/TurretGreen.png");
        turretShootCounter --;
        coolDownCounter++; // increases the time since last shot by 1
        mouseX = tank.getGame().getMouseX();
        mouseY = tank.getGame().getMouseY();
        
        x = tank.getX() + tank.getSize() / 2;
        y = tank.getY() + tank.getSize() / 2;
        // sets x and y based on x and y of the tank it is attached to
        xd = (double) x;
        yd = (double ) y;
        
        bounds.setLocation(x, y);
        rotate = Math.atan2((mouseY - yd), (mouseX - xd)) - Math.PI / 2;
        // sets the amount the turret needs to rotate based on the mouse location
        if(Key.shoot.isDown && coolDownCounter > coolDown) {
            shoot();
            turret = ImageLoader.imageLoader("./graphics/TurretShotGreen.png");
            coolDownCounter = 0;
            // shoots and sets timer back to 0 if conditions are met
        }
        
     
    }

    @Override
    public void render(Graphics g) {
        g2d = (Graphics2D) g;
        g2d.rotate(rotate + Math.toRadians(90), xd ,yd );//rotates graphics
        g2d.drawImage(turret, x - 16, y - 16, 64,32, null);//renders image
       g2d.rotate(-(rotate + Math.toRadians(90)), xd ,yd );//rotates grpahics back
        
    }
    
    private void shoot(){
        turretShootCounter = 10;
        double subX = -(tank.getSize() / 2 * Math.sin(rotate));
        double subY = (tank.getSize() / 2 * Math.cos(rotate));
        Game.getHandler().addObject(new Bullet(x + (int)subX, y + (int)subY, ID.Bullet, rotate, 5));
    }
    
}
