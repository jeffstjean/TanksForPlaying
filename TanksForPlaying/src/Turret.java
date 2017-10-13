
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Turret extends GameObject{

    private Tank tank;
    private Rectangle bounds;
    private Graphics2D g2d;
    private double xd,yd,rotate;
    private double mouseX, mouseY;
    public Turret(int x, int y, ID id, Tank t) {
        super(x, y, id);
        tank = t;
        bounds = new Rectangle(x,y, 10, 10);
    }

    @Override
    public void tick() {
        mouseX = tank.getGame().getMouseX();
        mouseY = tank.getGame().getMouseY();
        xd = (double) x;
        yd = (double ) y;
        bounds.setLocation(x, y);
        rotate = Math.pow(Math.sin(Math.abs((mouseX - xd)) / Math.abs((mouseY - yd))), -1);
    }

    @Override
    public void render(Graphics g) {
        g2d = (Graphics2D) g;
        g2d.rotate(Math.toRadians(rotate), xd,yd);
        g2d.fill(bounds);
       // g2d.rotate(- Math.toRadians(rotate), xd,yd);
        
    }
    
}
