package academy.learnprogramming.bubblesplit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * This is the scene for how to play. it explains in it the rules of the game.
 */
public class HowToPlayScene implements Scene{

    public static String SCENE_NAME = "HowToPlayScene";
    public static int WIDTH, HEIGHT;

    private  GamePanel gamePanel;

    private Background background;

    private SingleImageObject howToPlayScreen;

    private ImageButton backButton;

    public HowToPlayScene(GamePanel gamePanel, int width, int height){
        this.gamePanel = gamePanel;
        WIDTH=width;
        HEIGHT=height;

        background = new Background(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.background));


        howToPlayScreen = new SingleImageObject(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.how_to_play_screen));
        howToPlayScreen.setX(Constants.ORIGINAL_SCREEN_WIDTH/2 - howToPlayScreen.width/2);
        howToPlayScreen.setY(Constants.ORIGINAL_SCREEN_HEIGHT/2 - howToPlayScreen.height/2);

        backButton = new ImageButton(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.back_button), 200, 200,false));
        backButton.setX(50);
        backButton.setY(20);
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
                if(backButton.touched(xPosition,yPosition)){
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
        backButton.update();
        howToPlayScreen.update();
    }

    @Override
    public void draw(Canvas canvas) {
        background.draw(canvas);
        backButton.draw(canvas);
        howToPlayScreen.draw(canvas);
    }
}
