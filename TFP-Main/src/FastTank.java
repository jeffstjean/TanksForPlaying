/*
* This is the Fast Tank Class it is the object that get initialized when a player
* selects the 'Fast Tank' tank type. it extends Tank and overrides the contructor and the reet method.
*/



public class FastTank extends Tank{
    
    public FastTank(double x, double y, double width, double height, ID id, Game g, int num) {
        super(x, y, width, height, id, g, num);
        this.speed = 3;
        this.health = 80;
        tankClass = TankClasses.Fast;
    }

    @Override // changes the default reset values for this tank
    public void reset() {
        super.reset();
        health = 80;
    }
    
}
