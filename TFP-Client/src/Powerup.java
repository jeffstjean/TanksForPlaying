
import java.awt.Graphics;
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
public class Powerup extends GameObject{
    
    private final Handler h;
    private final BufferedImage[] imgs;
    private int type;
    private Animation animation;
    private Explosion explosion;
    private boolean animationComplete;
    
    public Powerup(int x, int y, int width, int height, ID id, Handler h, int type) {
        super(x, y, width, height, id);
        this.h = h; // Need the handler to add Explosion from within Mine
        this.type = type;
        motionX = 0; // No motion is needed
        motionY = 0;
        imgs = new BufferedImage[6];
        String fileLocation = "./graphics/powerup/";
        switch(type) {
            case(0):
                fileLocation+="blue";
                break;
            case(1):
                fileLocation+="green";
                break;
            case(2):
                fileLocation+="red";
                break;
            default:
                fileLocation+="yellow";
                break;
        }
        fileLocation+="/frame";
        File loc = new File(fileLocation + "1");
        //imgs[0] = ImageLoader.imageLoader("./graphics/powerup/blue/frame1");
        imgs[0] = ImageLoader.imageLoader("./graphics/bomb.png");

        //for (int i = 0; i < imgs.length; i++) {
        //    imgs[i] = ImageLoader.imageLoader(fileLocation + (i+1));
        //    System.out.println(fileLocation + (i+1));
        //}
        animationComplete = false;
        //animation = new Animation(imgs, 2, true);
    }

    @Override
    public void tick() {
        
    }

    @Override
    public void render(Graphics g) {
        
    }
}
