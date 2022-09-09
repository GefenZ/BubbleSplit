package academy.learnprogramming.bubblesplit;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * the pop up interface, which contains the needed methods for a pop up.
 */
public interface PopUp {
    public void update();
    public void draw(Canvas canvas);
    public void receiveTouch(MotionEvent event);
}
