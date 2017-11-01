
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
    private int size = 32;
    private BufferedImage[] imgs;
    private final int maxTime = 240;
    private int counter = 0, tens = 0;
    private Animation a;

    public Mine(int x, int y, int width, int height, ID id) {
        super(x, y, width, height, id);
        imgs = new BufferedImage[2];
        motionX = 0;
        motionY = 0;
        size = width;
        imgs[0] = ImageLoader.imageLoader(bomb.getPath());
        imgs[1] = ImageLoader.imageLoader(bombFlash.getPath());
        a = new Animation(imgs, 500, 20);
        a.animate();
    }

    @Override
    public void tick() {
        
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.draw(bounds);
        a.drawAnimation(g, x, y, size);
    }

}
