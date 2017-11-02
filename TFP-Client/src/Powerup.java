
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jeff4821
 */
public class Powerup extends GameObject {

    private final Handler h;
    private final BufferedImage[] imgs;
    private PowerupColor type;
    private Animation animation;
    private boolean animationComplete;
    private int tickCounter;

    public Powerup(int x, int y, int width, int height, ID id, Handler h, PowerupColor type) {
        super(x, y, width, height, id);
        this.h = h; // Need the handler to add Explosion from within Mine
        this.type = type;
        motionX = 0; // No motion is needed
        motionY = 0;
        tickCounter = 0;
        imgs = new BufferedImage[6];
        String fileLocation = "./graphics/powerup/";
        switch (type) {
            case Blue:
                fileLocation += "blue";
                break;
            case Green:
                fileLocation += "green";
                break;
            case Red:
                fileLocation += "red";
                break;
            case Yellow:
                fileLocation += "yellow";
                break;
            default:
                fileLocation += "blue";
                break;
        }
        fileLocation += "/frame";
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = ImageLoader.imageLoader(fileLocation + (i + 1) + ".png");
        }
        animationComplete = false;
        animation = new Animation(imgs, 7, true);
    }

    @Override
    public void tick() {
        tickCounter++;
        if (animation != null) { // Animation may not have been instatiated, don't do anything until it has been 
            if(tickCounter > 400) animation.stop();
            animation.tick(); // Tick the animation
            if (animation.isComplete() && !animationComplete) { // Check if Animation is done, second bool only allows the method to be run once
                animationComplete = true; // Stop method from being run more than once since tick() is continually called
                animation = null; // Dispose of now unused Animation object
            }
        }
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (animation != null) { // Animation may not have been instatiated, don't do anything until it has been 
            animation.render(g, x, y, width); // Render the image
        }
    }

    public void destroy() {
        animation.stop();
        animation = null;
        animationComplete = true;
    }

    public boolean isAnimationComplete() {
        return animationComplete;
    }
}
