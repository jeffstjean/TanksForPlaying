
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
public class Mine extends GameObject {

    private final File bomb = new File("./graphics/bomb.png"), bombFlash = new File("./graphics/bombFlash.png");
    private final int size;
    private final BufferedImage[] imgs;
    private Animation flash;
    private boolean explosionSequence, exploded, render;

    public Mine(int x, int y, int width, int height, ID id) {
        super(x, y, width, height, id);
        motionX = 0;
        motionY = 0;
        size = width;
        imgs = new BufferedImage[2];
        imgs[0] = ImageLoader.imageLoader(bomb.getPath());
        imgs[1] = ImageLoader.imageLoader(bombFlash.getPath());
        explosionSequence = false;
        exploded = false;
        render = true;
    }

    @Override
    public void tick() {
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.draw(bounds);
        if (render) {
            if (explosionSequence) {
                flash.drawAnimation(g, x, y, size);
            } else {
                g.drawImage(ImageLoader.imageLoader(bomb.getPath()), (int) x, (int) y, size, size, null);
            }
            if (flash != null) {
                if (!flash.isRunning() && explosionSequence) {
                    explode(g);
                }
            }
        }
        else {
            g.drawImage(ImageLoader.imageLoader("./graphics/TankGreen.png"), (int) x, (int) y, size, size, null);
        }
    }

    public void startExplosionSequence() {
        explosionSequence = true;
        flash = new Animation(imgs, 200, 20);
        flash.animate();
    }

    private void explode(Graphics g) {
        if (!exploded) {
            exploded = true;
            render = false;
            System.out.println("BOOM");
        }
    }

}
