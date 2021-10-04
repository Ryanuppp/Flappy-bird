import bagel.*;
import bagel.util.Rectangle;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * SWEN20003 Project 1, Semester 2, 2021
 *
 * @author Betty Lin
 */
public class ShadowFlap extends AbstractGame {
    private final Image BACKGROUND_IMAGE = new Image("res/level-0/background.png");
    private final Image BACKGROUND_IMAGE_LEVEL1 = new Image("res/level-1/background.png");
    private final Image BOMB_IMAGE = new Image("res/level-1/bomb.png");
    private final Image ROCK_IMAGE = new Image("res/level-1/rock.png");
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
    private Weapon currentWeapon;

    private LifeBar lifeBar;
    private Queue<PipeSet> pipeSets;
    private Queue<PipeSet> waitingToRemovePipeSet;
    private Queue<Weapon> weaponQueue;
    private Queue<Weapon> waitingToRemoveWeaponQueue;
    private Queue<Weapon> emissionQueue;
    private int score_level0;
    private int score_level1;
    private int score;
    private boolean gameOn;
    private boolean collision;
    private boolean collision_pickup;
    private int holdWeapon;
    private boolean win;
    private int frameCount;
    private boolean isLevel1;
    private int timeScale;
    private int interval;
    private Random random;


    public ShadowFlap() {
        super(1024, 768, "ShadowFlap");
        random =  new Random();
        bird = new Bird();
        lifeBar = new LifeBar(0);
        // currentPipeSet = new PipeSet();
        pipeSets = new LinkedList<PipeSet>();
        waitingToRemovePipeSet = new LinkedList<PipeSet>();
        waitingToRemoveWeaponQueue = new LinkedList<Weapon>();
        weaponQueue = new LinkedList<Weapon>();
        emissionQueue = new LinkedList<Weapon>();
        pipeSets.offer(new PipeSet(0));
        currentPipeSet = (PipeSet)pipeSets.peek();
        frameCount = 0;
        score = 0;
        score_level0 = 0;
        score_level1 = 0;
        gameOn = false;
        collision = false;
        win = false;
        isLevel1 = false;
        collision_pickup = false;
        holdWeapon = 0;
        timeScale = 0;  // speed of pipe
        interval = 100; //the interval to spawn a pipeSet
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
        if(!isLevel1)
            BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        else
            BACKGROUND_IMAGE_LEVEL1.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        // game has not started
        if (!gameOn) {
            if(!isLevel1)
                renderInstructionScreen(input);
            else
                renderLevelUpScreen(input);
        }

        if ((collision || birdOutOfBound()) && lifeBar.getLeftOverLife()!= 0){
            lifeBar.lifeLose();
            bird.reset();
            pipeSets.poll();
            currentPipeSet = pipeSets.peek();
            collision = false;
        }

        // game over
        if (lifeBar.getLeftOverLife() == 0) {
            if(!isLevel1)
                renderGameOverScreen(score_level0);
            else
                renderGameOverScreen(score_level1);
        }

        // game won
        if (win) {
            renderWinScreen();
        }

        // game is active
        if (gameOn && !collision && !win && !birdOutOfBound()) {

            frameCount++;
            if(frameCount % interval == 0){
                if(!isLevel1)
                    pipeSets.offer(new PipeSet(0));
                else
                    pipeSets.offer(new PipeSet(1));
            }

            bird.update(input);
            Rectangle birdBox = bird.getBox();

            if(!waitingToRemovePipeSet.isEmpty())
                for(PipeSet pipeSet:waitingToRemovePipeSet){
                    if(pipeSet.getPipeX()<-100)
                        waitingToRemovePipeSet.poll();
                    else
                        pipeSet.update();
                }

            if(input.wasPressed(Keys.L)){
                if (timeScale<4){
                    frameCount = frameCount/100 * 100;
                    timeScale++;
                    interval-=15;
                }
            }

            if(input.wasPressed(Keys.K)){
                if(timeScale>0){
                    frameCount = frameCount/100 * 100;
                    timeScale--;
                    interval+=15;
                }
            }

            if(!pipeSets.isEmpty())
                for(PipeSet pipeSet:pipeSets){
                    pipeSet.changeSpeed(timeScale);
                    pipeSet.update();
                    Rectangle topPipeBox = currentPipeSet.getTopBox();
                    Rectangle bottomPipeBox = currentPipeSet.getBottomBox();
                    collision = detectCollision(birdBox, topPipeBox, bottomPipeBox);
                }

            if(currentPipeSet != null){
                Rectangle topPipeBox = currentPipeSet.getTopBox();
                Rectangle bottomPipeBox = currentPipeSet.getBottomBox();
                collision = detectCollision(birdBox, topPipeBox, bottomPipeBox);
            }
            lifeBar.update();

            // spawn weapons
            if (isLevel1){
                if(frameCount % 125 == 0){
                    if(0 == random.nextInt(2)){
                        weaponQueue.offer(new Rock(1500, random.nextInt(100 + 700 )));
                    }
                    else{
                        weaponQueue.offer(new Bomb(1500, random.nextInt(100 + 700 )));
                    }
                }
                for(Weapon weapon: weaponQueue){
                    weapon.travel_update();
                }

                if (!weaponQueue.isEmpty()){
                    double a = weaponQueue.peek().getX();
                    double b = bird.getX()-30;
                    if(weaponQueue.size()>2){
                        int c = 0;
                    }
                    if(weaponQueue.peek().getX()<bird.getX()-30){
                        waitingToRemoveWeaponQueue.offer(weaponQueue.poll());
                    }
                }

                currentWeapon = weaponQueue.peek();

                for(Weapon weapon: waitingToRemoveWeaponQueue){
                    weapon.travel_update();
                    if(weapon.getX()<-50)
                        waitingToRemoveWeaponQueue.remove(weapon);
                }

                if(currentWeapon!= null){
                    collision_pickup = detectPickUp(birdBox, currentWeapon.getBox());
                    // System.out.println(currentWeapon.getClass());
                    // System.out.println(currentWeapon.hashCode());
                }
            }

            // test
            if(collision_pickup){
                System.out.println("meet weapon");
            }

            if (isLevel1 && collision_pickup && holdWeapon ==0){
                Weapon weapon = weaponQueue.poll();
                if(weapon instanceof Rock)
                    holdWeapon = 1;
                else
                    holdWeapon = 2;
                System.out.println("hold it.");
                collision_pickup = false;
            }else if (collision_pickup &&  holdWeapon != 0 && isLevel1){
                System.out.println("already have weapon:"+ holdWeapon);
                collision_pickup = false;
            }

            // draw weapon on th top
            if (isLevel1 && holdWeapon !=0){
                if(holdWeapon == 1)
                    ROCK_IMAGE.draw(400,15);
                else if(holdWeapon == 2)
                    BOMB_IMAGE.draw(400,15);
            }

            // emission the weapon
            if(isLevel1 && input.wasPressed(Keys.S)){
                if(holdWeapon !=0){
                    System.out.println("emission Weapon:"+holdWeapon);
                    if (holdWeapon == 1)
                        emissionQueue.offer(new Rock((int)bird.getX(),(int)bird.getY()));
                    else if(holdWeapon == 2)
                        emissionQueue.offer(new Bomb((int)bird.getX(),(int)bird.getY()));
                    holdWeapon = 0;
                }
                else{
                    System.out.println("no weapon!");

                }
            }

            // update weapon
            if(!emissionQueue.isEmpty()){
                for(Weapon weapon:emissionQueue){
                    weapon.shoot_update();
                    // ROCK
                    if(weapon instanceof Rock){
                        for(PipeSet pipeSet:pipeSets){
                            // collision and pipeSet is plastic
                            if(detectCollision(weapon.getBox(),pipeSet.getTopBox(),pipeSet.getBottomBox())){
                                if(pipeSet.getPIPE_TYPE() == 0){
                                    pipeSets.remove(pipeSet);
                                    currentPipeSet = pipeSets.peek();
                                }

                                emissionQueue.remove(weapon);
                                return;
                            }
                        }
                    }
                    else {
                        // BOMB
                        for(PipeSet pipeSet:pipeSets){
                            if(detectCollision(weapon.getBox(),pipeSet.getTopBox(),pipeSet.getBottomBox())){
                                pipeSets.remove(pipeSet);
                                currentPipeSet = pipeSets.peek();
                                Bomb bomb = (Bomb)weapon;
                                bomb.flame();
                                emissionQueue.remove(weapon);
                                return;
                            }
                        }
                    }
                }
            }
            updateScore();

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
        String finalScoreMsg = FINAL_SCORE_MSG + score_level1;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }

    public void renderLevelUpScreen(Input input){
        FONT.drawString(LEVEL_UP_MSG, (Window.getWidth()/2.0-(FONT.getWidth(LEVEL_UP_MSG)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0)));
        FONT.drawString(INSTRUCTION_MSG_LEVELUP, (Window.getWidth()/2.0-(FONT.getWidth(INSTRUCTION_MSG)/2.0)), (Window.getHeight()/2.0-(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
        lifeBar = new LifeBar(1);
        pipeSets.clear();
        pipeSets.offer(new PipeSet(1));
        currentPipeSet = pipeSets.peek();
        bird.reset();
        bird.levelUp();
        waitingToRemovePipeSet.clear();
        if (input.wasPressed(Keys.ENTER)) {
            gameOn = true;
        }
    }

    public boolean detectCollision(Rectangle birdBox, Rectangle topPipeBox, Rectangle bottomPipeBox) {
        // check for collision
        return birdBox.intersects(topPipeBox) ||
                birdBox.intersects(bottomPipeBox);
    }

    public boolean detectPickUp(Rectangle birdBox, Rectangle weaponBox){
        return birdBox.intersects(weaponBox);
    }


    public void updateScore() {
        // TODO level0 and level1 have different ways to update score
        if(currentPipeSet == null ){
            if(!pipeSets.isEmpty())
                currentPipeSet = pipeSets.peek();
            return;
        }
        if (!isLevel1 && bird.getX() > currentPipeSet.getTopBox().right()) {
             score_level0 += 1;
             //smooth the animation
             if(!pipeSets.isEmpty()){
                waitingToRemovePipeSet.offer(pipeSets.poll());
                if(!pipeSets.isEmpty())
                    currentPipeSet = pipeSets.peek();
             }
             score = score_level0;
        }else if(isLevel1 && bird.getX() > currentPipeSet.getTopBox().right()){
             score_level1 += 2;
             if(!pipeSets.isEmpty()){
                waitingToRemovePipeSet.offer(pipeSets.poll());
                if(!pipeSets.isEmpty())
                    currentPipeSet = pipeSets.peek();
             }
             score = score_level1;
        }
        String scoreMsg = SCORE_MSG + score;
        FONT.drawString(scoreMsg, 100, 100);

        // detect win
        if (score_level0 == 1){
            isLevel1 = true;
            gameOn = false;
            score_level0 = 0;
            score = 0;
            frameCount = 0;
        }

        if(score_level1 == 100){
            win = true;
        }
    }

}
