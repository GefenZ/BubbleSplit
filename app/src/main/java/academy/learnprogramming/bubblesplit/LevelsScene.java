package academy.learnprogramming.bubblesplit;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

/**
 * This is the levels scene. it is build by an array list of buttons.
 */
public class LevelsScene implements Scene {

    public static String SCENE_NAME = "LevelsScene";
    public static int WIDTH,HEIGHT;

    private GamePanel gamePanel;

    private Background background;

    private ImageButton backButton;

    private SingleDrawableObject levelsScreen;

    private SingleImageObject levelsTitle;

    public static ArrayList<Button> buttons;

    private int[] levelDrawablesIDS;

    public LevelsScene(GamePanel gamePanel, int width, int height){
        this.gamePanel=gamePanel;
        this.WIDTH=width;
        this.HEIGHT=height;

        SharedPreferences.Editor editor = gamePanel.levelProgress.edit();
        editor.putBoolean("1", true);
        editor.apply();

        background = new Background(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.background));

        backButton = new ImageButton(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.back_button), 200, 200,false));
        backButton.setX(50);
        backButton.setY(20);

        levelsScreen = new SingleDrawableObject(ResourcesCompat.getDrawable(gamePanel.getResources(), R.drawable.levels_screen, null));
        levelsScreen.setX((Constants.ORIGINAL_SCREEN_WIDTH/8));
        levelsScreen.setY((Constants.ORIGINAL_SCREEN_HEIGHT/8) );

        levelsTitle = new SingleImageObject(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.levels_title));
        levelsTitle.setX((Constants.ORIGINAL_SCREEN_WIDTH/8) + 100);
        levelsTitle.setY((Constants.ORIGINAL_SCREEN_HEIGHT/8) + 100 );

        levelDrawablesIDS= new int[] {R.drawable.level_1_button, R.drawable.level_2_button, R.drawable.level_3_button, R.drawable.level_4_button, R.drawable.level_5_button, R.drawable.level_6_button, R.drawable.level_7_button, R.drawable.level_8_button, R.drawable.level_9_button, R.drawable.level_10_button, R.drawable.level_11_button, R.drawable.level_12_button, R.drawable.level_13_button, R.drawable.level_14_button};

        buttons=new ArrayList<>();
        for(int i=0;i<14;i++){
            buttons.add(new Button(ResourcesCompat.getDrawable(gamePanel.getResources(),levelDrawablesIDS[i], null),ResourcesCompat.getDrawable(gamePanel.getResources(),levelDrawablesIDS[i], null),ResourcesCompat.getDrawable(gamePanel.getResources(),levelDrawablesIDS[i], null)));
            buttons.get(i).setX((i<=6?Constants.ORIGINAL_SCREEN_WIDTH/8 + i*200 +100:Constants.ORIGINAL_SCREEN_WIDTH/8 + (i-7)*200 +100));
            buttons.get(i).setY((i<=6?Constants.ORIGINAL_SCREEN_HEIGHT/8 +300:Constants.ORIGINAL_SCREEN_HEIGHT/8 +500));
            buttons.get(i).setShown(false);
            boolean defaultValue = false;
            if(gamePanel.levelProgress.getBoolean(String.valueOf(i+1), defaultValue)) {buttons.get(i).setShown(true);}
        }
    }

    @Override
    public void terminate() {

    }

    /**
     * we check by a for loop which button is being pressed when the user presses some button.
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
                break;
            case MotionEvent.ACTION_DOWN:  case MotionEvent.ACTION_POINTER_DOWN:
                if(backButton.touched(xPosition,yPosition)){
                    gamePanel.onBackPressed();
                }
                for(int i=0;i<buttons.size();i++) {
                    if (buttons.get(i).touched(xPosition, yPosition) && buttons.get(i).shown) {
                        GamePlayScene.currentLevel = i+1;
                        gamePanel.enterScene(GamePlayScene.SCENE_NAME);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
    }

    /**
     * draw a button only if it shown, that means only if the user unlocked that level.
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        background.draw(canvas);
        backButton.draw(canvas);
        levelsScreen.draw(canvas);
        levelsTitle.draw(canvas);
        for(int i=0;i<buttons.size();i++) {
            if(buttons.get(i).shown) buttons.get(i).draw(canvas);
        }
    }/**
     * update a button only if it shown, that means only if the user unlocked that level.
     */

    @Override
    public void update() {
        background.update();
        backButton.update();
        levelsScreen.update();
        levelsTitle.update();
        for(int i=0;i<buttons.size();i++) {
            if(buttons.get(i).shown) buttons.get(i).update();
        }
    }
}
