/*
 * Holder for all key bindings
*/

public class Key{

   public static Key up1 = new Key();
   public static Key down1 = new Key();
   public static Key left1 = new Key();
   public static Key right1 = new Key();
   public static Key shoot1 = new Key();
   public static Key mine1 = new Key();
   public static Key turretRight1 = new Key();
   public static Key turretLeft1 = new Key();
   
   
   
   public static Key up2 = new Key();
   public static Key down2 = new Key();
   public static Key left2 = new Key();
   public static Key right2 = new Key();
   public static Key shoot2 = new Key();
   public static Key mine2 = new Key();
   public static Key turretRight2 = new Key();
   public static Key turretLeft2 = new Key();
 
   
   public static Key pause = new Key();
   

   /* toggles the keys current state*/
   public void toggle(){
       isDown =  !isDown;
   }

   public boolean isDown;
}