
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

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
    public void tick() {
        if (Key.up.isDown) {
            motionY = -speed;
        } else if (Key.down.isDown) {
            motionY = speed;
        } else {
            motionY = 0;
        }
        if (Key.left.isDown) {
            motionX = -speed;
        } else if (Key.right.isDown) {
            motionX = speed;
        } else {
            motionX = 0;
        }
        // sets movement based on keys pressed
        if (motionX == 0 && motionY == 0) {
            moveDir = moveDirection.NONE;
        } else if (motionX < 0 && motionY == 0) {
            moveDir = moveDirection.LEFT;
        } else if (motionX > 0 && motionY == 0) {
            moveDir = moveDirection.RIGHT;
        } else if (motionX == 0 && motionY < 0) {
            moveDir = moveDirection.UP;
        } else if (motionX == 0 && motionY > 0) {
            moveDir = moveDirection.DOWN;
        } else if (motionX < 0 && motionY < 0) {
            moveDir = moveDirection.UP_LEFT;
        } else if (motionX < 0 && motionY > 0) {
            moveDir = moveDirection.DOWN_LEFT;
        } else if (motionX > 0 && motionY < 0) {
            moveDir = moveDirection.UP_RIGHT;
        } else if (motionX > 0 && motionY > 0) {
            moveDir = moveDirection.DOWN_RIGHT;
        }
        // sets enum to be the correct directional value
        if (intersecting){
            
            
            
            
            
//            switch (moveDir){
//               case NONE:
//               
//               break;
//            case LEFT:
//               x+= 10;
//               break;
//            case RIGHT:
//                x -= 10;
//               break;
//            case UP:
//                y += 10;
//               break;
//            case DOWN:
//               y -= 10;
//               break;
//            case UP_LEFT:
//                y +=10;
//                x += 10;
//               break;
//            case DOWN_LEFT:
//               y -= 10;
//               x += 10;
//               break;
//            case UP_RIGHT:
//                y +=10;
//                x -= 10;
//               break;
//            case DOWN_RIGHT:
//                y -=10;
//                x-= 10;
//               break;
//            }
        }
        // moves tank and rectangle
        bounds.setLocation(x, y);
        top.setBounds(x + 10,y - 10, size - 20, 10);
        bottom.setBounds(x + 10, y+size , size - 20, 10);
        left.setBounds(x-10, y+10, 10, size -20);
        right.setBounds(x + size , y + 10, 10, size - 20);
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
