
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class EnemyTank extends Tank {

    private int size = 64;
    private int speed = 2;
    private Game game;
    private BufferedImage body;
    private double rotate;
    private Rectangle top, bottom, left, right;

    public EnemyTank(int x, int y, int width, int height, ID id, Game g) {
        super(x,  y,  width,  height,  id,  g);
        
        body = ImageLoader.imageLoader("./graphics/TankRed.png");

    
    
    
    
    }
    
    
    public enum moveDirection {
       NONE, UP, UP_RIGHT, RIGHT,  DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT,
    };

    private moveDirection moveDir = moveDirection.NONE;

    

    public Game getGame() {
        return game;
    }

    public void setVals(int xt, int yt, int pointing){
        x = xt;
        y=yt;
        switch (pointing) {
            case 0:
                moveDir = moveDirection.NONE;
                break;
            case 1:
                moveDir = moveDirection.UP;
                break;
            case 2:
                moveDir = moveDirection.UP_RIGHT;
                break;

            case 3:
                moveDir = moveDirection.RIGHT;
                break;

            case 4:
                moveDir = moveDirection.DOWN_RIGHT;
                break;

            case 5:
                moveDir = moveDirection.DOWN;
                break;

            case 6:
                moveDir = moveDirection.DOWN_LEFT;
                break;

            case 7:
                moveDir = moveDirection.LEFT;
                break;

            case 8:
                moveDir = moveDirection.UP_LEFT;
                break;
            
            default:
               moveDir = moveDirection.NONE;
               break;     
        }
    }
    
    
    @Override
    public void tick() {
        
        bounds.setLocation(x, y);
        
     
        
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        switch (moveDir){
            case NONE:
               
               break;
            case LEFT:
                rotate = Math.toRadians(0);
               break;
            case RIGHT:
                rotate = Math.toRadians(180);
               break;
            case UP:
                rotate = Math.toRadians(-90);
               break;
            case DOWN:
                rotate = Math.toRadians(90);
               break;
            case UP_LEFT:
                 rotate = Math.toRadians(-90);
               break;
            case DOWN_LEFT:
                 rotate = Math.toRadians(90);
               break;
            case UP_RIGHT:
                 rotate = Math.toRadians(-90);
               break;
            case DOWN_RIGHT:
                 rotate = Math.toRadians(90);
               break;
                
        }
        
        g2d.rotate(rotate, x + size/2, y + size/2); // rotates graphics
        g2d.drawImage(body, x, y, size, size, null);//draws image with rotated graphics
        g2d.rotate(-rotate, x + size/2, y + size/2); //rotates graphics back
        //g2d.draw(top);
       // g2d.draw(bottom);
        //g2d.draw(left);
        //g2d.draw(right);
        
    }

    public int getSize() {
        return size;
    }

   


    
    
    
    
}
