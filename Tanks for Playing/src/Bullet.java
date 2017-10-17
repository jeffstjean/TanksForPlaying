
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject {

    private Rectangle bounds;
    private final int size = 15;
    private final int speed = 2;
    private Game game;
    private BufferedImage body;
    private double rotate;

    public Bullet(int x, int y, ID id, Game g) {
        super(x, y, id);
        bounds = new Rectangle(x, y, size, size);
        game = g;
        rotate = 0;
        body = ImageLoader.imageLoader("./graphics/Bullet.png"); 

    }
    
    public Game getGame() {
        return game;
    }

    @Override
    public void tick() {

    }


    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.draw(bounds);
        g2d.drawImage(body, x, y, size, size, null);
    }
    
    public int getSize() {
        return size;
    }

    public void shoot() {
        System.out.println("Shoot");
    }

}
