
import java.awt.Graphics;
import java.awt.Graphics2D;



public class Wall extends GameObject{

    public Wall(int x, int y, int width, int height, ID id) {
        super(x, y, width, height, id);
        motionX = 0;
        motionY = 0;
        
    }



    @Override
    public void tick() {
        
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.fill(bounds);
        
    }
    
}
