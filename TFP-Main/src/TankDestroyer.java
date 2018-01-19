
public class TankDestroyer extends Tank{
    
    public TankDestroyer(double x, double y, double width, double height, ID id, Game g, int num) {
        super(x, y, width, height, id, g, num);
        speed = 1;
        
        tankClass = TankClasses.Destroyer;
    }

    @Override
    public String toString() {
        return "Destroyer";
    }
    
    
    
}
