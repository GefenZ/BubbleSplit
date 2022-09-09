package academy.learnprogramming.bubblesplit;

import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * the bubble class extends circle, because is is a drawable in the shape of a circle.
 */
public class Bubble extends Circle {
    private static String TAG = "Bubble";

    private long startTime;

    private int dy;   //jump speed of bubble
    private int miny;
    private int dx;   // movement speed of bubble
    private int gravity;

    public BubbleSize bubbleSize;

    private Runnable runnable;
    public boolean isOnSplit;
    private long splitTimer;

    /**
     * the constructor of a bubble.
     * @param drawable - the drawable object of the bubble, there are 4 types - big, medium, small and very small
     * @param dy - the speed in the y axis
     * @param dx - the speed in the x axis
     * @param gravity - amount of gravity
     * @param bubbleSize - the bubble size, again there are 4 types
     * @param startingX - the starting x position of the bubble
     * @param startingY - the starting y position of the bubble
     */
    public Bubble(Drawable drawable, int dy,int dx, int gravity, BubbleSize bubbleSize, int startingX, int startingY){
        super(drawable);
        this.dy= dy;
        this.miny=this.dy;
        this.dx= dx;
        this.gravity= gravity;
        this.bubbleSize = bubbleSize;
        this.x=startingX;
        this.y=startingY;

        startTime=System.nanoTime();
    }

    /**
     * when the game is stopped and than resumed, we want to make sure we reset the animation, because if we don't the bubble takes a big leap forward in time because the time keeps on ticking while
     * the game is stopped.
     */
    public void resetAnimation(){
        shown= true;
        startTime= System.nanoTime();
    }

    /**
     * this is where all the gravity is implied. there are some cases it takes care of :
     * 1. when the bubble is right now splitting, meaning it has bean hit. we want to make sure gravity is working in that case, so we take care of dy - the speed of y.
     * 2. when the bubble is hitting the ground, we need to reverse it's speed of y to make a bouncing affect.
     * 3/ when the bubble is hitting the walls, we reverse it's speed of x, again to make a bouncing affect.
     */
    @Override
    public void update() {
        long elapsed = (System.nanoTime() - startTime) / 1000000;
        if(isOnSplit) {
            switch (bubbleSize){
                case BIG:
                    dy = -200;
                    break;
                case MEDIUM:
                    dy = -300;
                    break;
                case SMALL:
                    dy = -400;
                    break;
                case VERYSMALL:
                    dy = -600;
                    break;
            }
            isOnSplit = false;
        }
        if (this.y + this.radius > Constants.GAMEPLAY_GROUND_Y) {
            dy = -dy;
        } else {
            dy += (gravity * elapsed) / 100;
        }
        this.y += (dy * elapsed) / 1000;
        if (this.y + this.radius > Constants.GAMEPLAY_GROUND_Y) {
            this.y = Constants.GAMEPLAY_GROUND_Y - this.radius;
            dy = -miny;
        }
        if (this.x + this.radius > Constants.ORIGINAL_SCREEN_WIDTH || this.x < this.radius) {
            this.x = (this.x < this.radius ? this.radius : Constants.ORIGINAL_SCREEN_WIDTH - this.radius);
            dx = -dx;
        }
        this.x += (dx * elapsed) / 1000;


        startTime = System.nanoTime();
    }
}
