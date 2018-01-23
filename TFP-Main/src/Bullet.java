/*
* This is the bullet class it is the object that gets initialized when a bullet
* is fired. It extends GameObject and has Static variables for stats, 2 different constructors
* and has methods that override GameObject
*/





import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Bullet extends GameObject {

   
    private final double size = 10; // size of bullet
    private double speed = 4; // default speed of bullet
    private final BufferedImage bullet; // image of bullet for rendering
    private double rotate; // the direction in which the bullet is fired
    private int damage = 20; // the amount oof damage the bullet will do by default
    
    private static int shotsFired = 0, hits; // static variables for stats calculation
    
    
    public Bullet(double x, double y, double width, double height, ID id, double s, double r) {
        super(x, y, width, height, id);
        bounds.setRect(x, y, size, size); // sets size of bounds rectangle for collision
        speed = s;
  
        motionX =  (speed * Math.cos(r + Math.toRadians(90)));
        // sets the x speed of the bullet using trig
        motionY =  (speed * Math.sin(r + Math.toRadians(90)));
        // sets the y speed of the bullet using trig
        rotate = r; 
        bullet = ImageLoader.imageLoader("src/resources/graphics/Bullet.png"); // gets the graphic image for the bullet
        shotsFired ++; // increases static variable for shots fired
    }
    // same as previous constructor but with a custom amount of damage
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
    // getter for static variable
    public static int getShotsFired() {
        return shotsFired;
    }
// getter for static variable
    public static int getHits() {
        return hits ;
    }

    


    @Override
    public void tick() {
        
        bounds.setRect(x, y, size,size);
        // moves the bounds box
        
        //removes the bullet after 200 ticks
        if(aliveForTicks > 200) Game.handler.removeObject(this);
        
        
    }

    @Override
    public void collision(GameObject gO) {
        //if hits the top or bottom wall invert motion y
       if(gO.id == ID.TopWall || gO.id == ID.BottomWall)motionY = -motionY;        
       
       // if hits right or left wall invert motion left
       if(gO.id == ID.RightWall || gO.id == ID.LeftWall) motionX = -motionX;
       
       //if it hits another buller
       if(gO.id == ID.Bullet) {
           Game.handler.removeObject(gO); // remove both the bullet 
           Game.handler.removeObject(this);// and this bullet
           try {
               SoundManager.playHitSound(); // use the sound manager to play a hit sound
           } catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException ex) {
               
           }
       }
       
       // if hits a breakable (Normal) wall
       if(gO.id ==ID.BreakableWall){
           
           gO.collision(this); // run the wall's collision
           Game.handler.removeObject(this); // remove this bullet
           try {
               SoundManager.playHitSound(); // play sound
           } catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException ex) {
           }
       }
       
       // if it hits a tank and it has exited the barrel
       if(gO.id == ID.Tank && aliveForTicks >= 5){
           hits++; // increase hits for stats
           ((Tank)gO).reduceHealth(damage); // cast gO to tank and take health off 
           Game.handler.removeObject(this); // remove this bullet
           try {
               SoundManager.playHitSound(); // play hit sound
           } catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException ex) {
           }
       }
    }




    @Override // renders the bullet image in the right place
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
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

    @Override
    public void reset() {
        Game.game.handler.removeObject(this);
    }

   
    

}
