import bagel.*;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * SWEN20003 Project 1, Semester 2, 2021
 *
 * @author Betty Lin
 */
public class ShadowFlap extends AbstractGame {
    private final Image BACKGROUND_IMAGE = new Image("res/level-0/background.png");
    private final Image BACKGROUND_IMAGE_LEVEL1 = new Image("res/level-1/background.png");
    private final String INSTRUCTION_MSG = "PRESS SPACE TO START";
    private final String INSTRUCTION_MSG_LEVELUP = "PRESS ENTER TO CONTINUE";
    private final String GAME_OVER_MSG = "GAME OVER!";
    private final String CONGRATS_MSG = "CONGRATULATIONS!";
    private final String SCORE_MSG = "SCORE: ";
    private final String FINAL_SCORE_MSG = "FINAL SCORE: ";
    private final String LEVEL_UP_MSG = "LEVEL-UP!";
    private final int FONT_SIZE = 48;
    private final Font FONT = new Font("res/font/slkscr.ttf", FONT_SIZE);
    private final int SCORE_MSG_OFFSET = 75;
    private Bird bird;
    private PipeSet currentPipeSet;
    private LifeBar lifeBar;
    private Queue<PipeSet> pipeSets;
    private Queue<PipeSet> waitingToRemovePipeSet;
    private Queue<PipeSet_steel> pipeSet_steels;
    private Queue<PipeSet_steel> waitingToRemovePipeSet_steel;
    private int score_level1;
    private int score_level2;
    private boolean gameOn;
    private boolean collision;
    private boolean win;
    private int frameCount;
    private boolean isLevel1;


    public ShadowFlap() {
        super(1024, 768, "ShadowFlap");
        bird = new Bird();
        lifeBar = new LifeBar(0);
        // currentPipeSet = new PipeSet();
        pipeSets = new LinkedList<PipeSet>();
        waitingToRemovePipeSet = new LinkedList<PipeSet>();
        pipeSets.offer(new PipeSet());
        currentPipeSet = (PipeSet)pipeSets.peek();
        frameCount = 0;
        score_level1 = 0;
        score_level2 = 0;
        gameOn = false;
        collision = false;
        win = false;
        isLevel1 = false;
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowFlap game = new ShadowFlap();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {
        if(!isLevel1){
            update_level0(input);
        }else{
            update_level1(input);
        }
    }

    public boolean birdOutOfBound() {
        return (bird.getY() > Window.getHeight()) || (bird.getY() < 0);
    }


    public void renderInstructionScreen(Input input) {
        // paint the instruction on screen
        FONT.drawString(INSTRUCTION_MSG, (Window.getWidth()/2.0-(FONT.getWidth(INSTRUCTION_MSG)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0)));
        if (input.wasPressed(Keys.SPACE)) {
            gameOn = true;
        }
    }

    public void renderGameOverScreen(int score) {
        FONT.drawString(GAME_OVER_MSG, (Window.getWidth()/2.0-(FONT.getWidth(GAME_OVER_MSG)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
        collision = true;
    }

    public void renderWinScreen() {
        FONT.drawString(CONGRATS_MSG, (Window.getWidth()/2.0-(FONT.getWidth(CONGRATS_MSG)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score_level2;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }

    public void renderLevelUpScreen(Input input){
        FONT.drawString(LEVEL_UP_MSG, (Window.getWidth()/2.0-(FONT.getWidth(LEVEL_UP_MSG)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0)));
        FONT.drawString(INSTRUCTION_MSG_LEVELUP, (Window.getWidth()/2.0-(FONT.getWidth(INSTRUCTION_MSG)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
        lifeBar = new LifeBar(1);
        pipeSet_steels = new LinkedList<PipeSet_steel>();
        waitingToRemovePipeSet_steel = new LinkedList<PipeSet_steel>();
        pipeSet_steels.offer(new PipeSet_steel());
        currentPipeSet = (PipeSet_steel)pipeSet_steels.peek();
        if (input.wasPressed(Keys.ENTER)) {
            gameOn = true;
        }
    }

    public boolean detectCollision(Rectangle birdBox, Rectangle topPipeBox, Rectangle bottomPipeBox) {
        // check for collision
        return birdBox.intersects(topPipeBox) ||
                birdBox.intersects(bottomPipeBox);
    }

    public void updateScore() {
        // TODO level0 and level1 have different ways to update score
        if (!isLevel1 && bird.getX() > currentPipeSet.getTopBox().right()) {
             score_level1 += 1;
             //smooth the animation
             waitingToRemovePipeSet.offer(pipeSets.poll());
             if(!pipeSets.isEmpty())
                 currentPipeSet = pipeSets.peek();
             String scoreMsg = SCORE_MSG + score_level1;
             FONT.drawString(scoreMsg, 100, 100);
        }else if(isLevel1 && bird.getX() > currentPipeSet.getTopBox().right()){
             score_level2 += 1;
             waitingToRemovePipeSet_steel.offer(pipeSet_steels.poll());
             if(!pipeSet_steels.isEmpty())
                 currentPipeSet = pipeSet_steels.peek();
             String scoreMsg = SCORE_MSG + score_level2;
             FONT.drawString(scoreMsg, 100, 100);
        }

        // detect win
        if (score_level1 == 1) {
            isLevel1 = true;
            gameOn = false;
            score_level1 = 0;
        }

        if(score_level2 == 3){
            win = true;
        }
    }

    public void update_level0(Input input){
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        // game has not started
        if (!gameOn) {
            renderInstructionScreen(input);
        }

        if ((collision || birdOutOfBound()) && lifeBar.getLeftOverLife() != 0){
            lifeBar.lifeLose();
            bird.reset();
            pipeSets.poll();
            currentPipeSet = pipeSets.peek();
            collision = false;

        }

        // game over
        if ( lifeBar.getLeftOverLife() == 0) {
            renderGameOverScreen(score_level1);
        }

        // game won
        if (win) {
            renderWinScreen();
        }

        // game is active
        if (gameOn && !collision && !win && !birdOutOfBound()) {

            frameCount++;
            if(frameCount % 100 == 0){
                pipeSets.offer(new PipeSet());
            }

            bird.update(input);
            Rectangle birdBox = bird.getBox();
            for(PipeSet pipeSet:waitingToRemovePipeSet){
                if(pipeSet.getPipeX()<-30)
                    waitingToRemovePipeSet.poll();
                else
                    pipeSet.update();
            }

            for(PipeSet pipeSet:pipeSets){
                pipeSet.update();
                Rectangle topPipeBox = currentPipeSet.getTopBox();
                Rectangle bottomPipeBox = currentPipeSet.getBottomBox();
                collision = detectCollision(birdBox, topPipeBox, bottomPipeBox);
            };
            lifeBar.update();
            updateScore();

        }
    }

    public void update_level1(Input input){
        BACKGROUND_IMAGE_LEVEL1.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        // game has not started
        if (!gameOn) {
            renderLevelUpScreen(input);
        }

        if ((collision || birdOutOfBound()) && lifeBar.getLeftOverLife() != 0){
            lifeBar.lifeLose();
            bird.reset();
            pipeSet_steels.poll();
            currentPipeSet = pipeSet_steels.peek();
            collision = false;

        }

        // game over
        if ( lifeBar.getLeftOverLife() == 0) {
            renderGameOverScreen(score_level2);
        }

        // game won
        if (win) {
            renderWinScreen();
        }

        // game is active
        if (gameOn && !collision && !win && !birdOutOfBound()) {

            frameCount++;
            if(frameCount % 100 == 0){
                pipeSet_steels.offer(new PipeSet_steel());
            }

            bird.update(input);
            Rectangle birdBox = bird.getBox();
            for(PipeSet_steel pipeSet:waitingToRemovePipeSet_steel){
                if(pipeSet.getPipeX()<-30)
                    waitingToRemovePipeSet_steel.poll();
                else
                    pipeSet.update();
            }

            for(PipeSet_steel pipeSet:pipeSet_steels){
                pipeSet.update();
                Rectangle topPipeBox = currentPipeSet.getTopBox();
                Rectangle bottomPipeBox = currentPipeSet.getBottomBox();
                collision = detectCollision(birdBox, topPipeBox, bottomPipeBox);
            };
            lifeBar.update();
            updateScore();

        }
    }

}
