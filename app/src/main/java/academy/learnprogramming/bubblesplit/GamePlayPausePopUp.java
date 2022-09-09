package academy.learnprogramming.bubblesplit;

import android.graphics.Canvas;
import android.view.MotionEvent;

import androidx.core.content.res.ResourcesCompat;

/**
 * The pop up which is shown when pausing the game.
 */
public class GamePlayPausePopUp implements PopUp {
    public static String POP_UP_NAME = "GamePlayPausePopUp";

    private  GamePanel gamePanel;

    private SingleDrawableObject popUpBackground;

    private Button resumeButton;
    private Button settingsButton;
    private Button exitGameButton;

    public boolean waitingForAnswer = false;

    public GamePlayPausePopUp(GamePanel gamePanel){
        this.gamePanel = gamePanel;

        popUpBackground = new SingleDrawableObject(ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.pause_background, null));
        popUpBackground.setX(Constants.ORIGINAL_SCREEN_WIDTH/2 - popUpBackground.width/2);
        popUpBackground.setY(Constants.ORIGINAL_SCREEN_HEIGHT/2 - popUpBackground.height/2);

        resumeButton = new Button(ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.resume_button, null),ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.resume_button, null),ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.resume_button, null));
        resumeButton.setX(popUpBackground.x + popUpBackground.width/2 - resumeButton.width/2);
        resumeButton.setY(popUpBackground.y + 50);

        settingsButton = new Button(ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.pause_settings_button, null),ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.pause_settings_button, null),ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.pause_settings_button, null));
        settingsButton.setX(popUpBackground.x + popUpBackground.width/2 - resumeButton.width/2);
        settingsButton.setY(popUpBackground.y + 50 + resumeButton.height);

        exitGameButton = new Button(ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.exit_game_button, null),ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.exit_game_button, null),ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.exit_game_button, null));
        exitGameButton.setX(popUpBackground.x + popUpBackground.width/2 - resumeButton.width/2);
        exitGameButton.setY(popUpBackground.y + 50 + resumeButton.height + settingsButton.height);
    }

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
                break;
            case MotionEvent.ACTION_DOWN: case MotionEvent.ACTION_POINTER_DOWN:
                if(resumeButton.touched(xPosition,yPosition)){
                    gamePanel.closePopUp();
                    GamePlayScene.gameState = GameState.GAME_NOT_STARTED;
                }
                if(settingsButton.touched(xPosition,yPosition)){
                    gamePanel.openPopUp("SettingsPopUp");
                }
                if(exitGameButton.touched(xPosition,yPosition)){
                    gamePanel.openPopUp("AlertPopUp");
                    waitingForAnswer = true;
                }
                break;
            case MotionEvent.ACTION_UP: case MotionEvent.ACTION_CANCEL:  case MotionEvent.ACTION_POINTER_UP:
                break;
        }
    }

    /**
     *  wait for the user to press some button. if the user clicks on the exitGame button, an alert pop up is popped, and then if the user presses yes, meaning he want to
     *  exit the game, the pop up is closed and the game scene is closed.
     */
    @Override
    public void update() {
        popUpBackground.update();
        resumeButton.update();
        settingsButton.update();
        exitGameButton.update();
        if(waitingForAnswer){
            if(AlertPopUp.answer == AlertAnswer.YES){
                gamePanel.closePopUp();
                gamePanel.onBackPressed();
            }
            waitingForAnswer = false;
            AlertPopUp.answer = AlertAnswer.NOT_KNOWN;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        popUpBackground.draw(canvas);
        resumeButton.draw(canvas);
        settingsButton.draw(canvas);
        exitGameButton.draw(canvas);
    }
}
