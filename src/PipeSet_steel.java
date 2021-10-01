import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;

import java.util.Random;

/**
 * @author Rrryaan
 * @date 2021/9/30 0:05
 * @brief PipeSet for level1
 */
public class PipeSet_steel extends PipeSet {
    private final Image PIPE_IMAGE = new Image("res/level-1/steelPipe.png");
    private final int []starts = {-150, 0, 200};
    private final int PIPE_GAP = 168;
    private int PIPE_SPEED = 5;
    private double TOP_PIPE_Y = -PIPE_GAP / 2.0;
    private double BOTTOM_PIPE_Y = Window.getHeight() + PIPE_GAP / 2.0;
    private final DrawOptions ROTATOR = new DrawOptions().setRotation(Math.PI);
    private double pipeX = Window.getWidth();
    private Random random = new Random();

    PipeSet_steel(){
        int start = starts[random.nextInt(3)];
        TOP_PIPE_Y = -start + TOP_PIPE_Y;
        BOTTOM_PIPE_Y = -start + BOTTOM_PIPE_Y;
    }

    public void renderPipeSet() {
        PIPE_IMAGE.draw(pipeX, TOP_PIPE_Y);
        PIPE_IMAGE.draw(pipeX, BOTTOM_PIPE_Y, ROTATOR);
    }

    public void update() {
        renderPipeSet();
        pipeX -= PIPE_SPEED;

    }
}
