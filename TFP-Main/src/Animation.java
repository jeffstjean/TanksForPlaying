
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;


public class Animation {

    private final BufferedImage[] imgs; // Holds images to animate
    private final int speed, timesToRun; // Allows control over speed and number of times to run animation
    private int index, tickCounter, timesRun; // index is where the animation is, tick counts the life of the instance of Animation
    private BufferedImage currentImage; // Image to display
    private boolean complete; // Sets to true when animation is complete
    private final boolean runForever; // Run a set number of time or forever

    public Animation(BufferedImage imgs[], int speed, int timesToRun) {
        this.imgs = imgs;
        this.speed = speed;
        this.timesToRun = timesToRun;
        this.runForever = false;
        tickCounter = 0;
        timesRun = 0;
        complete = false;
        if (imgs.length == 0) { // Incase the array is empty
            currentImage = ImageLoader.imageLoader("");
            index = -1;
        } else {
            currentImage = imgs[0];
            index = 0;
        }
    }
    
    
    public Animation(BufferedImage imgs[], int speed, boolean runForever) {
        this.imgs = imgs;
        this.speed = speed;
        this.timesToRun = 1;
        this.runForever = runForever;
        tickCounter = 0;
        timesRun = 0;
        complete = false;
        if (imgs.length == 0) { // Incase the array is empty
            currentImage = ImageLoader.imageLoader("");
            index = -1;
        } else {
            currentImage = imgs[0];
            index = 0;
        }
    }

    private void nextAnimation() {
        if (imgs.length < 0) {
            return; // No images were passed in
        }
        if (tickCounter % speed == 0 && (timesRun < timesToRun || runForever)) { // Check if tick delay && times run has been reached
            if (index == imgs.length - 1) { // If at last image..
                index = 0; // loop to 0
                timesRun++; // The animation has been run once
            } else {
                index++; // Just add one and go to the next image
            }
            currentImage = imgs[index]; // Update the currentImage
        }
        if(timesRun >= timesToRun && !runForever) complete = true; // This boolean has public access so other classes know when the animation has completed
    }

    public void tick() {
        tickCounter++; // Keeping track of how many ticks have passed
        nextAnimation(); // Switch to the next image in the animations
    }

    public void render(Graphics g, double x, double y, double size) {
        AffineTransform t = new AffineTransform();
        t.translate(x, y);
        t.scale(size/currentImage.getWidth(), size/currentImage.getHeight());
        Graphics2D g2D = (Graphics2D)g;
        g2D.drawImage(currentImage, t, null); // Draw the image
    }

    public boolean isComplete() { // Boolean is set to true to let other classes know the animation is complete
        return complete;
    }
    
    public void stop() {
        complete = true; // Stop the animation
    }
    
    
}
