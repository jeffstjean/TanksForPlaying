
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;



public class Wall extends GameObject{

    
    private int health = 5, hits=0;
    public Wall(int x, int y, int width, int height, ID id) {
        super(x, y, width, height, id);
        motionX = 0;
        motionY = 0;
        
    }



    @Override
    public void tick() {
        if(hits >= health){
            Game.handler.removeObject(this);
        }
    }

    

    @Override
    public void collision(GameObject gO) {
        if(id == ID.BreakableWall){
            hits++;
            System.out.println("Hit");
        }
    }
    
}
