
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;


public class Animation {

    private final int speed, timesRun;
    private int count;
    private boolean running;
    private BufferedImage currentImg;
    private LinkedList<BufferedImage> imgs;
    private Timer timer;
    BufferedImage test = ImageLoader.imageLoader("./graphics/bomb.png");

    public Animation(BufferedImage[] imgs, int speed, int timesRun) {
        this.imgs = new LinkedList<>();
        this.imgs.addAll(Arrays.asList(imgs));
        running = true;
        this.speed = speed;
        this.timesRun = timesRun / speed;
    }

    public void animate() {
        timer = new Timer();
        count = 0;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (count < timesRun && running) {
                    //currentImg = imgs.get(count % imgs.size());
                    currentImg = imgs.get(0);
                    count++;
                }
            }
        }, 0, speed);
    }
    
    public void drawAnimation(Graphics g, double x, double y, int size){
		g.drawImage(test, (int)x, (int)y, size, size, null);
                //g.drawImage(ImageLoader.imageLoader("./graphics/bomb.png"), (int)x, (int)y, size, size, null);
                
	}
    
    public void stopAnimation() {
        running = false;
    }

    public BufferedImage getCurrentImg() {
        return currentImg;
    }
    
    
}
