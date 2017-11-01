
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jeff4821
 */
public final class Mine extends GameObject {

    private final File bomb = new File("./graphics/bomb.png"), bombFlash = new File("./graphics/bombFlash.png");
    private final BufferedImage[] imgs;
    private Animation a;
    private Explosion explosion;
    private boolean animationComplete, allAnimationsComplete;
    private final double EXPLOSION_SIZE_FACTOR;
    private Handler h;

    public Mine(int x, int y, int width, int height, ID id, Handler h, double explosionSizeRelativeToMine) {
        super(x, y, width, height, id);
        this.h = h;
        motionX = 0;
        motionY = 0;
        imgs = new BufferedImage[2];
        imgs[0] = ImageLoader.imageLoader(bomb.getPath());
        imgs[1] = ImageLoader.imageLoader(bombFlash.getPath());
        animationComplete = false;
        startCountdown();
        this.EXPLOSION_SIZE_FACTOR = explosionSizeRelativeToMine;
    }

    @Override
    public void tick() {
        if (a != null) {
            a.tick();
            if (a.isComplete() && !animationComplete) {
                animationComplete = true;
                startExplosion();
                a = null;
                allAnimationsComplete = true;
            }
        }
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (a != null) {
            a.render(g, x, y, width);
        }

    }

    public void startCountdown() {
        a = new Animation(imgs, 10, 5);
    }

    public void startExplosion() {
        explosion = new Explosion(x - (int)(((width*EXPLOSION_SIZE_FACTOR)-width)/2), y - (int)(int)(((height*EXPLOSION_SIZE_FACTOR)-height)/2), (int)(width * EXPLOSION_SIZE_FACTOR), (int)(height * EXPLOSION_SIZE_FACTOR), id, h);
    }

    public boolean isAllAnimationsComplete() {
        return allAnimationsComplete;
    }
    
    
}
