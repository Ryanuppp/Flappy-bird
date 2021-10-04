import bagel.Image;
import bagel.Input;
import bagel.util.Rectangle;

/**
 * @author Rrryaan
 * @date 2021/9/30 0:17
 * @brief
 */
public class Rock extends Weapon{
    private static final Image rock = new Image("res/level-1/rock.png");
    private Rectangle boundingBox;
    private static final int ShootingRange = 25;

    public Rock(int X, int Y){
        super(X, Y,ShootingRange, rock);
    }


}
