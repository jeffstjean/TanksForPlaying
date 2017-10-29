
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject {

   
    private final int size = 15;
    private int speed = 2;
    
    
    private double rotate;

    public Bullet(int x, int y, int width, int height, ID id, int s, double r) {
        super(x, y, width, height, id);
        bounds .setBounds(x, y, size, size);
        speed = s;
  
        motionX = (int) (speed * Math.cos(r + Math.toRadians(90)));
        // sets the x speed of the bullet using trig
        motionY = (int) (speed * Math.sin(r + Math.toRadians(90)));
        // sets the y speed of the bullet using trig
        rotate = r;
       
    }
    
    @Override
    public void tick() {
        x += motionX;
        y += motionY;
        // moves bullet by preset x and y speeds
        bounds.setLocation(x, y);
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




    
    public int getSize() {
        return size;
    }

    public void shoot() {
        
        Server.logger.info("we havent added shoot yet");
        
    }

}
