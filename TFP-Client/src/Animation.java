
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jeff
 */
public class Animation {

    private final BufferedImage[] imgs;
    private final int speed, timesToRun;
    private int index, tickCounter, timesRun;
    private BufferedImage currentImage;
    private boolean complete;

    public Animation(BufferedImage imgs[], int speed, int timesToRun) {
        this.imgs = imgs;
        this.speed = speed;
        this.timesToRun = timesToRun;
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
        if (tickCounter % speed == 0 && timesRun < timesToRun) { // Check if tick delay && times run has been reached
            if (index == imgs.length - 1) {
                index = 0; // If at last image, loop to 0
                timesRun++;
            } else {
                index++;
            }
            currentImage = imgs[index];
        }
        if(timesRun >= timesToRun) complete = true;
    }

    public void tick() {
        tickCounter++;
        nextAnimation();
    }

    public void render(Graphics g, double x, double y, int size) {
        g.drawImage(currentImage, (int) x, (int) y, size, size, null);
    }

    public boolean isComplete() {
        return complete;
    }
    
    
}
