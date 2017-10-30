
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
    private BufferedImage img;
    private final int maxTime = 240;
    private int counter = 0;

    public Mine(int x, int y, int width, int height, ID id) {
        super(x, y, width, height, id);
        motionX = 0;
        motionY = 0;
        size = width;
        img = ImageLoader.imageLoader(bomb.getPath());
    }

    @Override
    public void tick() {
        counter++;
        if(counter > maxTime / 2) img = ImageLoader.imageLoader(bombFlash.getPath());
        if (counter > maxTime) {
            System.out.println("BOOM");
            Game.handler.removeObject(this);
        }
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.draw(bounds);
        g2d.drawImage(img, x, y, size, size, null);
    }

}
