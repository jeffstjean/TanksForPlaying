
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

public final class Mine extends GameObject {

    private final File BOMB_FILE_LOCATION = new File("./graphics/bomb.png"), BOMB_FLASH_FILE_LOCATION = new File("./graphics/bombFlash.png");
    private final double EXPLOSION_SIZE_FACTOR;
    private final Handler h;
    private final BufferedImage[] imgs;
    private Animation animation;
    private Explosion explosion;
    private boolean animationComplete, allAnimationsComplete;
    

    public Mine(double x, double y, double width, double height, ID id, Handler h) {
        super(x, y, width, height, id);
        this.h = h; // Need the handler to add Explosion from within Mine
        motionX = 0; // No motion is needed
        motionY = 0;
        imgs = new BufferedImage[2]; // Make an array to store the two images
        imgs[0] = ImageLoader.imageLoader(BOMB_FILE_LOCATION.getPath()); // The default bomb
        imgs[1] = ImageLoader.imageLoader(BOMB_FLASH_FILE_LOCATION.getPath()); // The flashing bomb
        animationComplete = false; // Animation isn't yet complete

        this.EXPLOSION_SIZE_FACTOR = Game.getIntUserPropertyThenDefault("explosionRealtiveSizeToMine", 4); // Allows for resizing of explosion
    }

    @Override
    public void tick() {
        

        if (animation != null) { // Animation may not have been instatiated, don't do anything until it has been 
            animation.tick(); // Tick the animation
            if (animation.isComplete() && !animationComplete) { // Check if Animation is done, second bool only allows the method to be run once
                animationComplete = true; // Stop method from being run more than once since tick() is continually called
                startExplosion(); // Start the explosion part
                animation = null; // Dispose of now unused Animation object
            }
        }
        if (explosion != null) {
            h.removeObject(this); // Let other classes know that all animations are complete
        }
    }

    @Override
    public void collision(GameObject gO) {
        if (gO.getID() != ID.Wall && aliveForTicks >= 20) {
            startExplosion();
            
        }
    }

    @Override
    public void render(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
       
        if (animation != null) { // Animation may not have been instatiated, don't do anything until it has been 
            animation.render(g, x, y, width); // Render the image
        }else{
            AffineTransform t = new AffineTransform();
        t.translate(x, y);
        t.scale(width/imgs[0].getWidth(), width/imgs[0].getHeight());
            g2d.drawImage(imgs[0], t, null);
        }

    }

    public void startCountdown() {
        animation = new Animation(imgs, 10, 5); // Create a new Animation with the images, run every 10 ticks, 5 times.
    }

    private void startExplosion() {
        if (animation != null) {
            animation.stop();
        }
        animation = null;
        // Complicated equations that allow you to resize the Explosion realtive to the Mine but still keep it centred
        // 10/10 would recommend not touching unless you want to fish out that scrap piece of paper from my garbage can with the equation
        if(explosion == null)
        explosion = new Explosion(x - (((width * EXPLOSION_SIZE_FACTOR) - width) / 2), y -  (((height * EXPLOSION_SIZE_FACTOR) - height) / 2), (width * EXPLOSION_SIZE_FACTOR),  (height * EXPLOSION_SIZE_FACTOR), id, h);
    }

    public boolean isAllAnimationsComplete() {
        return allAnimationsComplete; // Lets other classes know that the animations are complete and we can dispose of the object
    }

}
