
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject {

   
    private final double size = 15;
    private double speed = 2;
    
    private BufferedImage bullet;
    private double rotate;

    public Bullet(double x, double y, double width, double height, ID id, double s, double r) {
        super(x, y, width, height, id);
        bounds.setRect(x, y, size, size);
        speed = s;
  
        motionX =  (speed * Math.cos(r + Math.toRadians(90)));
        // sets the x speed of the bullet using trig
        motionY =  (speed * Math.sin(r + Math.toRadians(90)));
        // sets the y speed of the bullet using trig
        rotate = r;
        bullet = ImageLoader.imageLoader("./graphics/Bullet.png"); 
    }
    
    

    


    @Override
    public void tick() {
        // moves bullet by preset x and y speeds
        bounds.setRect(x, y, size,size);
        // moves the bounds box
        if(aliveForTicks > 200) Game.handler.removeObject(this);
    }

    @Override
    public void collision(GameObject gO) {
       if(gO.id == ID.TopWall || gO.id == ID.BottomWall) motionY = -motionY;
       if(gO.id == ID.RightWall || gO.id == ID.LeftWall) motionX = -motionX;
       if(gO.id == ID.Bullet) {
           Game.handler.removeObject(gO);
           Game.handler.removeObject(this);
       }
       if(gO.id ==ID.BreakableWall){
           gO.collision(this);
           Game.handler.removeObject(this);
       }
    }




    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.draw(bounds);
        AffineTransform t = new AffineTransform();
        t.translate(x, y);
        t.scale(size/bullet.getWidth(), size/bullet.getHeight());
        // draws the image of the bullet and rectangle
        g2d.drawImage(bullet, t, null);
    }
    
    public double getSize() {
        return size;
    }

    public void shoot() {
        System.out.println("Shoot");
    }

}
