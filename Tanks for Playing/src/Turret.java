
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
    private int coolDown = 20, coolDownCounter = 20;
    public Turret(int x, int y, ID id, Tank t) {
        super(x, y, id);
        tank = t;
        bounds = new Rectangle(x,y, 10, 10);
        turret = ImageLoader.imageLoader("./graphics/TurretGreen.png");
    }

    @Override
    public void tick() {
        coolDownCounter++;
        mouseX = tank.getGame().getMouseX();
        mouseY = tank.getGame().getMouseY();
        x = tank.getX() + tank.getSize() / 2;
        y = tank.getY() + tank.getSize() / 2;
        xd = (double) x;
        yd = (double ) y;
        bounds.setLocation(x, y);
        rotate = Math.atan2((mouseY - yd), (mouseX - xd)) - Math.PI / 2;
        if(Key.shoot.isDown && coolDownCounter > coolDown) {
            shoot();
            coolDownCounter = 0;
        }
     
    }

    @Override
    public void render(Graphics g) {
        g2d = (Graphics2D) g;
        g2d.rotate(rotate + Math.toRadians(90), xd ,yd );

       
        g2d.drawImage(turret, x - 16, y - 16, 64,32, null);
       g2d.rotate(-(rotate + Math.toRadians(90)), xd ,yd );
        
    }
    
    private void shoot(){
        tank.getGame().getHandler().addObject(new Bullet(x, y, ID.Bullet, rotate, 5));
    }
    
}
