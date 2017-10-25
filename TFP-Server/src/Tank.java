
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
private int index;

    public Tank(int x, int y, int width, int height, ID id, Game g, int index) {
        super(x, y, width, height, id);
        game = g;
        rotate = 0;
        top = new Rectangle(x + 10,y - 10, size - 20, 10);
        bottom = new Rectangle (x + 10, y+size, size - 20, 10);
        left = new Rectangle (x - 10, y+10, 10, size -20);
        right = new Rectangle (x + size , y - 10, 10, size - 20);
       this.index = index;

    
    
    
    
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
        if (Server.game.key[index].up) {
            motionY = -speed;
        } else if (Server.game.key[index].down) {
            motionY = speed;
        } else {
            motionY = 0;
        }
        if (Server.game.key[index].left) {
            motionX = -speed;
        } else if (Server.game.key[index].right) {
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

    

    public int getSize() {
        return size;
    }

    public moveDirection getMoveDir() {
        return moveDir;
    }

    public void setPointing(int pointing){
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
    
    public int getIndex() {
        return index;
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
