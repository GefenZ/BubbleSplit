package academy.learnprogramming.bubblesplit;

import android.graphics.drawable.Drawable;

/**
 * the bullet that the main character shoots in order to hit t he bouncing bubbles.
 */
public class Bullet extends Circle {

    private long startTime;

    public Bullet(Drawable drawable){
        super(drawable);

        startTime = System.nanoTime();
    }

    /**
     * when the game is stopped and than resumed, we want to make sure we reset the animation, because if we don't the bullet takes a big leap forward in time because the time keeps on ticking while
     * the game is stopped.
     */
    public void resetAnimation(){
        shown= true;
        startTime= System.nanoTime();
    }

    /**
     * the bullets speed is linear, it is just going up at a regular speed, and we make it happen in update.
     */
    @Override
    public void update() {
        long elapsed = (System.nanoTime() - startTime) / 1000000;

        y-=  elapsed;

        startTime = System.nanoTime();
    }
}
