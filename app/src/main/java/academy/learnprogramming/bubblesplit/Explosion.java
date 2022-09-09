package academy.learnprogramming.bubblesplit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

/**
 * The explosion that is played when a bubble gets hit.
 */
public class Explosion extends AnimatedSpritesObject {

    public static final String TAG ="Explosion";

    public boolean playedOnce = false;

    public Explosion(Bitmap imageThatHasSprites, int numberOfSprites, int rowLength){
        super(imageThatHasSprites, numberOfSprites, rowLength);
    }

    /**
     * in the update I made sure that the explosion is played exactly one time.
     * I make sure exactly one round of sprite is being played.
     */
    @Override
    public void update() {
        super.update();
        playedOnce = animation.playedOnce;
        animation.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
}
