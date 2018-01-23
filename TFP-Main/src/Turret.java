
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Turret extends GameObject{

    private Tank tank;
    
    private Graphics2D g2d;
    private double xd,yd,rotate;
    
    private BufferedImage turret;
    private int turretShootCounter = 7;
    private double rotateChangeAmount = -0.1;
    
    private int coolDown = 20, coolDownCounter = 20;

    public Turret(double x, double y, double width, double height, ID id, Tank t) {
        super(x, y, width, height, id);

        tank = t;
        if(tank.getPlayerNum() == 1)
        turret = ImageLoader.imageLoader("src/resources/graphics/TurretGreen.png");
        else
            turret = ImageLoader.imageLoader("src/resources/graphics/TurretRed.png");
    }
    
    public Turret(Tank t) {
        super(100, 100, 10, 10, ID.Turret);

        tank = t;
        
        if(tank.getPlayerNum() == 1)
        turret = ImageLoader.imageLoader("src/resources/graphics/TurretGreen.png");
        else
            turret = ImageLoader.imageLoader("src/resources/graphics/TurretRed.png");
    }
    
    

    @Override
    public void tick() {
        
        if(turretShootCounter == 0){
            if(tank.getPlayerNum() == 1)
        turret = ImageLoader.imageLoader("src/resources/graphics/TurretGreen.png");
        else
            turret = ImageLoader.imageLoader("src/resources/graphics/TurretRed.png");
        }
        turretShootCounter --;
        coolDownCounter++; // increases the time since last shot by 1
        
        
        x = tank.getX() + tank.getSize() / 2;
        y = tank.getY() + tank.getSize() / 2;
        // sets x and y based on x and y of the tank it is attached to
        xd = (double) x;
        yd = (double ) y;
        
        bounds.setRect(x, y, 10,10);
        
        // sets the amount the turret needs to rotate based on the mouse location
        if(tank.getPlayerNum() == 1){
        if(Key.shoot1.isDown && coolDownCounter > coolDown) {
            shoot();
            if(tank.getPlayerNum() == 1)
        turret = ImageLoader.imageLoader("src/resources/graphics/TurretShotGreen.png");
        else
            turret = ImageLoader.imageLoader("src/resources/graphics/TurretShotRed.png");
            coolDownCounter = 0;
        }
        if(Key.turretRight1.isDown) 
            rotate = rotate - rotateChangeAmount;
        if(Key.turretLeft1.isDown)
            rotate = rotate + rotateChangeAmount;
        
        }else{
            if(Key.shoot2.isDown && coolDownCounter > coolDown) {
            shoot();
            if(tank.getPlayerNum() == 1)
        turret = ImageLoader.imageLoader("src/resources/graphics/TurretShotGreen.png");
        else
            turret = ImageLoader.imageLoader("src/resources/graphics/TurretShotRed.png");
            coolDownCounter = 0;
        }
            
            if(Key.turretRight2.isDown) 
            rotate = rotate - rotateChangeAmount;
        if(Key.turretLeft2.isDown)
            rotate = rotate + rotateChangeAmount;
            
        }
        
        
    }

    @Override
    public void render(Graphics g) {
        g2d = (Graphics2D) g;
        g2d.rotate(rotate + Math.toRadians(90), xd ,yd );//rotates graphics
        AffineTransform t = new AffineTransform();
        t.translate(x - tank.getSize() / 4, y - tank.getSize()/4);
        
        t.scale(tank.getSize()/turret.getWidth(), (tank.getSize()/2)/turret.getHeight());
        g2d.drawImage(turret, t, null);//renders image
       g2d.rotate(-(rotate + Math.toRadians(90)), xd ,yd );//rotates grpahics back
        
    }

    public double getRotate() {
        return rotate;
    }

    public void setTank(Tank tank) {
        this.tank = tank;
    }

    
    
    
    
    public void shoot(){

        try {
            SoundManager.playShootSound();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException ex) {
        }
        turretShootCounter = 10;
        double subX = -(tank.getSize() / 2 * Math.sin(rotate));
        double subY = (tank.getSize() / 2 * Math.cos(rotate));
        if(tank.tankClass == TankClasses.Destroyer) 
            Game.getHandler().addObject(new Bullet(x + (int)subX, y + (int)subY, 15, 15, ID.Bullet, 5, rotate, 40));
        else
            Game.getHandler().addObject(new Bullet(x + (int)subX, y + (int)subY, 15, 15, ID.Bullet, 5, rotate));
        if(tank.getPlayerNum() == 1)
        turret = ImageLoader.imageLoader("src/resources/graphics/TurretShotGreen.png");
        else
            turret = ImageLoader.imageLoader("src/resources/graphics/TurretShotRed.png");

    }

    public void setRotate(double rotate) {
        this.rotate = rotate;
    }
    
}
