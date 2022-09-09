package academy.learnprogramming.bubblesplit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;

/**
 * a class for object which are from bitmap type, that use an image.
 */
public class SingleImageObject extends GameObject {
    /**
     * Weather to show to object or not to
     * This is affecting the draw function
     */
    protected boolean shown = true;

    protected Bitmap image;

    public SingleImageObject(Bitmap image){
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
    }

    public void update() {

    }

    public void draw(Canvas canvas) {
        if(shown) canvas.drawBitmap(image, innerRect(), screenRect(), null);
    }

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
