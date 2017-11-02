
import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class Animation {

    private final BufferedImage[] imgs; 
    private final int speed, timesToRun;
    private int index, tickCounter, timesRun;
    private BufferedImage currentImage;
    private boolean complete, runForever;

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
        if(timesRun >= timesToRun) complete = true; // This boolean has public access so other classes know when the animation has completed
    }

    public void tick() {
        tickCounter++; // Keeping track of how many ticks have passed
        nextAnimation(); // Switch to the next image in the animations
    }

    public void render(Graphics g, double x, double y, int size) {
        g.drawImage(currentImage, (int) x, (int) y, size, size, null); // Draw the image
    }

    public boolean isComplete() { // Boolean is set to true to let other classes know the animation is complete
        return complete;
    }
    
    
}
