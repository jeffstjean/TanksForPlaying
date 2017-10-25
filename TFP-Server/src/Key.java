public class Key{

   public Key up = new Key();
   public Key down = new Key();
   public Key left = new Key();
   public Key right = new Key();
   public Key shoot = new Key();

   
   
   public Key(){
   up = new Key();
   down = new Key();
   left = new Key();
   right = new Key();
   shoot = new Key();
}

   /* toggles the keys current state*/
   public void toggle(){
       isDown =  !isDown;
   }

   public boolean isDown;
}