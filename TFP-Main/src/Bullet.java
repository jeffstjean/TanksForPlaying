
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Bullet extends GameObject {

   
    private final double size = 10;
    private double speed = 4;
    
    private BufferedImage bullet;
    private double rotate;
    private int damage = 20;
    private int canDoDamageTick = 0;
    private static int shotsFired = 0, hits;
    
    
    public Bullet(double x, double y, double width, double height, ID id, double s, double r) {
        super(x, y, width, height, id);
        bounds.setRect(x, y, size, size);
        speed = s;
  
        motionX =  (speed * Math.cos(r + Math.toRadians(90)));
        // sets the x speed of the bullet using trig
        motionY =  (speed * Math.sin(r + Math.toRadians(90)));
        // sets the y speed of the bullet using trig
        rotate = r;
        bullet = ImageLoader.imageLoader("src/resources/graphics/Bullet.png");
        shotsFired ++;
    }
    
    public Bullet(double x, double y, double width, double height, ID id, double s, double r, int damage) {
        super(x, y, width, height, id);
        bounds.setRect(x, y, size, size);
        speed = s;
  
        motionX =  (speed * Math.cos(r + Math.toRadians(90)));
        // sets the x speed of the bullet using trig
        motionY =  (speed * Math.sin(r + Math.toRadians(90)));
        // sets the y speed of the bullet using trig
        rotate = r;
        bullet = ImageLoader.imageLoader("src/resources/graphics/Bullet.png"); 
        this.damage = damage;
        shotsFired++;
    }

    public static int getShotsFired() {
        return shotsFired;
    }

    public static int getHits() {
        return hits;
    }

    


    @Override
    public void tick() {
        // moves bullet by preset x and y speeds
        bounds.setRect(x, y, size,size);
        // moves the bounds box
        if(aliveForTicks > 200) Game.handler.removeObject(this);
        canDoDamageTick ++;
    }

    @Override
    public void collision(GameObject gO) {
       if(gO.id == ID.TopWall || gO.id == ID.BottomWall)motionY = -motionY;        
       if(gO.id == ID.RightWall || gO.id == ID.LeftWall) motionX = -motionX;
       if(gO.id == ID.Bullet) {
           Game.handler.removeObject(gO);
           Game.handler.removeObject(this);
           try {
               SoundManager.playHitSound();
           } catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException ex) {
               Logger.getLogger(Bullet.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
       if(gO.id ==ID.BreakableWall){
           hits++;
           gO.collision(this);
           Game.handler.removeObject(this);
           try {
               SoundManager.playHitSound();
           } catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException ex) {
               Logger.getLogger(Bullet.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
       if(gO.id == ID.Tank && canDoDamageTick >= 5){
           hits++;
           ((Tank)gO).reduceHealth(damage);
           Game.handler.removeObject(this);
           try {
               SoundManager.playHitSound();
           } catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException ex) {
               Logger.getLogger(Bullet.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
    }




    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        //g2d.draw(bounds);
        AffineTransform t = new AffineTransform();
        t.translate(x, y);
        t.scale(size/bullet.getWidth(), size/bullet.getHeight());
        // draws the image of the bullet and rectangle
        g2d.drawImage(bullet, t, null);
    }
    
    public double getSize() {
        return size;
    }

    public int getDamage() {
        return damage;
    }

   
    

}
