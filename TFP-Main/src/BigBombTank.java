/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author owen7900
 */
public class BigBombTank extends Tank{
    
    public BigBombTank(double x, double y, double width, double height, ID id, Game g, int num) {
        super(x, y, width, height, id, g, num);
        health = 140;
        speed = 1;
        tankClass = TankClasses.BigBomb;
    }

    @Override
    public void reset() {
        super.reset(); 
        health = 140;
    }

    @Override
    protected void dropMine() {
        Game.handler.addObject(new Mine(x, y, 16, 16, ID.Mine, Game.handler, 1.5));
    }
    
        @Override
    public String toString() {
        return "Bomber Boy";
    }
    
}
