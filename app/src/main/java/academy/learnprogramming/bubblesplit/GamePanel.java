package academy.learnprogramming.bubblesplit;

/**
 * *************************************************************
 * Class description:
 * <p>
 * This is the main class of our game,
 * it uses the SurfaceView class to implement screen management and touch
 * for our game, all the UI is here
 * *************************************************************
 */


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "GamePanel";

    public SharedPreferences levelProgress = this.getContext().getSharedPreferences("LevelProgress", Context.MODE_PRIVATE);
    public SharedPreferences savedSettings = this.getContext().getSharedPreferences("SavedSettings", Context.MODE_PRIVATE);

    private SceneManager sceneManager;

    private Map<String, Scene> sceneDictionary;
    private Map<String, PopUp> popUpDictionary;

    public static MediaPlayer endingSound;

    public static MediaPlayer explodeSound;

    public static MediaPlayer gameLostSound;

    /** Now we will set our game screen width and height
     * display size in pixels
     * the screen upper left corner is 0,0
     */
    public static int WIDTH;
    public static int HEIGHT;

    // canvas of the game, we draw our pictures on this CANVAS
    public static Canvas canvas;

    //the random class
    private Random rnd = new Random();

    /**
     * ******************************************************************
     *     game logic class - the "BRAIN" of our game, rules and more ...
     *     very important class - all the game algorithms are there
     */
    GameLogic gameLogic;
    /**
     * ******************************************************************
     */


    /**
     * ******************************************************************
     *     game loop class - this thread loops our game screen 30 frame for second ...
     *     very important class - it address (by its run method) the update and draw method
     *     on gamePanel methods, by that it updates all game objects parameters (and more things
     *     that are periodically) and the draw method which refreshes the screen
     */
    private MainThread thread;
    /**
     * ******************************************************************
     */


    /**
     * This (ENUM CLASS) will help us navigate in our game states
    // current game state - start of the game MESSAGE SHOWN
     */

    /** TODO later implementation of different screen different with resolutions
     * This a very important stage to be implemented - for dealing different phone with
     * different screen resolutions ...
     *
     *
     * When we are implementing graphic application we need to adjust our application
     * to other possible phone that will operate our application, which will have screen different resolutions
     * for that reason. we use a vars we call "xChangedFactor" and "yChangedFactor" (for x and y of screen)
     * We will need it on two places
     *      1. when drawing images (draw method),
     *      2 - on touching the screen (onTouchEvent) method
     * This vars will be initiated at first when we will get scaling factor of the
     * current phone screen we are using
     */
    public static float scaleFactorXMul = 1.0f;
    public static float scaleFactorYMul = 1.0f;

    //lets create the constructor of our new class,that is going to help us calling objects and methods!
    public GamePanel(Context context, int WIDTH, int HEIGHT) {
        super(context);
        construct(WIDTH, HEIGHT);
    }//end of  constructor

    /**
     * This is where we construct main things of the game. we create here the scene manager, which is responsible to switch between scenes. we also define the music here.
     * @param WIDTH
     * @param HEIGHT
     */
    public void construct(int WIDTH, int HEIGHT){
        // of phone's dimensions
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;

        sceneManager = new SceneManager();
        sceneManager.enterScene(new OpeningScene(this, WIDTH, HEIGHT));
        sceneDictionary = new HashMap<>();
        popUpDictionary= new HashMap<>();

        endingSound = MediaPlayer.create(this.getContext().getApplicationContext(), R.raw.congrats_sound);
        gameLostSound = MediaPlayer.create(getContext(), R.raw.game_lost_sound);
        explodeSound = MediaPlayer.create(getContext(), R.raw.pop_sound);

        /**
         * set the drawing scaled to the screen size WIDTH & HEIGHT, using the defaults
         * the defaults of our screen are in the Constants class
         * in this case: ORIGINAL_SCREEN_WIDTH, and ORIGINAL_SCREEN_HEIGHT
         *
         * calculate multipliers of scaleFactorX, scaleFactorY
         */

        scaleFactorXMul = WIDTH * 1.0f / Constants.ORIGINAL_SCREEN_WIDTH;
        scaleFactorYMul = HEIGHT * 1.0f / Constants.ORIGINAL_SCREEN_HEIGHT;

        // create GameLogic OBJECT
        gameLogic = new GameLogic(this);
        // create thread OBJECT
        thread = new MainThread(getHolder(), this);

        /**
         * These (callback and focusable) are layers of android construction buildings,
         * we need them for SurfaceHolder class
         * of android (SurfaceHolder is a class which inherent the View class android and add
         * more capabilities.
         *
         Callback is used for the surfaceHolder to intercept events:
         Surface objects enable apps to render images to be presented on screens.
         SurfaceHolder interfaces enable apps to edit and control surfaces.
         */
        getHolder().addCallback(this);

        /**
         make gamePanel focusable so it can handle events.
         setFocusable mainly used for enable/disable view's focus event
         on both touch mode and keypad mode( using up/down/next key).
         */
        setFocusable(true);
    }


    /**
     * This method tells us that the surface is READY,
     * so we can put object and draw them on it !
     * here all of the scenes are being created, and defined in a dictionary for easy access.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e(TAG, "surfaceCreated");
//         adding scenes to scene manager
        sceneDictionary.put(LevelsScene.SCENE_NAME, new LevelsScene(this, WIDTH, HEIGHT));
        sceneDictionary.put(GamePlayScene.SCENE_NAME,new GamePlayScene(this, WIDTH, HEIGHT));
        sceneDictionary.put(HowToPlayScene.SCENE_NAME, new HowToPlayScene(this, WIDTH, HEIGHT));
        sceneDictionary.put(EndingScene.SCENE_NAME, new EndingScene(this, WIDTH, HEIGHT));
        popUpDictionary.put(GamePlayPausePopUp.POP_UP_NAME, new GamePlayPausePopUp(this));
        popUpDictionary.put(AlertPopUp.POP_UP_NAME, new AlertPopUp(this));
        popUpDictionary.put(SettingsPopUp.POP_UP_NAME, new SettingsPopUp(this));

        SharedPreferences.Editor editor = savedSettings.edit();
        editor.putInt("MusicVolume", savedSettings.getInt("MusicVolume", 80));
        editor.putInt("MaxLevelReached", savedSettings.getInt("MaxLevelReached", 1));
        editor.apply();
        Intent i = new Intent(getContext(), MusicService.class);
        i.putExtra("currLvl", savedSettings.getInt("MaxLevelReached", 1));
        getContext().startService(i);


        // start the game loop thread
        if(thread.getState() == Thread.State.TERMINATED){
            thread = new MainThread(holder, this);
            MusicService.backgroundMusic.start();
            //Intent j = new Intent(getContext(), MusicService.class);
            //j.putExtra("currLvl", savedSettings.getInt("MaxLevelReached", 1));
            //getContext().startService(j);
            Log.e(TAG, "surfaceCreated: Thread terminated and recreated" );
        }
        thread.setRunning(true);
        thread.start();
    }

    /**
     This is called immediately after any structural changes (format or size) have been made to the surface.
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e(TAG, "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e(TAG, "surfaceDestroyed");
        try {
            thread.setRunning(false);
            thread.join();
            if(GamePlayScene.gameState == GameState.GAME_STARTED) {
                openPopUp("GamePlayPausePopUp");
                GamePlayScene.gameState = GameState.GAME_PAUSED;
            }
            getContext().stopService(new Intent(getContext(), MusicService.class));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * when a touch event happens
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        sceneManager.receiveTouch(event);
        return true;
    }

    /**
     * in content view GamePanel we must constantly update our image
     * GamePanel cooperates with our thread
     * So our game run a new game loop ....
     */
    public void update() {
        sceneManager.update();
    }//end update


    /**
     * GamePanel cooperates with our thread
     * So our game draw new screen every 33 milli seconds
     * GamePanel draw method , SurfaceView override
     *
     */
    @Override
    public void draw(Canvas canvas) {
        // pass to super class... to do its things
        super.draw(canvas); // 2 milli

        // here we start our implantation
        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);

        /** so if something appears on our screen we must scale it
         * we would like that our graphic image can scale on different android phone screen sizes
         */
        if(canvas != null){
            final int savedState = canvas.save();

            /**
             //                 * set the drawing scaled to the screen size WIDTH & HEIGHT, using the defaults
             //                 * the defaults of our screen are in the Constants class
             //                 * in this case: ORIGINAL_SCREEN_WIDTH, and ORIGINAL_SCREEN_HEIGHT
             //                 *
             //                */
            canvas.scale(scaleFactorX * scaleFactorXMul, scaleFactorY * scaleFactorYMul);

            sceneManager.draw(canvas);

            canvas.restoreToCount(savedState);
        }
    }

    /**
     * Used to enter a given scene, by it's scene name. made easier by the dictionary.
     * @param sceneName
     */
    public void enterScene(String sceneName) {
        sceneManager.enterScene(sceneDictionary.get(sceneName));
    }

    /**
     * Used to enter a given pop up, by it's pop up name. made easier by the dictionary.
     * @param popUpName
     */
    public void openPopUp(String popUpName){
        sceneManager.openPopUp(popUpDictionary.get(popUpName));
    }

    /**
     * closing a pop up
     */
    public void closePopUp(){
        sceneManager.closePopUp();
    }

    /**
     * closing a scene
     */
    public void onBackPressed(){
        sceneManager.terminate();
        sceneManager.onBackPressed();
    }

    /**
     * saving the level progress, after progressing a level
     * @param level
     */
    public void saveProgress(String level){
        SharedPreferences.Editor editor = levelProgress.edit();
        editor.putBoolean(level, true);
        editor.apply();
    }

    /**
     * setting the music volume of the background music
     * @param volume
     */
    public void setMusicVolume(int volume){
//        Log.e(TAG, "setMusicVolume: "+ volume);
        final float volume1 = (float) (1 - (Math.log(Constants.MAX_VOLUME - volume) / Math.log(Constants.MAX_VOLUME)));
        MusicService.backgroundMusic.setVolume(volume1,volume1);
        SharedPreferences.Editor editor = savedSettings.edit();
        editor.putInt("MusicVolume", volume);
        editor.apply();
    }

    /**
     * switching the sound - on or off
     * @param isSound
     */
    public void setSound(boolean isSound){
        SharedPreferences.Editor editor = savedSettings.edit();
        editor.putBoolean("Sound", isSound);
        editor.apply();
    }

    /**
     * switching whether the hit box is shown or not
     * @param isShown
     */
    public void setHitBox(boolean isShown){
        SharedPreferences.Editor editor = savedSettings.edit();
        editor.putBoolean("HitBox", isShown);
        editor.apply();
    }


    /**
     * show message of state:
     * in this method we can implement different popup message by using
     * a Paint class to write something, and more...
     */

    /**
     * setter and Getters **********************************************************
     *
     */
    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public static void setWIDTH(int WIDTH) {
        GamePanel.WIDTH = WIDTH;
    }


}//end of class
