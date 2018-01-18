
public class FastTank extends Tank{
    
    public FastTank(double x, double y, double width, double height, ID id, Game g, int num) {
        super(x, y, width, height, id, g, num);
        this.speed = 3;
        this.health = 80;
        tankClass = TankClasses.Fast;
    }

    @Override
    public void reset() {
        super.reset();
        health = 80;
    }
    
}
