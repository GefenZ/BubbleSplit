package academy.learnprogramming.bubblesplit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

import androidx.core.content.res.ResourcesCompat;

/**
 * the class for the alert pop up, which pops up as an alert dialog
 */
public class AlertPopUp implements PopUp{
    public static String POP_UP_NAME = "AlertPopUp";

    private  GamePanel gamePanel;

    private SingleDrawableObject alertPopUp;

    private SingleImageObject alertText;

    private ImageButton alertYes;

    private  ImageButton alertNo;

    public static AlertAnswer answer;

    /**
     * the alert pop up defines the answer attribute as not known, as when the pop up is created at the start of the game it is not shown yet, and because the pop up was not called we
     * simply can't tell what is the answer, so at the moment it is set to not known.
     * @param gamePanel
     */
    public AlertPopUp(GamePanel gamePanel){
        this.gamePanel = gamePanel;

        alertPopUp = new SingleDrawableObject(ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.alert_pop_up, null));
        alertPopUp.setX(Constants.ORIGINAL_SCREEN_WIDTH/2 - alertPopUp.width/2);
        alertPopUp.setY(Constants.ORIGINAL_SCREEN_HEIGHT/2 - alertPopUp.height/2);

        alertText = new SingleImageObject(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.alert_text), 600, 200, false));
        alertText.setX(alertPopUp.x + 50);
        alertText.setY(alertPopUp.y + 50);

        alertYes = new ImageButton(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.alert_yes), 200, 100, false));
        alertYes.setX(alertPopUp.x + 50);
        alertYes.setY(alertPopUp.y + alertPopUp.height - 150);

        alertNo = new ImageButton(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.alert_no), 200, 100, false));
        alertNo.setX(alertPopUp.x + alertPopUp.width - 250);
        alertNo.setY(alertPopUp.y + alertPopUp.height - 150);

        answer = AlertAnswer.NOT_KNOWN;
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
                if(alertYes.touched(xPosition, yPosition)){
                    answer = AlertAnswer.YES;
                    gamePanel.closePopUp();
                }
                if(alertNo.touched(xPosition,yPosition)){
                    answer = AlertAnswer.NO;
                    gamePanel.closePopUp();
                }
                break;
            case MotionEvent.ACTION_UP: case MotionEvent.ACTION_CANCEL:  case MotionEvent.ACTION_POINTER_UP:
                break;
        }
    }

    @Override
    public void update() {
        alertPopUp.update();
        alertText.update();
        alertYes.update();
        alertNo.update();
    }

    @Override
    public void draw(Canvas canvas) {
        alertPopUp.draw(canvas);
        alertText.draw(canvas);
        alertYes.draw(canvas);
        alertNo.draw(canvas);
    }
}
