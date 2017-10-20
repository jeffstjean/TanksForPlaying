import java.awt.Graphics;
import java.awt.Rectangle;


public abstract class GameObject {
	protected int x, y, height ,width;
	protected ID id;
	public int motionX, motionY;
	protected Rectangle bounds;
        protected boolean intersecting;
        protected int aliveForTicks;
	public GameObject(int x, int y, int width, int height, ID id){
		this.x = x;
		this.y = y;
		this.id = id;
                this.height = height;
                this.width = width;
                bounds = new Rectangle(x,y, width, height);
                intersecting = false;
                aliveForTicks = 0;
	}

	public abstract void tick();
	
	public void setX(int x ){
		this.x = x;
	}
	public void setY(int y){
		this.y = y;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setID(ID id){
		this.id = id;
	}
	public ID getID(){
		return id;
	}
	public void setmotionX(int motionX){
		this.motionX = motionX;
	}
	public void setmotionY(int motionY){
		this.motionY = motionY;
	}
	public int getmotionX(){
		return motionX;
	}
	public int getmotionY(){
		return motionY;
	}
        
        public void collision(GameObject gO){
            
        }
        
	public abstract void render(Graphics g) ;
	
	
}