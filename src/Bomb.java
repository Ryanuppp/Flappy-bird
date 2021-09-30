import bagel.Image;
import bagel.Input;
import bagel.util.Rectangle;

/**
 * @author Rrryaan
 * @date 2021/9/30 0:17
 * @brief
 */
public class Bomb extends Weapon{
    private final Image bomb = new Image("res/level-1/bomb.png");
    private final Image flame = new Image("res/level-1/flame.png");
    private final int ShootingRange = 50;

    private Rectangle boundingBox;
    public Bomb(){

    }
    public Rectangle update(Input input){
        // TODO: movement of bomb and explode
        return boundingBox;
    }
}
