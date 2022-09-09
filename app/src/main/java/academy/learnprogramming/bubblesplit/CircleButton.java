package academy.learnprogramming.bubblesplit;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;

import androidx.activity.OnBackPressedDispatcher;

/**
 * a button that has some drawables for the pressed affect, and is a circle
 */
public class CircleButton extends Circle {

    private static final String TAG = "CircleButton";
    private long startTime;
    private  boolean enabled = true,  pressed = false;

    private int motionEventID;

    private Drawable enabledDrawable, disabledDrawable, pressedDrawable;

    private OnButtonTouchListener onButtonTouchListener;

    /**
     * attaching each drawable to its corresponding type.
     * @param enabledDrawable
     * @param disabledDrawable
     * @param pressedDrawable
     */
    public CircleButton(Drawable enabledDrawable, Drawable disabledDrawable, Drawable pressedDrawable){
        super(enabledDrawable);
        this.enabledDrawable = enabledDrawable;
        this.disabledDrawable = disabledDrawable;
        this.pressedDrawable = pressedDrawable;

        startTime = System.nanoTime();
    }

    @Override
    public void draw(Canvas canvas) {
        drawable.setBounds(screenRect());
        if(shown) drawable.draw(canvas);
    }

    public boolean isPressed() {
        return pressed;
    }
    /**
     * if the button is pressed, we need to change it is drawable to the pressed drawable, to create an affect of pressing when the button is touched.
     * @param pressed
     * @return
     */
    public boolean setPressed(boolean pressed) {
        if(enabled && onButtonTouchListener != null){
            if(pressed){
                setDrawable(pressedDrawable);
                onButtonTouchListener.onTouchDown();
            }
            else if(this.pressed){
                onButtonTouchListener.onTouchUp();
                setDrawable(enabledDrawable);
            }
        }
        else if(enabled) {
            setDrawable(enabledDrawable);
        }
        else return false;
        this.pressed = pressed;
        return true;
    }


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if(enabled && !pressed) setDrawable(enabledDrawable);
        else{
            setDrawable(disabledDrawable);
            pressed = false;
        }
        this.enabled = enabled;
    }

    public Drawable getEnabledDrawable() {
        return enabledDrawable;
    }

    public void setEnabledDrawable(Drawable enabledDrawable) {
        this.enabledDrawable = enabledDrawable;
    }

    public Drawable getPressedDrawable() {
        return pressedDrawable;
    }

    public void setPressedDrawable(Drawable pressedDrawable) {
        this.pressedDrawable = pressedDrawable;
    }

    public Drawable getDisabledDrawable() {
        return disabledDrawable;
    }

    public void setDisabledDrawable(Drawable disabledDrawable) {
        this.disabledDrawable = disabledDrawable;
    }

    public OnButtonTouchListener getOnButtonTouchListener() {
        return onButtonTouchListener;
    }

    public void setOnButtonTouchListener(OnButtonTouchListener onButtonTouchListener) {
        this.onButtonTouchListener = onButtonTouchListener;
    }

    public int getMotionEventID() {
        return motionEventID;
    }

    public void setMotionEventID(int motionEventID) {
        this.motionEventID = motionEventID;
    }

    /**
     * this is a built in interface to make the process of defining the function of a button easier. with this interface, you can implement what the button does where you define it. in my case, I defined
     * what the circle buttons does in character class.
     */
    public interface OnButtonTouchListener {
        void onTouchDown();
        void onTouchUp();
    }
}
