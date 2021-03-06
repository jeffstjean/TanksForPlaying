
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;


public class Explosion extends GameObject{
    
    private final File explosion = new File("src/resources/graphics/explosionSheet.png");
    private final BufferedImage[] imgs;
    private Animation explosionAnimation;
    private boolean animationComplete;
    private final boolean debug = false; // Set to true to see collison boxes
    private final Handler h;
    public Explosion(double x, double y, double width, double height, ID id, Handler h) {
        super(x, y, width, height, id);
        imgs = SpriteSheetReader.getSprites(9, 9, 100, ImageLoader.imageLoader(explosion.getPath())); // Load from spreadsheet
        explosionAnimation = new Animation(imgs, 1, 1); // Start a new Animation, every tick, only once
        h.addObject(this); // Pass this class to the handler to add
        animationComplete = false; // Let other classes know that the animation has not completed
        this. h = h;
    }

    @Override
    public void tick() {

        if (explosionAnimation != null) { // Only tick the Animation if the Animation has been instatiated
            explosionAnimation.tick();
            if (explosionAnimation.isComplete() && !animationComplete) { //If Animation has completed... (Second bool only allows the code to be run once)
                animationComplete = true; // Stop this code from being run twice because tick() is continually run
                explosionAnimation = null; // Dispose of the now unused Animation class
                h.removeObject(this);
            }
        }
    }

    @Override
    public void render(Graphics g) {
        
        if(explosionAnimation!=null) {
            //if(debug) g.drawOval(x, y, width, height); // Draw the collison box of the explosion
            explosionAnimation.render(g, x, y, width); // Render the image
        }
    }

    public boolean isAnimationComplete() { // Lets other classes know that the animation has been completed
        return animationComplete;
    }

    @Override
    public void reset() {
         Game.game.handler.removeObject(this);
    }
    
    
    
    
    
}
