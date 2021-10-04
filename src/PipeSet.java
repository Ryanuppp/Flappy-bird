import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.Random;

public class PipeSet {
    private final Image PIPE_IMAGE_PLASTIC = new Image("res/level/plasticPipe.png");
    private final Image PIPE_IMAGE_STEEL = new Image("res/level-1/steelPipe.png");
    private final int []starts = {-150, 0, 200};
    private final int PIPE_GAP = 168;
    private int type;
    private int PIPE_SPEED = 5;
    private double TOP_PIPE_Y = -PIPE_GAP / 2.0;
    private double BOTTOM_PIPE_Y = Window.getHeight() + PIPE_GAP / 2.0;
    private final DrawOptions ROTATOR = new DrawOptions().setRotation(Math.PI);
    private double pipeX = Window.getWidth();
    private Random random = new Random();
    private final int []speed = {5,6,7,8,9};

    public PipeSet(int level) {
        if(level == 0)
            this.type = 0;
        else
            this.type = random.nextInt(2);
       int start = starts[random.nextInt(3)];
       TOP_PIPE_Y = -start + TOP_PIPE_Y;
       BOTTOM_PIPE_Y = -start + BOTTOM_PIPE_Y;
    }
    
    public void changeSpeed(int timescale){
        PIPE_SPEED = speed[timescale];
    }

    public int getPIPE_TYPE(){
        return type;
    }

    public double getPipeX() {
        return pipeX;
    }

    public void renderPipeSet() {
        if(type == 0){
            PIPE_IMAGE_PLASTIC.draw(pipeX, TOP_PIPE_Y);
            PIPE_IMAGE_PLASTIC.draw(pipeX, BOTTOM_PIPE_Y, ROTATOR);
        }
        else{
            PIPE_IMAGE_STEEL.draw(pipeX, TOP_PIPE_Y);
            PIPE_IMAGE_STEEL.draw(pipeX, BOTTOM_PIPE_Y, ROTATOR);
        }
    }

    public void update() {
        renderPipeSet();
        pipeX -= PIPE_SPEED;
    }

    public Rectangle getTopBox() {
        if(type==0)
            return PIPE_IMAGE_PLASTIC.getBoundingBoxAt(new Point(pipeX, TOP_PIPE_Y));
        else
            return PIPE_IMAGE_STEEL.getBoundingBoxAt(new Point(pipeX, TOP_PIPE_Y));

    }

    public Rectangle getBottomBox() {
        if(type==0)
            return PIPE_IMAGE_PLASTIC.getBoundingBoxAt(new Point(pipeX, BOTTOM_PIPE_Y));
        else
            return PIPE_IMAGE_STEEL.getBoundingBoxAt(new Point(pipeX, BOTTOM_PIPE_Y));
    }


}
