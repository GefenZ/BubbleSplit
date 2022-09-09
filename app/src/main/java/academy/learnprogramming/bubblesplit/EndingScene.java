package academy.learnprogramming.bubblesplit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.view.MotionEvent;

/**
 * The scene that is shown when you finish the game - finish all of the levels. meaning it is shown when you win the last level.
 */
public class EndingScene implements Scene {
    public static String SCENE_NAME = "EndingScene";
    public static int WIDTH, HEIGHT;

    private  GamePanel gamePanel;

    private Background background;

    private SingleImageObject endingScreen;

    private ImageButton homeButton;

    public EndingScene(GamePanel gamePanel, int width, int height){
        this.gamePanel = gamePanel;
        WIDTH=width;
        HEIGHT=height;

        background = new Background(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.background));

        endingScreen = new SingleImageObject(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.ending_screen));
        endingScreen.setX(Constants.ORIGINAL_SCREEN_WIDTH/2 - endingScreen.width/2);
        endingScreen.setY(Constants.ORIGINAL_SCREEN_HEIGHT/2 - endingScreen.height/2);

        homeButton = new ImageButton(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.home_button), 200, 200,false));
        homeButton.setX(50);
        homeButton.setY(20);
    }

    @Override
    public void terminate() {

    }

    @Override
    public void receiveTouch(MotionEvent event) {
        int action = event.getActionMasked();
        int index = event.getActionIndex() ;
        int xPosition, yPosition;

        // adjust x and y the screen resolution by dividing by the factors
        xPosition = (int) (event.getX(index) / GamePanel.scaleFactorXMul);
        yPosition = (int) (event.getY(index) / GamePanel.scaleFactorYMul);

        switch (action){
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_DOWN: case MotionEvent.ACTION_POINTER_DOWN:
                if(homeButton.touched(xPosition,yPosition)){
                    //from ending to gameplay
                    gamePanel.onBackPressed();
                    //from gameplay to levels
                    gamePanel.onBackPressed();
                    //from levels to opening-home
                    gamePanel.onBackPressed();
                }
                break;
            case MotionEvent.ACTION_UP: case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
        }
    }

    @Override
    public void update() {
        background.update();
        endingScreen.update();
        homeButton.update();
    }

    @Override
    public void draw(Canvas canvas) {
        background.draw(canvas);
        endingScreen.draw(canvas);
        homeButton.draw(canvas);
    }
}
