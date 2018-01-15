import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;


public abstract class GameObject {
	protected double x, y, height ,width;
	protected ID id;
	public double motionX, motionY;
	protected Rectangle2D bounds;
        protected boolean intersecting;
        protected int aliveForTicks;
        private static int idNum = 0;
	public GameObject(double x, double y, double width, double height, ID id){
		this.x = x;
		this.y = y;
		this.id = id;
                this.height = height;
                this.width = width;
                bounds = new Rectangle.Double(x,y, width, height);
                intersecting = false;
                aliveForTicks = 0;
                idNum ++;
	}
        
        public GameObject(double x, double y, double width,  ID id){
		this.x = x;
		this.y = y;
		this.id = id;
                this.height = width;
                this.width = width;
                bounds = new Rectangle.Double(x,y, width, height);
                intersecting = false;
                aliveForTicks = 0;
                idNum ++;
	}
        

	public abstract void tick();
	
	public void setX(double x ){
		this.x = x;
	}
	public void setY(double y){
		this.y = y;
	}

    public static int getIdNum() {
        return idNum;
    }
	
	public final double getX(){
		return x;
	}
	
	public final double getY(){
		return y;
	}
	
	public void setID(ID id){
		this.id = id;
	}
	public ID getID(){
		return id;
	}
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
            //gO.collision(this);
        }
        
	public abstract void render(Graphics g) ;
	
	public void reset(){
            
        }
}