package academy.learnprogramming.bubblesplit;

import android.graphics.Bitmap;

/**
 * A button which is just a button with an image, that can be pressed.
 */
public class ImageButton extends SingleImageObject {

    public ImageButton(Bitmap image){
        super(image);
    }

    public boolean touched(int xPosition, int yPostion){
        if(xPosition>=x &&xPosition <= x + width && yPostion>= y && yPostion <= y + height)
            return true;
        return false;
    }
}
