import bagel.Image;
import bagel.Input;
import bagel.util.Rectangle;

/**
 * @author Rrryaan
 * @date 2021/9/30 0:17
 * @brief
 */
public class Bomb extends Weapon{
    private static final Image bomb = new Image("res/level-1/bomb.png");
    private static final Image flame = new Image("res/level-1/flame.png");
    private static final int ShootingRange = 150;
    private Rectangle boundingBox;

    public Bomb(int X, int Y){
        super(X,Y,ShootingRange, bomb);
    }

    public void flame(){
        flame.draw(getX(), getY());
    }

}
