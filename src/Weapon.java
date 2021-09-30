import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * @author Rrryaan
 * @date 2021/9/29 23:49
 * @brief weapon
 */
public class Weapon {

    private final int WEAPON_TRAVEL_SPEED = 5;
    private final int WEAPON_SHOOT_SPEED = 3;
    private double weaponX = Window.getWidth();
    private int weaponY;
    private int shootingRange;
    private Rectangle boundingBox;


    public double getY() {
        return weaponX;
    }

    public double getX() {
        return weaponY;
    }

    public Rectangle getBox() {
        return boundingBox;
    }
}
