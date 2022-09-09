package academy.learnprogramming.bubblesplit;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import androidx.core.content.res.ResourcesCompat;

/**
 * The opening scene of the game, the scene that is shown when first entering the application.
 */
public class OpeningScene implements Scene {

    private static final String TAG = "OpeningScene";
    public static String SCENE_NAME = "OpeningScene";
    public static int WIDTH, HEIGHT;

    private  GamePanel gamePanel;

    private Background background;

    private GameTitle gameTitle;

    private Button levelsButton;
    private Button howToPlayButton;
    private Button settings;
    private Button resetGameAndExit;
    private Button exit;

    public boolean waitingForAnswerResetAndExit = false;
    public boolean waitingForAnswerExit = false;


    public OpeningScene(GamePanel gamePanel, int width, int height){
        this.gamePanel = gamePanel;
        WIDTH=width;
        HEIGHT=height;

        background = new Background(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.background));

        gameTitle = new GameTitle(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.bs_title));
        gameTitle.setX((Constants.ORIGINAL_SCREEN_WIDTH/2) - (gameTitle.getWidth() / 2));
        gameTitle.setY((Constants.ORIGINAL_SCREEN_HEIGHT/2) - (gameTitle.getHeight() / 2) - 300);


        levelsButton=new Button(ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.levels_button_not_pressed, null),ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.levels_button_not_pressed, null),ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.levels_button_pressed, null));
        levelsButton.setX((Constants.ORIGINAL_SCREEN_WIDTH/2) - (levelsButton.getWidth() / 2));
        levelsButton.setY((Constants.ORIGINAL_SCREEN_HEIGHT/2)  - 200);

        howToPlayButton = new Button(ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.how_to_play_button_not_pressed, null),ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.how_to_play_button_not_pressed, null),ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.how_to_play_button_pressed, null));
        howToPlayButton.setX((Constants.ORIGINAL_SCREEN_WIDTH/2) - (howToPlayButton.getWidth() / 2));
        howToPlayButton.setY((Constants.ORIGINAL_SCREEN_HEIGHT/2) +30);

        settings = new Button(ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.settings_button_not_pressed, null),ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.settings_button_not_pressed, null),ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.settings_button_pressed, null));
        settings.setX((Constants.ORIGINAL_SCREEN_WIDTH/2) - (settings.getWidth() / 2));
        settings.setY((Constants.ORIGINAL_SCREEN_HEIGHT/2) + 200);

        resetGameAndExit = new Button(ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.reset_game_button_not_pressed, null),ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.reset_game_button_not_pressed, null),ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.reset_game_button_pressed, null));
        resetGameAndExit.setX((Constants.ORIGINAL_SCREEN_WIDTH/2) - (settings.getWidth() / 2) + 650);
        resetGameAndExit.setY((Constants.ORIGINAL_SCREEN_HEIGHT/2) + 250);

        exit = new Button(ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.exit_button_not_pressed, null),ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.exit_button_not_pressed, null),ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.exit_button_pressed, null));
        exit.setX((Constants.ORIGINAL_SCREEN_WIDTH/2) - (settings.getWidth() / 2) - 650);
        exit.setY((Constants.ORIGINAL_SCREEN_HEIGHT/2) + 250);
    }

    @Override
    public void terminate() {
    }

    /**
     * The buttons here are very smooth. they all have a pressed affect, and you can slide easily between them.
     * @param event
     */
    @Override
    public void receiveTouch(MotionEvent event) {
        int action = event.getActionMasked();
        int index = event.getActionIndex() ;
        int xPosition, yPosition;

        // adjust x and y the screen resolution by dividing by the factors
        xPosition = (int) (event.getX(index) / GamePanel.scaleFactorXMul);
        yPosition = (int) (event.getY(index) / GamePanel.scaleFactorYMul);

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                if(levelsButton.isPressed() && !levelsButton.touched((int) (event.getX(event.findPointerIndex(levelsButton.getMotionEventID())) / GamePanel.scaleFactorXMul), (int) (event.getY(event.findPointerIndex(levelsButton.getMotionEventID())) / GamePanel.scaleFactorYMul))){
                    levelsButton.setPressed(false);
                    levelsButton.setMotionEventID(Constants.INVALID_POINTER_ID);
                    howToPlayButton.setEnabled(true);
                    settings.setEnabled(true);
                    resetGameAndExit.setEnabled(true);
                    exit.setEnabled(true);
                }
                if(howToPlayButton.isPressed() && !howToPlayButton.touched((int) (event.getX(event.findPointerIndex(howToPlayButton.getMotionEventID())) / GamePanel.scaleFactorXMul), (int) (event.getY(event.findPointerIndex(howToPlayButton.getMotionEventID())) / GamePanel.scaleFactorYMul))){
                    howToPlayButton.setPressed(false);
                    howToPlayButton.setMotionEventID(Constants.INVALID_POINTER_ID);
                    levelsButton.setEnabled(true);
                    settings.setEnabled(true);
                    resetGameAndExit.setEnabled(true);
                    exit.setEnabled(true);
                }
                if(settings.isPressed() &&!settings.touched((int) (event.getX(event.findPointerIndex(settings.getMotionEventID())) / GamePanel.scaleFactorXMul), (int) (event.getY(event.findPointerIndex(settings.getMotionEventID())) / GamePanel.scaleFactorYMul))){
                    settings.setPressed(false);
                    settings.setMotionEventID(Constants.INVALID_POINTER_ID);
                    levelsButton.setEnabled(true);
                    howToPlayButton.setEnabled(true);
                    resetGameAndExit.setEnabled(true);
                    exit.setEnabled(true);
                }
                if(resetGameAndExit.isPressed() &&!resetGameAndExit.touched((int) (event.getX(event.findPointerIndex(resetGameAndExit.getMotionEventID())) / GamePanel.scaleFactorXMul), (int) (event.getY(event.findPointerIndex(resetGameAndExit.getMotionEventID())) / GamePanel.scaleFactorYMul))){
                    resetGameAndExit.setPressed(false);
                    resetGameAndExit.setMotionEventID(Constants.INVALID_POINTER_ID);
                    levelsButton.setEnabled(true);
                    howToPlayButton.setEnabled(true);
                    settings.setEnabled(true);
                    exit.setEnabled(true);
                }
                if(exit.isPressed() &&!exit.touched((int) (event.getX(event.findPointerIndex(exit.getMotionEventID())) / GamePanel.scaleFactorXMul), (int) (event.getY(event.findPointerIndex(exit.getMotionEventID())) / GamePanel.scaleFactorYMul))){
                    exit.setPressed(false);
                    exit.setMotionEventID(Constants.INVALID_POINTER_ID);
                    levelsButton.setEnabled(true);
                    howToPlayButton.setEnabled(true);
                    settings.setEnabled(true);
                    resetGameAndExit.setEnabled(true);
                }
                if(levelsButton.touched(xPosition,yPosition)){
                    levelsButton.setPressed(true);
                    levelsButton.setMotionEventID(event.getPointerId(index));
                    howToPlayButton.setEnabled(false);
                    settings.setEnabled(false);
                    resetGameAndExit.setEnabled(false);
                    exit.setEnabled(false);
                }
                if(howToPlayButton.touched(xPosition,yPosition)){
                    howToPlayButton.setPressed(true);
                    howToPlayButton.setMotionEventID(event.getPointerId(index));
                    levelsButton.setEnabled(false);
                    settings.setEnabled(false);
                    resetGameAndExit.setEnabled(false);
                    exit.setEnabled(false);
                }
                if(settings.touched(xPosition,yPosition)){
                    settings.setPressed(true);
                    settings.setMotionEventID(event.getPointerId(index));
                    levelsButton.setEnabled(false);
                    howToPlayButton.setEnabled(false);
                    resetGameAndExit.setEnabled(false);
                    exit.setEnabled(false);
                }
                if(resetGameAndExit.touched(xPosition,yPosition)){
                    resetGameAndExit.setPressed(true);
                    resetGameAndExit.setMotionEventID(event.getPointerId(index));
                    levelsButton.setEnabled(false);
                    howToPlayButton.setEnabled(false);
                    settings.setEnabled(false);
                    exit.setEnabled(false);
                }
                if(exit.touched(xPosition,yPosition)){
                    exit.setPressed(true);
                    exit.setMotionEventID(event.getPointerId(index));
                    levelsButton.setEnabled(false);
                    howToPlayButton.setEnabled(false);
                    settings.setEnabled(false);
                    resetGameAndExit.setEnabled(false);
                }
                break;
            case MotionEvent.ACTION_DOWN: case MotionEvent.ACTION_POINTER_DOWN:
                if(levelsButton.isEnabled() && levelsButton.touched(xPosition,yPosition)){
                    levelsButton.setPressed(true);
                    levelsButton.setMotionEventID(event.getPointerId(index));
                    howToPlayButton.setEnabled(false);
                    settings.setEnabled(false);
                    resetGameAndExit.setEnabled(false);
                    exit.setEnabled(false);
                }
                if(howToPlayButton.isEnabled() && howToPlayButton.touched(xPosition,yPosition)){
                    howToPlayButton.setPressed(true);
                    howToPlayButton.setMotionEventID(event.getPointerId(index));
                    levelsButton.setEnabled(false);
                    settings.setEnabled(false);
                    resetGameAndExit.setEnabled(false);
                    exit.setEnabled(false);
                }
                if(settings.isEnabled() && settings.touched(xPosition,yPosition)){
                    settings.setPressed(true);
                    settings.setMotionEventID(event.getPointerId(index));
                    levelsButton.setEnabled(false);
                    howToPlayButton.setEnabled(false);
                    resetGameAndExit.setEnabled(false);
                    exit.setEnabled(false);
                }
                if(resetGameAndExit.isEnabled() && resetGameAndExit.touched(xPosition,yPosition)){
                    resetGameAndExit.setPressed(true);
                    resetGameAndExit.setMotionEventID(event.getPointerId(index));
                    levelsButton.setEnabled(false);
                    howToPlayButton.setEnabled(false);
                    settings.setEnabled(false);
                    exit.setEnabled(false);
                }
                if(exit.isEnabled() && exit.touched(xPosition,yPosition)){
                    exit.setPressed(true);
                    exit.setMotionEventID(event.getPointerId(index));
                    levelsButton.setEnabled(false);
                    howToPlayButton.setEnabled(false);
                    settings.setEnabled(false);
                    resetGameAndExit.setEnabled(false);
                }
                break;
            case MotionEvent.ACTION_UP: case MotionEvent.ACTION_CANCEL:  case MotionEvent.ACTION_POINTER_UP:
                if(levelsButton.isPressed() && levelsButton.getMotionEventID() ==  event.getPointerId(event.getActionIndex())) {
                    levelsButton.setPressed(false);
                    levelsButton.setMotionEventID(Constants.INVALID_POINTER_ID);
                    howToPlayButton.setEnabled(true);
                    settings.setEnabled(true);
                    resetGameAndExit.setEnabled(true);
                    exit.setEnabled(true);
                    gamePanel.enterScene(LevelsScene.SCENE_NAME);
                }
                if(howToPlayButton.isPressed() && howToPlayButton.getMotionEventID() ==  event.getPointerId(event.getActionIndex())){
                    howToPlayButton.setPressed(false);
                    howToPlayButton.setMotionEventID(Constants.INVALID_POINTER_ID);
                    levelsButton.setEnabled(true);
                    settings.setEnabled(true);
                    resetGameAndExit.setEnabled(true);
                    exit.setEnabled(true);
                    gamePanel.enterScene(HowToPlayScene.SCENE_NAME);
                }
                if(settings.isPressed() && settings.getMotionEventID() ==  event.getPointerId(event.getActionIndex())){
                    settings.setPressed(false);
                    settings.setMotionEventID(Constants.INVALID_POINTER_ID);
                    levelsButton.setEnabled(true);
                    howToPlayButton.setEnabled(true);
                    resetGameAndExit.setEnabled(true);
                    exit.setEnabled(true);
                    gamePanel.openPopUp(SettingsPopUp.POP_UP_NAME);
                }
                if(resetGameAndExit.isPressed() && resetGameAndExit.getMotionEventID() ==  event.getPointerId(event.getActionIndex())){
                    resetGameAndExit.setPressed(false);
                    resetGameAndExit.setMotionEventID(Constants.INVALID_POINTER_ID);
                    levelsButton.setEnabled(true);
                    howToPlayButton.setEnabled(true);
                    settings.setEnabled(true);
                    exit.setEnabled(true);
                    gamePanel.openPopUp(AlertPopUp.POP_UP_NAME);
                    waitingForAnswerResetAndExit = true;
                }
                if(exit.isPressed() && exit.getMotionEventID() ==  event.getPointerId(event.getActionIndex())){
                    exit.setPressed(false);
                    exit.setMotionEventID(Constants.INVALID_POINTER_ID);
                    levelsButton.setEnabled(true);
                    howToPlayButton.setEnabled(true);
                    settings.setEnabled(true);
                    resetGameAndExit.setEnabled(true);
                    gamePanel.openPopUp(AlertPopUp.POP_UP_NAME);
                    waitingForAnswerExit = true;
                }
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        background.draw(canvas);
        gameTitle.draw(canvas);
        levelsButton.draw(canvas);
        howToPlayButton.draw(canvas);
        settings.draw(canvas);
        resetGameAndExit.draw(canvas);
        exit.draw(canvas);
    }

    /**
     * when pressing the exit or reset and exit button, the update is waiting for their answer.
     */
    @Override
    public void update() {
        background.update();
        gameTitle.update();
        levelsButton.update();
        howToPlayButton.update();
        settings.update();
        resetGameAndExit.update();
        exit.update();
        if(waitingForAnswerResetAndExit){
            if(AlertPopUp.answer == AlertAnswer.YES){
                SharedPreferences.Editor editor = gamePanel.levelProgress.edit();
                editor.clear().apply();
                SharedPreferences.Editor editor1 = gamePanel.savedSettings.edit();
                editor1.clear().apply();
                MainActivity.activity.finishAffinity();
                Log.e(TAG, "update: level progress have been reseted");
            }
            waitingForAnswerResetAndExit = false;
            AlertPopUp.answer = AlertAnswer.NOT_KNOWN;
        }
        if(waitingForAnswerExit){
            if(AlertPopUp.answer == AlertAnswer.YES){
                MainActivity.activity.finishAffinity();
            }
            waitingForAnswerExit = false;
            AlertPopUp.answer = AlertAnswer.NOT_KNOWN;
        }
    }

}

