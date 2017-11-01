
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

    private final File bomb = new File("./graphics/bomb.png"), bombFlash = new File("./graphics/bombFlash.png"), explosion = new File("./graphics/explosion.png"), explosionSheet = new File("./graphics/explosionSheet.png");
    private final BufferedImage[] imgs;
    private Animation a;

    public Mine(int x, int y, int width, int height, ID id) {
        super(x, y, width, height, id);
        motionX = 0;
        motionY = 0;
        imgs = new BufferedImage[2];
        imgs[0] = ImageLoader.imageLoader(bomb.getPath());
        imgs[1] = ImageLoader.imageLoader(bombFlash.getPath());
    }

    @Override
    public void tick() {
        if(a!=null) System.out.println(a.isDone());
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if(a!=null) a.drawAnimation(g, x, y, width);
        
    }
    
    public void startExplosionSequence() {
        a = new Animation(imgs, 200, 10);
        a.animate();
    }
}
