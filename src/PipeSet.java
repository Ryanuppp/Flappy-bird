import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PipeSet {
    private final Image PIPE_IMAGE = new Image("res/level/plasticPipe.png");
    private final int []starts = {-150, 0, 200};
    private final int PIPE_GAP = 168;
    private int PIPE_SPEED = 5;
    private double TOP_PIPE_Y = -PIPE_GAP / 2.0;
    private double BOTTOM_PIPE_Y = Window.getHeight() + PIPE_GAP / 2.0;
    private final DrawOptions ROTATOR = new DrawOptions().setRotation(Math.PI);
    private double pipeX = Window.getWidth();
    private Random random = new Random();

    public PipeSet() {
       int start = starts[random.nextInt(3)];
       TOP_PIPE_Y = -start + TOP_PIPE_Y;
       BOTTOM_PIPE_Y = -start + BOTTOM_PIPE_Y;
    }

    public void setPIPE_SPEED(int PIPE_SPEED) {
        this.PIPE_SPEED = PIPE_SPEED;
    }

    public double getPipeX() {
        return pipeX;
    }

    public void renderPipeSet() {
        PIPE_IMAGE.draw(pipeX, TOP_PIPE_Y);
        PIPE_IMAGE.draw(pipeX, BOTTOM_PIPE_Y, ROTATOR);
    }

    public void update() {
        renderPipeSet();
        pipeX -= PIPE_SPEED;

    }

    public Rectangle getTopBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, TOP_PIPE_Y));

    }

    public Rectangle getBottomBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, BOTTOM_PIPE_Y));

    }


}
