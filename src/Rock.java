import bagel.Image;
import bagel.Input;
import bagel.util.Rectangle;

/**
 * @author Rrryaan
 * @date 2021/9/30 0:17
 * @brief
 */
public class Rock extends Weapon{
    private final Image rock = new Image("res/level-1/rock.png");
    private Rectangle boundingBox;
    private final int ShootingRange = 25;

    public Rock(){

    }

    public Rectangle update(){
        // TODO: movement and explode of Rock
        return boundingBox;
    }
}
