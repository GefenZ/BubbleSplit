package academy.learnprogramming.bubblesplit;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;

import androidx.core.content.res.ResourcesCompat;

/**
 * the scene of the settings. here you can decide the settings of the game, like music or sounds. all of the details you set are
 * of course also gets saved in the shared preferences data base.
 */
public class SettingsPopUp implements PopUp {
    private static final String TAG = "SettingsPopUp";
    public static String POP_UP_NAME = "SettingsPopUp";

    private  GamePanel gamePanel;

    private SingleDrawableObject popUpBackground;

    private SingleImageObject popUpTitle;

    private Button musicButton;
    private SingleImageObject musicVolumeLine;

    private Button soundButton;

    private Button hitBoxButton;

    private ImageButton backButton;

    public SettingsPopUp(GamePanel gamePanel){
        this.gamePanel = gamePanel;

        popUpBackground = new SingleDrawableObject(ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.settings_popup_background, null));
        popUpBackground.setX(Constants.ORIGINAL_SCREEN_WIDTH/2 - popUpBackground.width/2);
        popUpBackground.setY(Constants.ORIGINAL_SCREEN_HEIGHT/2 - popUpBackground.height/2);

        popUpTitle = new SingleImageObject(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.settings_pop_up_text),500 ,150 , false));
        popUpTitle.setX(popUpBackground.x +popUpBackground.width/2 -popUpTitle.width/2);
        popUpTitle.setY(popUpBackground.y + 50);

        musicVolumeLine = new SingleImageObject(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.music_volume_line),1000,45,false));
        musicVolumeLine.setX(popUpBackground.x + 120);
        musicVolumeLine.setY(popUpTitle.y + 325);

        musicButton = new Button(ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.music_button_not_pressed, null),ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.music_button_not_pressed, null),ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.music_button_pressed, null));
        musicButton.setX( (98*gamePanel.savedSettings.getInt("MusicVolume", 80))/10 + (musicVolumeLine.x - 40));
        musicButton.setY(popUpTitle.y + 300);

        SharedPreferences.Editor editor = gamePanel.savedSettings.edit();
        Log.e(TAG, "SettingsPopUp: "+gamePanel.savedSettings.getBoolean("Sound",true));
        soundButton = new Button(ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.sound_button_not_pressed, null),ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.sound_button_not_pressed, null),ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.sound_button_pressed, null));
        editor.putBoolean("Sound", gamePanel.savedSettings.getBoolean("Sound",true));
        editor.apply();
        if(!gamePanel.savedSettings.getBoolean("Sound",true)){
            soundButton.setPressed(true);
        }
        soundButton.setX(popUpBackground.x + 600 );
        soundButton.setY(popUpTitle.y + 450);

        hitBoxButton = new Button(ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.sound_button_not_pressed, null),ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.sound_button_not_pressed, null),ResourcesCompat.getDrawable(gamePanel.getResources(),R.drawable.sound_button_pressed, null));
        editor.putBoolean("HitBox", gamePanel.savedSettings.getBoolean("HitBox",false));
        editor.apply();
        if(!gamePanel.savedSettings.getBoolean("HitBox",false)){
            hitBoxButton.setPressed(true);
        }
        hitBoxButton.setX(popUpBackground.x + 600 );
        hitBoxButton.setY(popUpTitle.y + 600);

        backButton = new ImageButton(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.back_button), 200, 200,false));
        backButton.setX(popUpBackground.x + 50);
        backButton.setY(popUpBackground.y + 20);
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
                if(musicButton.isPressed()){
                    if(xPosition >= musicVolumeLine.x - 40&& xPosition <= musicVolumeLine.x + musicVolumeLine.width - 60 ) musicButton.setX(xPosition);
                    else if(xPosition < musicVolumeLine.x - 40 ) musicButton.setX(musicVolumeLine.x - 40);
                    else if(xPosition > musicVolumeLine.x + musicVolumeLine.width - 60) musicButton.setX(musicVolumeLine.x + musicVolumeLine.width - 60);
//                    Log.e(TAG, "receiveTouch: " + (musicButton.x - (musicVolumeLine.x - 40)));
//                    Log.e(TAG, "receiveTouch: " + ((10*(musicButton.x - (musicVolumeLine.x - 40)))/98));
                    gamePanel.setMusicVolume((int)((10*(musicButton.x - (musicVolumeLine.x - 40)))/98));
                }
                break;
            case MotionEvent.ACTION_DOWN: case MotionEvent.ACTION_POINTER_DOWN:
                if(backButton.touched(xPosition,yPosition)){
                    gamePanel.closePopUp();
                }
                if(musicButton.touched(xPosition,yPosition)){
                    musicButton.setPressed(true);
                    musicButton.setMotionEventID(event.getPointerId(index));
                }
                if(soundButton.touched(xPosition,yPosition)){
                    if(gamePanel.savedSettings.getBoolean("Sound",false)){
                        soundButton.setPressed(true);
                    }else{
                        soundButton.setPressed(false);
                    }
                    soundButton.setMotionEventID(event.getPointerId(index));
                    gamePanel.setSound(!gamePanel.savedSettings.getBoolean("Sound",false));
//                    Log.e(TAG, "SettingsPopUp: "+sharedPreferences.getBoolean("Sound",true));
                }
                if(hitBoxButton.touched(xPosition,yPosition)){
                    if(gamePanel.savedSettings.getBoolean("HitBox",false)){
                        hitBoxButton.setPressed(true);
                    }else{
                        hitBoxButton.setPressed(false);
                    }
                    hitBoxButton.setMotionEventID(event.getPointerId(index));
                    gamePanel.setHitBox(!gamePanel.savedSettings.getBoolean("HitBox",false));
//                    Log.e(TAG, "SettingsPopUp: "+sharedPreferences.getBoolean("Sound",true));
                }
                break;
            case MotionEvent.ACTION_UP: case MotionEvent.ACTION_CANCEL:  case MotionEvent.ACTION_POINTER_UP:
                if(musicButton.isPressed() && musicButton.getMotionEventID() == event.getPointerId(event.getActionIndex())){
                    musicButton.setPressed(false);
                    musicButton.setMotionEventID(Constants.INVALID_POINTER_ID);
                }
                break;
        }
    }

    /**
     * drawing the current fps which the game is running on.
     * @param canvas
     */
    public void drawCurrentFPS(Canvas canvas){
        Paint FPSText = new Paint();
        FPSText.setColor(Color.WHITE);
        FPSText.setStyle(Paint.Style.FILL);

        FPSText.setColor(Color.BLACK);
        FPSText.setTextSize(50);
//        FPSText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        Rect levelNumberTextBounds = new Rect();
        FPSText.getTextBounds(String.valueOf(MainThread.frames.size()), 0, 1, levelNumberTextBounds);
        canvas.drawText("FPS: " + (MainThread.frames.size()), popUpBackground.x + popUpBackground.width -levelNumberTextBounds.exactCenterX()  - 200, popUpBackground.y + 150, FPSText);
    }

    /**
     * drawing the music volume which the user set.
     * @param canvas
     */
    public void drawCurrentMusicVolume(Canvas canvas){
        Paint musicTitle = new Paint();
        musicTitle.setColor(Color.WHITE);
        musicTitle.setStyle(Paint.Style.FILL);

        musicTitle.setColor(Color.BLACK);
        musicTitle.setTextSize(50);
        musicTitle.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Music:" , popUpBackground.x + 150, popUpTitle.y + 250, musicTitle);

        Paint musicVolume = new Paint();

        musicVolume.setColor(Color.WHITE);
        musicVolume.setStyle(Paint.Style.FILL);

        musicVolume.setColor(Color.BLACK);
        musicVolume.setTextSize(50);
//        FPSText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        Rect levelNumberTextBounds = new Rect();
        musicVolume.getTextBounds(String.valueOf((int)(musicButton.x - (popUpBackground.x+50))/10), 0, 1, levelNumberTextBounds);
        canvas.drawText("volume: " + ((int)Math.ceil((10*(musicButton.x - (musicVolumeLine.x - 40)))/98)), popUpBackground.x + popUpBackground.width -levelNumberTextBounds.exactCenterX()  - 300, popUpTitle.y + 250, musicVolume);
    }

    /**
     * drawing the sound text.
     * @param canvas
     */
    public void drawSound(Canvas canvas){
        Paint soundTitle = new Paint();
        soundTitle.setColor(Color.WHITE);
        soundTitle.setStyle(Paint.Style.FILL);

        soundTitle.setColor(Color.BLACK);
        soundTitle.setTextSize(50);
        soundTitle.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Sound:" , popUpBackground.x + 150, popUpTitle.y + 500, soundTitle);
    }

    /**
     * drawing the hit box text.
     * @param canvas
     */
    public void drawHitBox(Canvas canvas){
        Paint soundTitle = new Paint();
        soundTitle.setColor(Color.WHITE);
        soundTitle.setStyle(Paint.Style.FILL);

        soundTitle.setColor(Color.BLACK);
        soundTitle.setTextSize(50);
        soundTitle.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Show Hitbox:" , popUpBackground.x + 150, popUpTitle.y + 650, soundTitle);
    }

    @Override
    public void update() {
//        Log.e(TAG, "update: "+ ((musicVolumeLine.x + musicVolumeLine.width - 60) - (musicVolumeLine.x - 40)));
//        Log.e(TAG, "update: " + musicVolumeLine.width);
        popUpBackground.update();
        popUpTitle.update();
        musicVolumeLine.update();
        musicButton.update();
        soundButton.update();
        hitBoxButton.update();
        backButton.update();
    }

    /**
     * here we also draw all the draw methods from earlier.
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        popUpBackground.draw(canvas);
        popUpTitle.draw(canvas);
        musicVolumeLine.draw(canvas);
        musicButton.draw(canvas);
        soundButton.draw(canvas);
        hitBoxButton.draw(canvas);
        backButton.draw(canvas);
        drawCurrentFPS(canvas);
        drawCurrentMusicVolume(canvas);
        drawSound(canvas);
        drawHitBox(canvas);
    }
}
