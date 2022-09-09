package academy.learnprogramming.bubblesplit;

import android.graphics.Canvas;
import android.view.MotionEvent;
/**
 * the scene interface, which contains the needed methods for a scene.
 */
public interface Scene {
    public void update();
    public void draw(Canvas canvas);
    public void terminate();
    public void receiveTouch(MotionEvent event);
}
