/**
* The parent class for all tank objects, also the class for any default tank
* Has a whole bunch of code, extends GameObject and is extended by the other tank classes
* has a render method and a tick method that get called to render and tick
*/
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Tank extends GameObject {

    protected double size = 64;
    protected int speed = 2;
    protected final Game game;
    protected final BufferedImage body;
    private double rotate;
    private final Rectangle2D top, bottom, left, right;
    protected int coolDown = 100, coolDownCounter = 100; 
    private int playerNum;
    protected int health = 100;
    public TankClasses tankClass;
    boolean canPlaceMine = true;
    boolean canShoot = true;
    /**
     * main constructor
    */
    public Tank(double x, double y, double width, double height, ID id, Game g, int num) {
        super(x, y, width, height, id);
        game = g;
        rotate = 0;
        size = width;
        // sets collision boxes
        top = new Rectangle2D.Double(x + 10, y - 10, size - 20, 10); 
        bottom = new Rectangle2D.Double(x + 10, y + size, size - 20, 10);
        left = new Rectangle2D.Double(x - 10, y + 10, 10, size - 20);
        right = new Rectangle2D.Double(x + size, y - 10, 10, size - 20);
        // sets the tank image to the colour of the player number
        if(num == 1)

        body = ImageLoader.imageLoader("src/resources/graphics/TankGreen.png");

        else 
           body = ImageLoader.imageLoader("src/resources/graphics/TankRed.png"); 
        playerNum = num;
        tankClass = TankClasses.Normal;
        canPlaceMine = true;
    }
    /**
     * quick access default constructor with default values
     */
    public Tank(Game g, int num) {
        super(100, 100, 64, 64, ID.Tank);
        game = g;
        rotate = 0;
        size = width;
        top = new Rectangle2D.Double(x + 10, y - 10, size - 20, 10);
        bottom = new Rectangle2D.Double(x + 10, y + size, size - 20, 10);
        left = new Rectangle2D.Double(x - 10, y + 10, 10, size - 20);
        right = new Rectangle2D.Double(x + size, y - 10, 10, size - 20);
        body = ImageLoader.imageLoader("src/resources/graphics/TankGreen.png");
        playerNum = num;
        tankClass = TankClasses.Normal;
    }
/** 
 * enum of which direction the tank is faceing/moving
*/
    public enum moveDirection {
        NONE, UP, UP_RIGHT, RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT,
    };

    private moveDirection moveDir = moveDirection.NONE;
/** 
 * gets Game from tank
 * the tanks instance of game
 */
    public Game getGame() {
        return game;
    }
/**
 * tosrting that returns the tank Class name
 */
    @Override
    public String toString() {
        return "Balanced";
    }

    /**
     * gets run every tick
     */
    @Override
    public void tick() {
        // tick, gets run every tick
        
        //when the tank dies run this
        if (health <= 0) {
            canPlaceMine = false; // stops it from placing more mines
            canShoot =  false;
            Game.game.handler.reset(); // resets everything
            WinningScreen screen; 
            if (playerNum == 1 ) 
                screen = new WinningScreen(Game.frame, true, 2); // wakes a winning screen with the corect player number 
            else
                screen = new WinningScreen(Game.frame, true, 1);
            screen.setVisible(true); // sets the winning screen to visible
        }

   
        if(playerNum == 1){
            
            //move up  and down player 1
        if (Key.up1.isDown) {
            motionY = -speed;
        } else if (Key.down1.isDown) {
            motionY = speed;
        } else {
            motionY = 0;
        }
        
        
        // move left and right player 1
        if (Key.left1.isDown) {
            motionX = -speed;
        } else if (Key.right1.isDown) {
            motionX = speed;
        } else {
            motionX = 0;
        }
        
        // plave mine player 1
        coolDownCounter++;
        if(Key.mine1.isDown && coolDownCounter >= coolDown){
            if(canPlaceMine){
            dropMine();
            coolDownCounter = 0;
            }
        }
        
        }else{
            
            
            //up and down player 2
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
        }else {
            motionX = 0;
        }

        
        // place mine player 2
            coolDownCounter++;
            if (Key.mine2.isDown && coolDownCounter >= coolDown) {
                if(canPlaceMine){
                dropMine();
                coolDownCounter = 0;
                }
            }


        
            
        }
        
        

        // sets movement based on motion pressed
        if (motionX == 0 && motionY == 0)
            moveDir = moveDirection.NONE;
        else if (motionX < 0 && motionY == 0)
            moveDir = moveDirection.LEFT;
        else if (motionX > 0 && motionY == 0)
            moveDir = moveDirection.RIGHT;
        else if (motionX == 0 && motionY < 0)
            moveDir = moveDirection.UP;
        else if (motionX == 0 && motionY > 0)
            moveDir = moveDirection.DOWN;
        else if (motionX < 0 && motionY < 0)
            moveDir = moveDirection.UP_LEFT;
        else if (motionX < 0 && motionY > 0)
            moveDir = moveDirection.DOWN_LEFT;
        else if (motionX > 0 && motionY < 0)
            moveDir = moveDirection.UP_RIGHT;
        else if (motionX > 0 && motionY > 0)
            moveDir = moveDirection.DOWN_RIGHT;

        //moves the rectangle to the new X and Y
        bounds.setRect(x, y, size, size);
        top.setRect(x + 10, y - 10, size - 20, 10);
        bottom.setRect(x + 10, y + size, size - 20, 10);
        left.setRect(x - 10, y + 10, 10, size - 20);
        right.setRect(x + size, y + 10, 10, size - 20);


    }
/**
 * getter for player num
 * returns the player num
 */
    public int getPlayerNum() {
        return playerNum;
    }

/**
 * gets run every time the screen repaints
 * allows the handler to render the tank
 */
    @Override 
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
      // sets the amount that the image has too rotate 
        switch (moveDir) {
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
        t.scale(size / body.getWidth(), size / body.getHeight());
        g2d.rotate(rotate, x + size / 2, y + size / 2); // rotates graphics
        g2d.drawImage(body, t, null);//draws image with rotated graphics
        g2d.rotate(-rotate, x + size / 2, y + size / 2); //rotates graphics back

        g2d.drawString("" + health, (float) x, (float) y - 20);
        

    }

    public double getSize() {
        return size;
    }

    public moveDirection getMoveDir() {
        return moveDir;
    }

    public void reduceHealth(int n) {
        health -= n;
    }

    public int getHealth() {
        return health;
    }

    /** 
     * is run when it collides with another gameObject
     */
    @Override 
    public void collision(GameObject gO) {
        // sets the motion based on the side that is being collided with
        if (top.intersects(gO.bounds))

            if (motionY < 0)
                motionY = 0;
        if (bottom.intersects(gO.bounds))

            if (motionY > 0)
                motionY = 0;
        if (right.intersects(gO.bounds))

            if (motionX > 0)
                motionX = 0;
        if (left.intersects(gO.bounds))

            if (motionX < 0)
                motionX = 0;
    }
    /**
     * sets the canPlaceMine property of tank
     * @param canPlaceMine 
     */
    public void setCanPlaceMine(boolean canPlaceMine) {
        this.canPlaceMine = canPlaceMine;
    }
/**
     * sets the canShoot property of tank
     * @param canShoot 
     */
    public void setCanShoot(boolean canShoot) {
        this.canShoot = canShoot;
    }

    /**
     * resets the tank to original values
     */
    @Override 
    public void reset() {
        super.reset();
        health = 100;
       
    }
    /**
     * drops a mine
     */
    protected void dropMine() {
        Game.handler.addObject(new Mine(x, y, 16, 16, ID.Mine, Game.handler));
    }
    


}
