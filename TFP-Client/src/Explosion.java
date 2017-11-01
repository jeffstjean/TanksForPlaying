
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;


public class Explosion extends GameObject{
    
    private final File explosion = new File("./graphics/explosionSheet.png");
    private final BufferedImage[] imgs;
    private Animation explosionAnimation;
    private boolean explosionAnimationHasStarted, animationComplete;
    
    public Explosion(int x, int y, int width, int height, ID id, Handler h) {
        super(x, y, width, height, id);
        imgs = SpriteSheetReader.getSprites(9, 9, 100, ImageLoader.imageLoader(explosion.getPath()));
        explosionAnimation = new Animation(imgs, 1, 1);
        h.addObject(this);
        animationComplete = false;
    }

    @Override
    public void tick() {
        if(explosionAnimation!=null) explosionAnimation.tick();
        if (explosionAnimation != null) {
            explosionAnimation.tick();
            if (explosionAnimation.isComplete() && !animationComplete) {
                animationComplete = true;
                explosionAnimation = null;
            }
        }
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if(explosionAnimation!=null) explosionAnimation.render(g, x, y, width);
    }
}
