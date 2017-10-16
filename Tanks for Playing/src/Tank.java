
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;


public class Tank extends GameObject{

    private Rectangle bounds;
    private  int size = 64;
    private int  speed = 2;
    private Game game;
    public Tank(int x, int y, ID id, Game g) {
        super(x, y, id);
        bounds = new Rectangle(x,y,size, size);
        game = g;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void tick() {
        if(Key.up.isDown) motionY = -speed;
        else if(Key.down.isDown) motionY = speed;
        else motionY = 0;
        if(Key.left.isDown) motionX = -speed;
        else if(Key.right.isDown) motionX = speed;
        else motionX = 0;
        x += motionX;
        y += motionY;
        bounds.setLocation(x, y);
    }

    @Override
    public void render(Graphics g) {
       Graphics2D g2d = (Graphics2D) g;
       g2d.draw(bounds);
       
    }
    
}
