
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


public class Tank extends GameObject {

    private double size = 64;
    private int speed = 2;
    private Game game;
    private BufferedImage body;
    private double rotate;
    private Rectangle2D top, bottom, left, right;
    private int coolDown = 20, coolDownCounter = 20; // should boh be 20
    private int playerNum;
    private int health = 100;
    
    
    
    public Tank(double x, double y, double width, double height, ID id, Game g, int num) {
        super(x, y, width, height, id);
        game = g;
        rotate = 0;
        size = width;
        top = new Rectangle2D.Double(x + 10,y - 10, size - 20, 10);
        bottom = new Rectangle2D.Double (x + 10, y+size, size - 20, 10);
        left = new Rectangle2D.Double (x - 10, y+10, 10, size -20);
        right = new Rectangle2D.Double (x + size , y - 10, 10, size - 20);
        body = ImageLoader.imageLoader("./graphics/TankGreen.png");
        playerNum = num;
    
    
    
    
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
        if(playerNum == 1){
        if (Key.up1.isDown) {
            motionY = -speed;
        } else if (Key.down1.isDown) {
            motionY = speed;
        } else {
            motionY = 0;
        }
        if (Key.left1.isDown) {
            motionX = -speed;
        } else if (Key.right1.isDown) {
            motionX = speed;
        } else {
            motionX = 0;
        }
        
        coolDownCounter++;
        if(Key.mine1.isDown && coolDownCounter >= coolDown){
            Game.handler.addObject(new Mine(x,y, 16,16, ID.Mine, Game.handler));
            coolDownCounter = 0;
        }
        
        }else{
            if (Key.up2.isDown) {
            motionY = -speed;
        } else if (Key.down2.isDown) {
            motionY = speed;
        } else {
            motionY = 0;
        }
        if (Key.left2.isDown) {
            motionX = -speed;
        } else if (Key.right2.isDown) {
            motionX = speed;
        } else {
            motionX = 0;
        }
        
        coolDownCounter++;
        if(Key.mine2.isDown && coolDownCounter >= coolDown){
            Game.handler.addObject(new Mine(x,y, 16,16, ID.Mine, Game.handler));
            coolDownCounter = 0;
        }        
        
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
        
        
        bounds.setRect(x, y, size, size);
        top.setRect(x + 10,y - 10, size - 20, 10);
        bottom.setRect(x + 10, y+size , size - 20, 10);
        left.setRect(x-10, y+10, 10, size -20);
        right.setRect(x + size , y + 10, 10, size - 20);
    }

    public int getPlayerNum() {
        return playerNum;
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
        AffineTransform t = new AffineTransform();
        t.translate(x, y);
        t.scale(size/body.getWidth(), size/body.getHeight());
        g2d.rotate(rotate, x + size/2, y + size/2); // rotates graphics
        g2d.drawImage(body, t, null);//draws image with rotated graphics
        g2d.rotate(-rotate, x + size/2, y + size/2); //rotates graphics back
        
        
        g2d.drawString("" + health, (float)x, (float)y-20);
     //   g2d.draw(top);
       // g2d.draw(bottom);
        //g2d.draw(left);
        //g2d.draw(right);
        
    }

    public double getSize() {
        return size;
    }

    public moveDirection getMoveDir() {
        return moveDir;
    }

    public void reduceHealth(int n){
        health -= n;
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
