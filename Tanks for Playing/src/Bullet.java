
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject {

    private Rectangle bounds;
    private final int size = 64;
    private final int speed = 2;
    private Game game;
    private BufferedImage body;
    private double rotate;

    public Bullet(int x, int y, ID id, Game g) {
        super(x, y, id);
        bounds = new Rectangle(x, y, size, size);
        game = g;
        rotate = 0;

    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {

    }

}
