package academy.learnprogramming.bubblesplit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;

import androidx.core.content.res.ResourcesCompat;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import java.io.IOException;

/**
 * The main scene of the game - the game play scene.
 */
public class GamePlayScene implements Scene{

    private static String TAG = "GamePlayScene";
    public static String SCENE_NAME = "GamePlayScene";
    public static int WIDTH, HEIGHT;

    private  GamePanel gamePanel;

    private Background background;

    private CircleButton leftCircleButton;
    private CircleButton rightCircleButton;
    private CircleButton shootCircleButton;
    private CircleButton pauseButton;

    private Character character;

    private Set<Bubble> bubbles;
    private InputStream inputStream;

    private SingleImageObject tapToStart;
    private SingleImageObject endGameMessage;
    private SingleImageObject lostMessage;
    public static GameState gameState = GameState.GAME_NOT_STARTED;
    public static int currentLevel;
    public static boolean levelLoaded;
    public static boolean levelProgressed;

    private SingleDrawableObject characterHitbox;

    public GamePlayScene(GamePanel gamePanel, int width, int height) {
        this.gamePanel=gamePanel;
        WIDTH=width;
        HEIGHT=height;

        background = new Background(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.game_map), Constants.ORIGINAL_SCREEN_WIDTH, Constants.ORIGINAL_SCREEN_HEIGHT, false));

        bubbles = new HashSet<>();

        leftCircleButton = new CircleButton(ResourcesCompat.getDrawable(gamePanel.getResources(), R.drawable.left_button_not_pressed, null), ResourcesCompat.getDrawable(gamePanel.getResources(), R.drawable.left_button_not_pressed, null), ResourcesCompat.getDrawable(gamePanel.getResources(), R.drawable.left_button_pressed, null));
        leftCircleButton.setX(Constants.ORIGINAL_SCREEN_WIDTH / 6 - 100);
        leftCircleButton.setY(Constants.GAMEPLAY_GROUND_Y + leftCircleButton.radius + 65);
        rightCircleButton = new CircleButton(ResourcesCompat.getDrawable(gamePanel.getResources(), R.drawable.right_button_not_pressed, null), ResourcesCompat.getDrawable(gamePanel.getResources(), R.drawable.right_button_not_pressed, null), ResourcesCompat.getDrawable(gamePanel.getResources(), R.drawable.right_button_pressed, null));
        rightCircleButton.setX((Constants.ORIGINAL_SCREEN_WIDTH / 6) + 150);
        rightCircleButton.setY(Constants.GAMEPLAY_GROUND_Y + rightCircleButton.radius + 65);
        shootCircleButton = new CircleButton(ResourcesCompat.getDrawable(gamePanel.getResources(), R.drawable.shoot_button_not_pressed, null), ResourcesCompat.getDrawable(gamePanel.getResources(), R.drawable.shoot_button_not_pressed, null), ResourcesCompat.getDrawable(gamePanel.getResources(), R.drawable.shoot_button_pressed, null));
        shootCircleButton.setX((Constants.ORIGINAL_SCREEN_WIDTH / 8) + 1350);
        shootCircleButton.setY(Constants.GAMEPLAY_GROUND_Y + shootCircleButton.radius );
        pauseButton = new CircleButton(ResourcesCompat.getDrawable(gamePanel.getResources(), R.drawable.pause_button, null), ResourcesCompat.getDrawable(gamePanel.getResources(), R.drawable.pause_button, null), ResourcesCompat.getDrawable(gamePanel.getResources(), R.drawable.pause_button, null));
        pauseButton.setX(Constants.ORIGINAL_SCREEN_WIDTH - pauseButton.width );
        pauseButton.setY(150);


        BitmapDrawable bulletDrawable = new BitmapDrawable(gamePanel.getResources(),BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.bullet) );
        character= new Character(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.moving_man_sprite), 36, 9, leftCircleButton, rightCircleButton, shootCircleButton, bulletDrawable, bubbles, gamePanel.getContext());
        character.setX((Constants.ORIGINAL_SCREEN_WIDTH/2) - character.width);
        character.setY(Constants.GAMEPLAY_GROUND_Y-character.height);


        tapToStart = new SingleImageObject(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.tap_to_start));
        tapToStart.setX((Constants.ORIGINAL_SCREEN_WIDTH/4)-200);
        tapToStart.setY(Constants.ORIGINAL_SCREEN_HEIGHT/4);
        endGameMessage = new SingleImageObject(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.end_game_message));
        endGameMessage.setX((Constants.ORIGINAL_SCREEN_WIDTH/4) + 100);
        endGameMessage.setY(Constants.ORIGINAL_SCREEN_HEIGHT/4);
        lostMessage = new SingleImageObject(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.lost_message));
        lostMessage.setX((Constants.ORIGINAL_SCREEN_WIDTH/4) - 200);
        lostMessage.setY(Constants.ORIGINAL_SCREEN_HEIGHT/4);

        characterHitbox = new SingleDrawableObject(ResourcesCompat.getDrawable(gamePanel.getResources(), R.drawable.character_hitbox, null));
        characterHitbox.setX(character.x + (character.width-Constants.CHARACTER_HITBOX_WIDTH)/2 + Constants.CHARACTER_HITBOX_WIDTH);
        characterHitbox.setY(character.y  + (character.height-Constants.CHARACTER_HITBOX_HEIGHT)/2);
    }


    @Override
    public void terminate() {
        character.finish();
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        int action = event.getActionMasked();
        int index = event.getActionIndex() ;
        int xPosition, yPosition;

        // adjust x and y the screen resolution by dividing by the factors
        xPosition = (int) (event.getX(index) / GamePanel.scaleFactorXMul);
        yPosition = (int) (event.getY(index) / GamePanel.scaleFactorYMul);

        Rect leftRect = new Rect(0, Constants.MOVE_BUTTON_HITBOX_Y, Constants.LEFT_BUTTON_HITBOX_X, Constants.ORIGINAL_SCREEN_HEIGHT);
        Rect rightRect = new Rect(Constants.LEFT_BUTTON_HITBOX_X, Constants.MOVE_BUTTON_HITBOX_Y, Constants.RIGHT_BUTTON_HITBOX_X, Constants.ORIGINAL_SCREEN_HEIGHT);

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                if(gameState == GameState.GAME_NOT_STARTED){

                }else if (gameState == GameState.GAME_STARTED){
//                    Log.e(SCENE_NAME, "receiveTouchMove: " + event.getPointerId(event.getActionIndex()));
                    if(leftCircleButton.isPressed() && !isInMoveButtonsHitBox(leftRect, (int) (event.getX(event.findPointerIndex(leftCircleButton.getMotionEventID())) / GamePanel.scaleFactorXMul), (int) (event.getY(event.findPointerIndex(leftCircleButton.getMotionEventID())) / GamePanel.scaleFactorYMul))){
                        if(isInMoveButtonsHitBox(rightRect, (int) (event.getX(event.findPointerIndex(leftCircleButton.getMotionEventID())) / GamePanel.scaleFactorXMul), (int) (event.getY(event.findPointerIndex(leftCircleButton.getMotionEventID())) / GamePanel.scaleFactorYMul))){
                            leftCircleButton.setPressed(false);
                            leftCircleButton.setEnabled(false);
                            rightCircleButton.setEnabled(true);
                            rightCircleButton.setPressed(true);
                            rightCircleButton.setMotionEventID(leftCircleButton.getMotionEventID());
                            leftCircleButton.setMotionEventID(Constants.INVALID_POINTER_ID);
                        }
                        else{
                            leftCircleButton.setPressed(false);
                            leftCircleButton.setEnabled(true);
                        }
                    }
                    if(rightCircleButton.isPressed() && !isInMoveButtonsHitBox(rightRect, (int) (event.getX(event.findPointerIndex(rightCircleButton.getMotionEventID())) / GamePanel.scaleFactorXMul), (int) (event.getY(event.findPointerIndex(rightCircleButton.getMotionEventID())) / GamePanel.scaleFactorYMul))){
                        if(isInMoveButtonsHitBox(leftRect, (int) (event.getX(event.findPointerIndex(rightCircleButton.getMotionEventID())) / GamePanel.scaleFactorXMul), (int) (event.getY(event.findPointerIndex(rightCircleButton.getMotionEventID())) / GamePanel.scaleFactorYMul))){
                            rightCircleButton.setPressed(false);
                            rightCircleButton.setEnabled(false);
                            leftCircleButton.setEnabled(true);
                            leftCircleButton.setPressed(true);
                            leftCircleButton.setMotionEventID(rightCircleButton.getMotionEventID());
                            rightCircleButton.setMotionEventID(Constants.INVALID_POINTER_ID);
                        }
                        else{
                            rightCircleButton.setPressed(false);
                            rightCircleButton.setEnabled(true);
                        }
                    }

                }
                break;
            case MotionEvent.ACTION_DOWN:  case MotionEvent.ACTION_POINTER_DOWN:
//                Log.e(SCENE_NAME, "receiveTouchDown: " + xPosition +", "+ yPosition);
                if (gameState == GameState.GAME_NOT_STARTED) {
                    if(levelLoaded) gameState = GameState.GAME_STARTED;
                    character.resetAnimation();
                    for(Bubble bubble:bubbles){
                        bubble.resetAnimation();
                    }
                } else if(gameState == GameState.GAME_STARTED){
                    if (leftCircleButton.isEnabled() && !leftCircleButton.isPressed() && isInMoveButtonsHitBox(leftRect, xPosition, yPosition)) {
                        leftCircleButton.setPressed(true);
                        leftCircleButton.setMotionEventID(event.getPointerId(index));
                        rightCircleButton.setEnabled(false);
                    }
                    if (rightCircleButton.isEnabled() && !rightCircleButton.isPressed() && isInMoveButtonsHitBox(rightRect, xPosition, yPosition)) {
                        rightCircleButton.setPressed(true);
                        rightCircleButton.setMotionEventID(event.getPointerId(index));
                        leftCircleButton.setEnabled(false);
                    }
                    if(shootCircleButton.isEnabled() && !shootCircleButton.isPressed() && shootCircleButton.containsPoint(xPosition, yPosition)) {
                        shootCircleButton.setPressed(true);
                        shootCircleButton.setMotionEventID(event.getPointerId(index));
//
                    }
                    if(pauseButton.isEnabled() && !pauseButton.isPressed() && pauseButton.containsPoint(xPosition, yPosition)){
                        gamePanel.openPopUp("GamePlayPausePopUp");
                        gameState = GameState.GAME_PAUSED;
                    }
                }
                break;
            case MotionEvent.ACTION_UP: case MotionEvent.ACTION_CANCEL:
                if(gameState == GameState.GAME_NOT_STARTED){

                }
                else if(gameState == GameState.GAME_STARTED){
                    leftCircleButton.setPressed(false);
                    leftCircleButton.setMotionEventID(Constants.INVALID_POINTER_ID);
                    leftCircleButton.setEnabled(true);
                    rightCircleButton.setPressed(false);
                    rightCircleButton.setMotionEventID(Constants.INVALID_POINTER_ID);
                    rightCircleButton.setEnabled(true);
                    shootCircleButton.setPressed(false);
                    shootCircleButton.setMotionEventID(Constants.INVALID_POINTER_ID);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:

                if(leftCircleButton.isPressed() && leftCircleButton.getMotionEventID() ==  event.getPointerId(event.getActionIndex())){
                    leftCircleButton.setPressed(false);
                    leftCircleButton.setMotionEventID(Constants.INVALID_POINTER_ID);
                    rightCircleButton.setEnabled(true);
                }
                if(rightCircleButton.isPressed() && rightCircleButton.getMotionEventID() ==  event.getPointerId(event.getActionIndex())){
                    rightCircleButton.setPressed(false);
                    rightCircleButton.setMotionEventID(Constants.INVALID_POINTER_ID);
                    leftCircleButton.setEnabled(true);
                }
                if(shootCircleButton.isPressed() && shootCircleButton.getMotionEventID() == event.getPointerId(event.getActionIndex())){
                    shootCircleButton.setPressed(false);
                    shootCircleButton.setMotionEventID(Constants.INVALID_POINTER_ID);
                }
                break;
        }

    }

    /**
     * There is a hit box which is not visible to the user. it is in the bottom left of the screen. here we check if the user is currently touching this hit box.
     * @param rect
     * @param x
     * @param y
     * @return
     */
    public boolean isInMoveButtonsHitBox(Rect rect, int x, int y){
        if(x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom) return true;
        else return false;
    }

    /**
     * here we load levels to the game, by loading a text file from assets. there is some rules that the text files are build by those rules
     * Building Levels:
     * 1. at the first char, enter the number of bubbles
     * 2. than enter 3 chars:
     * 2a. the first char is the bubble size - b(big),m(medium),s(small) or v(very_small)
     * 2b. than enter where you want the starting x position of the bubble to be. the char you enter needs to be between 1-9, and the starting x position will be char*200
     * 2c. than enter '+' or '-' for the direction of the bubble, '+' for forward and '-' for backwards
     * 3. a level should be a string of 1 line
     */
    public void loadLevel(String level){
        try {
            inputStream = gamePanel.getContext().getAssets().open(level+".txt");
        } catch (IOException e){
        }
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        for(int i = 0; i< java.lang.Character.getNumericValue(result.charAt(0)); i++){
            int dx = (result.charAt(i*3 +3) == '-'?Constants.BUBBLE_NEGATIVE_X_DIRECTION :Constants.BUBBLE_POSITIVE_X_DIRECTION);
            switch (result.charAt(i*3 + 1)){
                case 'b':
                    bubbles.add(new Bubble(ResourcesCompat.getDrawable(gamePanel.getResources(), R.drawable.big_bubble, null), Constants.BIG_BUBBLE_Y_SPEED, dx, 80, BubbleSize.BIG,java.lang.Character.getNumericValue(result.charAt(i*3 + 2))*200 , 200));
                    break;
                case 'm':
                    bubbles.add(new Bubble(ResourcesCompat.getDrawable(gamePanel.getResources(), R.drawable.medium_bubble, null), Constants.MEDIUM_BUBBLE_Y_SPEED, dx, 90, BubbleSize.MEDIUM,java.lang.Character.getNumericValue(result.charAt(i*3 + 2))*200 ,200 ));
                    break;
                case 's':
                    bubbles.add(new Bubble(ResourcesCompat.getDrawable(gamePanel.getResources(), R.drawable.small_bubble, null), Constants.SMALL_BUBBLE_Y_SPEED, dx, 120, BubbleSize.SMALL,java.lang.Character.getNumericValue(result.charAt(i*3 + 2))*200 ,200 ));
                    break;
                case 'v':
                    bubbles.add(new Bubble(ResourcesCompat.getDrawable(gamePanel.getResources(), R.drawable.very_small_bubble, null), Constants.VERY_SMALL_BUBBLE_Y_SPEED, dx, 140, BubbleSize.VERYSMALL,java.lang.Character.getNumericValue(result.charAt(i*3 + 2))*200 ,200 ));
                    break;
            }
        }
        levelLoaded = true;
    }


    /**
     * drawing the current level of the game, using Paint.
     * @param canvas
     * @param level
     */
    public void drawCurrentLevel(Canvas canvas, int level){
        Paint levelNumber = new Paint();
        Paint levelText = new Paint();
        levelNumber.setColor(Color.WHITE);
        levelNumber.setStyle(Paint.Style.FILL);
//        canvas.drawPaint(levelNumber);
        levelText.setColor(Color.WHITE);
        levelText.setStyle(Paint.Style.FILL);

        levelText.setColor(Color.BLACK);
        levelText.setTextSize(100);
        levelText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        Rect levelTextTextBounds = new Rect();
        levelText.getTextBounds("Level", 0, 5, levelTextTextBounds);
        canvas.drawText("Level",Constants.ORIGINAL_SCREEN_WIDTH/2 - levelTextTextBounds.width() +100 , Constants.GAMEPLAY_GROUND_Y+150, levelText );

        levelNumber.setColor(Color.BLACK);
        levelNumber.setTextSize(100);
        levelNumber.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        Rect levelNumberTextBounds = new Rect();
        levelNumber.getTextBounds(String.valueOf(level), 0, 1, levelNumberTextBounds);
        canvas.drawText(String.valueOf(level), Constants.ORIGINAL_SCREEN_WIDTH/2 -levelTextTextBounds.exactCenterX()  + 80, Constants.GAMEPLAY_GROUND_Y + 250, levelNumber);
    }

    /**
     * Because the music changes according to the progress of the game, we need to update the service
     */
    public void updateService(){
        gamePanel.getContext().stopService(new Intent(gamePanel.getContext(), MusicService.class));
        Intent i = new Intent(gamePanel.getContext(), MusicService.class);
        i.putExtra("currLvl", GamePlayScene.currentLevel);
        gamePanel.getContext().startService(i);
    }

    /**
     * updating the game , by considering all the different states the game could be.
     * 1. the game can be paused
     * 2. the game can be still not started
     * 3. the game can be started
     * 4. the game can be finished
     */
    @Override
    public void update() {
        if(levelProgressed){
            SharedPreferences.Editor editor = gamePanel.savedSettings.edit();
            editor.putInt("MaxLevelReached", Math.max(currentLevel,gamePanel.savedSettings.getInt("MaxLevelReached",1)));
            editor.apply();

            if(currentLevel>Constants.NO_OF_LEVELS){
                gamePanel.enterScene(EndingScene.SCENE_NAME);
                try {
                    if (GamePanel.endingSound.isPlaying()) {
                        GamePanel.endingSound.stop();
                        GamePanel.endingSound.release();
                        GamePanel.endingSound = MediaPlayer.create(gamePanel.getContext(), R.raw.congrats_sound);
                    } GamePanel.endingSound.start();
                } catch(Exception e) { e.printStackTrace(); }
            }else {
                LevelsScene.buttons.get(GamePlayScene.currentLevel - 1).setShown(true);
                gamePanel.saveProgress(String.valueOf(GamePlayScene.currentLevel));
                if(currentLevel == gamePanel.savedSettings.getInt("MaxLevelReached",1)){
                    if(currentLevel == 6 || currentLevel == 11) updateService();
                }
                levelProgressed = false;
            }
        }
        if(gameState == GameState.GAME_NOT_STARTED) {
            if(bubbles.isEmpty()) loadLevel("level_" + currentLevel);
        }else if(gameState == GameState.GAME_STARTED){
            background.update();
            leftCircleButton.update();
            rightCircleButton.update();
            shootCircleButton.update();
            pauseButton.update();
            character.update();
            for(Bubble bubble:bubbles){
                bubble.update();
            }
            tapToStart.update();
            endGameMessage.update();
            lostMessage.update();
        }
        characterHitbox.setX(character.x + (character.width-Constants.CHARACTER_HITBOX_WIDTH)/2);
        characterHitbox.setY(character.y  + (character.height-Constants.CHARACTER_HITBOX_HEIGHT)/2);
    }

    @Override
    public void draw(Canvas canvas) {
        background.draw(canvas);
        character.draw(canvas);
        for(Bubble bubble:bubbles){
            bubble.draw(canvas);
        }
        leftCircleButton.draw(canvas);
        rightCircleButton.draw(canvas);
        shootCircleButton.draw(canvas);
        pauseButton.draw(canvas);
        if(gameState != GameState.GAME_ENDED) drawCurrentLevel(canvas, currentLevel);
        else{
            if(character.gameResult) drawCurrentLevel(canvas, currentLevel-1);
            else drawCurrentLevel(canvas, currentLevel);
        }
        if(gameState == GameState.GAME_NOT_STARTED) {
            tapToStart.draw(canvas);
        }
        else if(gameState == GameState.GAME_ENDED){
            if(character.gameResult) endGameMessage.draw(canvas);
            else lostMessage.draw(canvas);
        }
        if(gamePanel.savedSettings.getBoolean("HitBox",false)) {
            characterHitbox.draw(canvas);
        }
    }

}
