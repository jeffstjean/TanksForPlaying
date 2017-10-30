
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public class Tank extends GameObject {

    private int size = 64;
    private int speed = 2;
    private Game game;
    private BufferedImage body;
private double rotate;
private Rectangle top, bottom, left, right;

    public Tank(int x, int y, int width, int height, ID id, Game g) {
        super(x, y, width, height, id);
        game = g;
        rotate = 0;
        size = width;
        top = new Rectangle(x + 10,y - 10, size - 20, 10);
        bottom = new Rectangle (x + 10, y+size, size - 20, 10);
        left = new Rectangle (x - 10, y+10, 10, size -20);
        right = new Rectangle (x + size , y - 10, 10, size - 20);
        body = ImageLoader.imageLoader("./graphics/TankGreen.png");

    
    
    
    
    }
    
    
    public enum moveDirection {
       NONE, UP, UP_RIGHT, RIGHT,  DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT,
    };

    private moveDirection moveDir = moveDirection.NONE;

    

    public Game getGame() {
        return game;
    }

     @Override
    public String toString() {
        return " x: " + x + " Y: " + y ;
     }
    
    
    @Override
    public void tick() {
       
       
        bounds.setLocation(x, y);
        top.setBounds(x + 10,y - 10, size - 20, 10);
        bottom.setBounds(x + 10, y+size , size - 20, 10);
        left.setBounds(x-10, y+10, 10, size -20);
        right.setBounds(x + size , y + 10, 10, size - 20);
    }

    
    public void setPointing(int i){
                switch (i) {
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
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        //g2d.draw(bounds);
        // draws rectangle
        //g2d.drawImage(body, x, y, size, size, null);
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
        // sets the rotation value based on direction
        g2d.rotate(rotate, x + size/2, y + size/2); // rotates graphics
        g2d.drawImage(body, x, y, size, size, null);//draws image with rotated graphics
        g2d.rotate(-rotate, x + size/2, y + size/2); //rotates graphics back
     //   g2d.draw(top);
       // g2d.draw(bottom);
        //g2d.draw(left);
        //g2d.draw(right);
        
    }

    public int getSize() {
        return size;
    }

    public moveDirection getMoveDir() {
        return moveDir;
    }

    @Override
    public void collision(GameObject gO) {
        if ( top.intersects(gO.bounds)){
            
            if (motionY < 0) motionY = 0;
        }if(bottom.intersects(gO.bounds)){
            
            if(motionY > 0) motionY = 0;
        } if (right.intersects(gO.bounds)){
           
            if(motionX > 0) motionX = 0;
        }if(left.intersects(gO.bounds)){
            
            if(motionX < 0) motionX = 0;
        }
    }
    
    
    
    
}
