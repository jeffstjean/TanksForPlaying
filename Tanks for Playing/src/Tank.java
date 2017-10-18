
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

    public Tank(int x, int y, int width, int height, ID id, Game g) {
        super(x, y, width, height, id);
        game = g;
        rotate = 0;

        body = ImageLoader.imageLoader("./graphics/TankGreen.png");

    
    
    
    
    }
    
    
    private enum moveDirection {
        UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT, NONE,
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
            motionX = -motionX;
            motionY = - motionY;
        }
        // moves tank and rectangle
        bounds.setLocation(x, y);
        
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.draw(bounds);
        // draws rectangle
        g2d.drawImage(body, x, y, size, size, null);
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
    }

    public int getSize() {
        return size;
    }
    
    
    
    
}
