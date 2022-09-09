package academy.learnprogramming.bubblesplit;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import academy.learnprogramming.bubblesplit.SingleDrawableObject;

/**
 * This is a button which is a drawable type. it has an enabled drawable plus a pressed drawable, in  order to make pressing affect.
 */
public class Button extends SingleDrawableObject {

    private  boolean enabled = true,  pressed = false;

    private int motionEventID;

    private Drawable enabledDrawable, disabledDrawable, pressedDrawable;

    /**
     * attaching each drawable to its corresponding type.
     * @param enabledDrawable
     * @param disabledDrawable
     * @param pressedDrawable
     */
    public Button(Drawable enabledDrawable, Drawable disabledDrawable, Drawable pressedDrawable){
        super(enabledDrawable);
        this.enabledDrawable = enabledDrawable;
        this.disabledDrawable = disabledDrawable;
        this.pressedDrawable = pressedDrawable;
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
     * if the button is touched within it's borders
     * @param xPosition
     * @param yPostion
     * @return
     */
    public boolean touched(int xPosition, int yPostion){
        if(xPosition>=x &&xPosition <= x + width && yPostion>= y && yPostion <= y + height)
            return true;
        return false;
    }

    /**
     * if the button is pressed, we need to change it is drawable to the pressed drawable, to create an affect of pressing when the button is touched.
     * @param pressed
     * @return
     */
    public boolean setPressed(boolean pressed) {
        if(enabled){
            if(pressed){
                setDrawable(pressedDrawable);
            }
            else if(this.pressed){
                setDrawable(enabledDrawable);
            }
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

    public int getMotionEventID() {
        return motionEventID;
    }

    public void setMotionEventID(int motionEventID) {
        this.motionEventID = motionEventID;
    }
}
