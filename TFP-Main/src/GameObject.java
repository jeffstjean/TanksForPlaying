/*
* This is the main parent class for all Objects visible on the screen. It has 
* abstract methods for render and tick and final methods for getters. It has 2
* different Constructors that can either make objects that are square or rectangular. 
*/



import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;


public abstract class GameObject {
    
	protected double x, y, height ,width; // is the x y width and height of the GameObject
	protected ID id; // is the enum ID of the GameObject
	public double motionX, motionY; // is the motion of the GameObject
	protected Rectangle2D bounds; // bounds rectangle for collision
        protected boolean intersecting; // is the object colliding with another
        protected int aliveForTicks; // how long has this object existed
        private static int totalObjects = 0; // totalObjects created
        
        // finals for reseting screen
        protected final double xF, yF; 
	public final double motionXF, motionYF; 
	protected final Rectangle2D boundsF;
        
        
	public GameObject(double x, double y, double width, double height, ID id){
		this.x = x;
		this.y = y;
		this.id = id;
                this.height = height;
                this.width = width;
                bounds = new Rectangle.Double(x,y, width, height);
                intersecting = false;
                aliveForTicks = 0;
                totalObjects ++; //increments totalObjects
                
                this.xF = x;
		this.yF = y;
                motionXF = 0;
                motionYF = 0;
                boundsF = new Rectangle.Double(x,y, width, height);
	}
        
        public GameObject(double x, double y, double width,  ID id){
		this.x = x;
		this.y = y;
		this.id = id;
                this.height = width;
                this.width = width;
                bounds = new Rectangle.Double(x,y, width, height);
                
                this.xF = x;
		this.yF = y;
                motionXF = 0;
                motionYF = 0;
                boundsF = new Rectangle.Double(x,y, width, height);
                
                
                
                intersecting = false;
                aliveForTicks = 0;
                totalObjects ++; //increments totalObjects
                
	}
        

	public abstract void tick();
	
	
	
        //gets totalObjects
        public static int getTotalObjects() {
            return totalObjects;
        }
	
        // getsX
	public final double getX(){
		return x;
	}
	
        //gets Y
	public final double getY(){
		return y;
	}
	
	
	public ID getID(){ // gets ID
		return id;
	}
        
        // getters and setter for motion
	public void setmotionX(double motionX){
		this.motionX = motionX;
	}
        
        
	public void setmotionY(double motionY){
		this.motionY = motionY;
	}
        
        
        
	public double getmotionX(){
		return motionX;
	}
        
	public double getmotionY(){
		return motionY;
	}
        
        public void collision(GameObject gO){
            
        }
        
        // forces all other classes to be able to extend 
	public abstract void render(Graphics g) ;
	
        // resets the object to initial values
	public void reset(){
            motionX = motionXF;
            motionY = motionYF;
            
            bounds = boundsF;
            x = xF;
            y = yF;
        }
}