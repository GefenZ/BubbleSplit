package academy.learnprogramming.bubblesplit;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

/**
 * a class for object which are from drawable type, that use drawable.
 */
public class SingleDrawableObject extends GameObject {

    /**
     * Weather to show to object or not to
     * This is affecting the draw function
     */
    protected boolean shown = true;
    protected Drawable drawable;

    public SingleDrawableObject(Drawable drawable){
        this.drawable=drawable;
        width=drawable.getIntrinsicWidth();
        height=drawable.getIntrinsicHeight();
    }

    public void update() {
    }

    public void draw(Canvas canvas) {
        drawable.setBounds(screenRect());
        if(shown) drawable.draw(canvas);
    }

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }

    public Drawable getDrawable(){
        return drawable;
    }

    public void setDrawable(Drawable drawable){
        this.drawable=drawable;
        width=drawable.getIntrinsicWidth();
        height=drawable.getIntrinsicHeight();
    }
}
