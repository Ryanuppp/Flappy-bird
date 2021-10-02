import bagel.Image;

/**
 * @author Rrryaan
 * @date 2021/9/29 23:49
 * @brief
 */
public class LifeBar {
    private final Image fullLife = new Image("res/level/fullLife.png");
    private final Image noLife = new Image("res/level/noLife.png");
    private final int top_left_X = 100;
    private final int top_left_Y = 15;
    private int totalLife;
    private int leftOverLife;
    private int level;
    public LifeBar(int level){
        this.level = level;
        if(level == 0){
            totalLife = 3;
            leftOverLife = 3;
        }
        if(level == 1){
            totalLife = 6;
            leftOverLife = 6;
        }
    }

    public int getTotalLife() {
        return totalLife;
    }

    public void lifeLose() {
        this.leftOverLife--;
    }

    public int getLeftOverLife() {
        return leftOverLife;
    }

    public void setLeftOverLife(int leftOverLife) {
        this.leftOverLife = leftOverLife;
    }

    public void update(){
        // TODO:update LifeBar when bird collides with a pipe or goes out-of-bound
       for(int i=0;i<leftOverLife;i++){
           fullLife.draw(top_left_X + i*50 , top_left_Y);
       }
       for(int j=0;j<totalLife-leftOverLife;j++){
            noLife.draw(top_left_X + leftOverLife*50 + j *50  , top_left_Y);
       }
    }
}
