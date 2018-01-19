/*
* This is the class for the 'Bomber' type tank
* it goes slower, has more health and drop larger bombs
* it extends Tank and overrides some of its meathods
*/
public class BigBombTank extends Tank{
    
    public BigBombTank(double x, double y, double width, double height, ID id, Game g, int num) {
        super(x, y, width, height, id, g, num);
        //increases tanks health and decreases speed
        health = 140;
        speed = 1;
        // sets tank class to bomb class
        tankClass = TankClasses.BigBomb;
    }

    @Override
    public void reset() {
        //calls parent reset to reset all values
        super.reset(); 
        //sets health to normal value
        health = 140;
    }

    @Override
    protected void dropMine() {
        //drops a custom mine that is larger
        Game.handler.addObject(new Mine(x, y, 16, 16, ID.Mine, Game.handler, 1.5));
    }
    
        @Override
    public String toString() {
        return "Bomber Boy";
    }
    
}
