package academy.learnprogramming.bubblesplit;

import android.util.Log;
import android.view.SurfaceHolder;

import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;

/**
 * *************************************************************
 * This class in a THREAD for creating a LOOP for screen (Game Panel) refresh - 30 times a second
 * it addresses two method on gamePanel object: "update" and "draw" methods
 * *************************************************************
 */

public class MainThread extends Thread {

    public static final String TAG = "MainThread";

//Vars that we need for our thread
    /**
     * What is FPS?, FPS=Frames per Second, a good thread runs between 30 and 60 fps
     * more fps more cpu demanding
     * in our game we will need 30 fps cause we want to have many objects
     * and our drawing object are going to have sprites.
     */
    private int FPS = 30;
    private boolean running;

    public static Queue<Long> frames;
    private long msSum = 0;

    /**
     * The class holds the "draw" calls. To draw something, you need 4 basic components:
     *    a Bitmap to hold the pixels,
     *    a Canvas to host the draw calls (writing into the bitmap),
     *    a drawing primitive (e.g. Rect, Path, text, Bitmap),
     *	  and a paint

     * Now lets create the class constructor
     * For our thread constructor we need references of our Content view objects
     * 1. a SurfaceHolder ref
     * 2. a GamePanel
     **/
    private GamePanel gamePanel;
    private SurfaceHolder surfaceHolder;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
        frames = new LinkedList<>();
    }//end of constructor

    /**
     * Now we will override the run method. All thread have a run method
     * So to get advantage of the thread we will write our time code inside the run method
     */
    @Override
    public void run() {
        /**
         * Inside the run method we want every second to catch the 30 frames
         * So the vars we will need...
         */
        long startTime;
        long timeMillis;
        long waitTime;
        int FPSCheck=0;
        long start = 0;
        
        long targetTime = 1000 / FPS;

        // if the game is in running mode
        while (running) {
            /**
             First we set the time
             Careful now our system timer start to count in nanos. 1 milli = 1000000 nanos
             We will need that later...
             */
            startTime = System.nanoTime();
//            Log.e(TAG, "1 run: " + getCurrentTime());

            /**
             *At first our canvas is blank...
             */
            GamePanel.canvas = null;
            //try locking the canvas for pixel editing
            /**
             * we need to use the canvas to paint our object on our screen every frame
             * we will develop our canvas code inside a try{}catch() in case something goes wrong
             */

            try {
                //we lock canvas to our content view
                GamePanel.canvas = this.surfaceHolder.lockCanvas();
//                Log.e(TAG, "2 run: " + getCurrentTime());
                /**
                 * and we want to be synchronized every time we update or draw
                 * something on our screen in every frame... so our game will flow naturally
                 **/
                synchronized (surfaceHolder) {

                    /**
                     * this is the game data update as for example the x and y position coordinates
                     * for a little character (Sprites positions, Score base in time, ...)
                     */
//                    Log.e(TAG, "3 run: " + getCurrentTime());
                    this.gamePanel.update();
//                    Log.e(TAG, "4 run: " + getCurrentTime());
//                    Log.d("MainThread","updated:"+startTime*1.f/1000000000);
                    /**
                     * this is about drawing the picture you see in the screen.
                     * When this method is called repeatedly it gives you
                     * the perception of a MOVIE or of an animation.
                     **/
                    this.gamePanel.draw(GamePanel.canvas);
//                    Log.e(TAG, "5 run: " + getCurrentTime());
                }//end synchronized

            } catch (Exception e) {
            }//end try
            finally {
                if (GamePanel.canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(GamePanel.canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // we will do some time calculation... The waitTime var
            // we must return back to millis again cause we need to do calculation easier!
            timeMillis = (System.nanoTime() - startTime) / 1000000;
            /**
             *The time we will wait till the next frame enter the loop
             */
            waitTime = targetTime - timeMillis;


            /**
             * while we wait WE pause the thread for
             * the waitTime between the frames
             * is so little that you cant notice it....
             **/
            try {
                this.sleep(waitTime);
            } catch (Exception e) {
            }
            frames.add(timeMillis + Math.max(0, waitTime));
            msSum += timeMillis + Math.max(0, waitTime);

            if(msSum/* - frames.peek() */> 1000){
                msSum -= frames.poll();
            }
//            Log.e(TAG, "run: FPS : " + frames.size());
//             if(start >= 1000){
//                 Log.e(TAG, "run: FPS: " + FPSCheck);
//                 start = 0;
//                 FPSCheck = 0;
//             }
//             else{
//                 FPSCheck++;
//             }
        } // end while
    } // end of run method

    public static String getCurrentTime() { return String.format(Locale.getDefault(), "%06d", (System.nanoTime() / 1000) % 100000000); }

    public void setRunning(boolean running) {
        this.running = running;
    }

}//end of thread class
