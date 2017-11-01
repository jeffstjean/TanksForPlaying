
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class AnimationOld {

    private final int speed, timesRun;
    private int count, index;
    private boolean running, done;
    private BufferedImage currentImg;
    private final BufferedImage imgs[];
    private Timer timer;
    BufferedImage test = ImageLoader.imageLoader("./graphics/bomb.png");

    public AnimationOld(BufferedImage[] imgs, int speed, int timesRun) {
        this.imgs = imgs;
        running = true;
        done = false;
        this.speed = speed;
        this.timesRun = timesRun;
        index = 0;
    }

    public void animate() {
        timer = new Timer();
        count = 0;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (count < timesRun && running) {
                    index = count % imgs.length;
                    count++;
                }
                else {
                    running = false;
                }
            }  
        }, 0, speed);
        done = true;
    }

    public void drawAnimation(Graphics g, double x, double y, int size) {
        currentImg = imgs[index];
        g.drawImage(currentImg, (int) x, (int) y, size, size, null);

    }

    public void stopAnimation() {
        running = false;
    }

    public BufferedImage getCurrentImg() {
        return currentImg;
    }

    public boolean isRunning() {
        return running;
    }
    
    public boolean isDone() {
        return done;
    }
    
    

}
