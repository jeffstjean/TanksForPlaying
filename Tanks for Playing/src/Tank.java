
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Tank extends GameObject {

    private Rectangle bounds;
    private int size = 64;
    private int speed = 2;
    private Game game;
    private BufferedImage body;
private double rotate;
    private enum moveDirection {
        UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT, NONE,
    };

    private moveDirection moveDir = moveDirection.NONE;

    public Tank(int x, int y, ID id, Game g) {
        super(x, y, id);
        bounds = new Rectangle(x, y, size, size);
        game = g;
        rotate = 0;
        File file = new File("./test.txt");
        PrintWriter writer;
        try {
            writer = new PrintWriter(file);
            writer.println("test");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        body = ImageLoader.imageLoader("./graphics/TankGreen.png");

    }

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

        x += motionX;
        y += motionY;
        bounds.setLocation(x, y);

    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.draw(bounds);
        
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
        g2d.rotate(rotate, x + size/2, y + size/2);
                g2d.drawImage(body, x, y, size, size, null);
        g2d.rotate(-rotate, x + size/2, y + size/2); 
    }

    public int getSize() {
        return size;
    }

}
