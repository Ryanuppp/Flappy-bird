import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;
import org.lwjgl.system.CallbackI;

import java.util.Random;

/**
 * @author Rrryaan
 * @date 2021/9/29 23:49
 * @brief weapon
 */
public class Weapon {
    private int WEAPON_TRAVEL_SPEED = 5;
    private final int WEAPON_SHOOT_SPEED = 5;
    private double weaponX;
    private int weaponY;
    private int shootingRange;
    private Rectangle boundingBox;
    private Image weaponImage;
    private Random random =  new Random();
    private final int []speed = {5,6,7,8,9};

    public Weapon(int weaponX, int weaponY, int shootingRange, Image image){
        this.weaponX = weaponX;
        this.weaponY = weaponY;
        this.shootingRange = shootingRange;
        weaponImage = image;
    }

    public double getY() {
        return weaponY;
    }

    public double getX() {
        return weaponX;
    }

    public void changeSpeed(int timescale){
        WEAPON_TRAVEL_SPEED = speed[timescale];
    }

    public void renderWeapon(){
        weaponImage.draw(weaponX,weaponY);
    }

    public void travel_update(){
        renderWeapon();
        weaponX -= WEAPON_TRAVEL_SPEED;
    }

    public void shoot_update(){
        renderWeapon();;
        weaponX += WEAPON_SHOOT_SPEED;
    }

    public Rectangle getBox() {
        return weaponImage.getBoundingBoxAt(new Point(weaponX, weaponY));
    }
}
