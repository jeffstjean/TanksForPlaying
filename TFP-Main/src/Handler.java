/*
* This class handles all objects in the game. It has  List of GameObjects that it 
* iterates through and runs their tick method. It then checks for collision between
* objects and finally moves all objects.
* It renders all objects in a similar way starting at the top of the list and working its way down.
*/



import java.awt.Graphics;
import java.util.LinkedList;

public class Handler {

    static LinkedList<GameObject> object = new LinkedList<GameObject>(); // list of all objects that are in use

    // gets run every tick
    public void tick() {
        for (int i = 0; i < object.size(); i++) {
            GameObject temp1 = object.get(i); 
            temp1.tick(); // tick object over
            temp1.aliveForTicks ++; // increment how longit has existed
            if(temp1.id == ID.Tank || temp1.id == ID.Bullet || temp1.id == ID.Mine){ 
                // if the object is something that needs to check if collsion is occuring because of it
            for (int j = 0; j < object.size(); j++) {
                // iterates through all objects again
                GameObject temp2 = object.get(j);
               
                // if it is not the same object or a turret
                if(!(temp2 == temp1) && (temp2.id != ID.Turret)){
                    if (temp2.bounds.intersects(temp1.bounds)){ 
                        temp1.collision(temp2); // run collision 
                        temp1.intersecting = true;
                    }else{
                        temp1.intersecting = false;
                    }
                }
            }
            
            }
            
            temp1.x += temp1.motionX; // move object
            temp1.y += temp1.motionY;

        }
    }
    
    public void render(Graphics g) {

        for (int i = 0; i < object.size(); i++) {
            GameObject tempObject = object.get(i);
            // iterate and then render the object
            tempObject.render(g);
        }
    }

    public void addObject(GameObject object) {
        this.object.add(object); // add an object to the bottom of the list

    }

    public void reset(){
         for (int i = 0; i < object.size(); i++) {
            GameObject temp1 = object.get(i);
            // rest all objects to original position
            temp1.reset();
            if(temp1.id == ID.Mine) removeObject(temp1);
         }
    }
    
    
    public void removeObject(GameObject object) {
        this.object.remove(object); 
        // remove an object from the list
        object = null;
    }
    
    
    
}
