
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject {

    private Rectangle bounds;
    private final int size = 15;
    private int speed = 2;
    
    private BufferedImage bullet;
    private double rotate;
    public Bullet(int x, int y, ID id, double r, int s) {
        super(x, y, id);
        bounds = new Rectangle(x, y, size, size);
        speed = s;
  
        motionX = (int) (speed * Math.cos(r + Math.toRadians(90)));
        motionY = (int) (speed * Math.sin(r + Math.toRadians(90)));
        rotate = r;
        bullet = ImageLoader.imageLoader("./graphics/Bullet.png"); 

    }
    


    @Override
    public void tick() {
        x += motionX;
        y+= motionY;
        bounds.setLocation(x, y);
    }


    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.draw(bounds);
        g2d.drawImage(bullet, x, y, size, size, null);
    }
    
    public int getSize() {
        return size;
    }

    public void shoot() {
        System.out.println("Shoot");
    }

}
