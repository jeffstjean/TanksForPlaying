
import java.awt.Graphics;
import java.util.LinkedList;

public class Handler {

    LinkedList<GameObject> object = new LinkedList<GameObject>();

    public void tick() {
        for (int i = 0; i < object.size(); i++) {
            GameObject temp1 = object.get(i);
            temp1.tick();
            if(temp1.id == ID.Tank){
            for (int j = 0; j < object.size(); j++) {
                GameObject temp2 = object.get(j);
                if(!(temp2 == temp1) && (temp2.id != ID.Turret) && (temp2.id != ID.Bullet)){
                    if (temp2.bounds.intersects(temp1.bounds)){
                        temp1.intersecting = true;
                       
                    }else{
                        temp1.intersecting = false;
                    }
                }
            }
            
            }
            
            temp1.x += temp1.motionX;
            temp1.y += temp1.motionY;

        }
    }

    public void render(Graphics g) {

        for (int i = 0; i < object.size(); i++) {
            GameObject tempObject = object.get(i);

            tempObject.render(g);
        }
    }

    public void addObject(GameObject object) {
        this.object.add(object);

    }

    public void removeObject(GameObject object) {
        this.object.remove(object);

    }
}
