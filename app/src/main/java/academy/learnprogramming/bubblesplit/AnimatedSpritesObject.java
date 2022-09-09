package academy.learnprogramming.bubblesplit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;


public class AnimatedSpritesObject extends GameObject {

    // show flag - set default true, so we can see the image
    private boolean show = true;

    // this class uses the Animation class, so we make a reference to a new Animation
    protected Animation animation = new Animation();

    // set an array of bitmaps for our sprites
    Bitmap[] spritesArray;
    // save bitmap  - this is used for rotate method
    Bitmap[] saveSpritesArray;

    // bitmap image from where we cut our sprites
    private Bitmap spriteSheet;

    // start time pointer, for many uses
    private long startTime;

    /** So to create an animated object
     * first we pass the bit map image (the image that has the sprites) - imageThatHasSprites
     * the dimensions of each sprite we get from the bitmap image
     * numberOfSprites -  number Of Sprites in the all image
     * rowLength - number of sprites in each row
     * so now we can cut each sprite from the image and save it in an array of bitmap (spritesArray)
     * and pass this array to the animation class
     */
    public AnimatedSpritesObject(Bitmap imageThatHasSprites, int numberOfSprites, int rowLength) {

        /**
         * first calculate:
         * The dimensions of each sprite in the image - width and height -
         * from rowLength and numberOfSprites
         * so we can create a bitmap
         * for each sprite
         */
        height = imageThatHasSprites.getHeight() /(numberOfSprites/rowLength);
        width = imageThatHasSprites.getWidth() / rowLength;

        // set an array of bitmaps for our sprites
        spritesArray = new Bitmap[numberOfSprites];

        int row = 0;

        //loop
        for (int i = 0; i < spritesArray.length; i++) {
            /**
             *Our image has XxX sprites so [0] is the first sprite,  [1] the second,  [2] the third ...
             */
            if (i % rowLength == 0 && i > 0) row++;
            spritesArray[i] = Bitmap.createBitmap(imageThatHasSprites, (i - (rowLength * row)) * width, row * height,
                    width, height);
        }


        // now that we know all the info about the bitmap image
        // we need to set the animation and the delay

        // init to row 0
        animation.setSpriteRow(0);
        animation.setRowLength(rowLength);
        // pass the sprites array
        animation.setSprites(spritesArray);
        saveSpritesArray = spritesArray;
        animation.setDelay(100);

        //Now we initiate the timer so we can use in the update method
        startTime = System.nanoTime();
    }//end constructor

    // update method
    public void update() {
        /*
        TODO later
        // here is the timer of our player millis
        long elapsed = (System.nanoTime() - startTime) / 1000000;

        // Now when our timer gets past xxx millis we want the to do something
        if (elapsed > 100) {
            // here is our code


            // set back start time
            startTime = System.nanoTime();
        }
        // update the animation
        animation.update();
        */
    }//end update


    public boolean touched(int xPosition, int yPosition){
        if(xPosition>=x &&xPosition <= x + width && yPosition>= y && yPosition <= y + height)
            return true;
        return false;
    }


    /**
     * draw our object on the canvas (screen)
     */
    public void draw(Canvas canvas) {
        if (show) canvas.drawBitmap(animation.getImage(), x, y, null);
    }//end draw


    // this will flip the image - horizontally if True, Vertically if False
    public void flipImage(boolean horizontally, boolean vertically) {
        Bitmap[] bOutput = new Bitmap[spritesArray.length];
        Matrix matrix = new Matrix();

        if (horizontally)
            matrix.postScale(-1f, 1f);
        else
            matrix.postScale(1f, -1f);

        for (int i = 0; i < spritesArray.length; i++) {
            bOutput[i] = Bitmap.createBitmap(spritesArray[i], 0, 0, spritesArray[i].getWidth(),
                    spritesArray[i].getHeight(), matrix, true);
        }
        spritesArray = bOutput;
        animation.setSprites(spritesArray);
    }

    /** this will rotate the image clockwise, by "angle"
     * pay attention:
     * the rotation is always done from the base original image "spritesArray", this is the reason why we
     * use the "saveSpritesArray" to save the base original image to it, and when we call this method it
     * always start to rotate from the original image
     *
     * @param angle
     */
    public void rotateImage(float angle) {
        spritesArray = saveSpritesArray;
        Bitmap[] bOutput = new Bitmap[spritesArray.length];
        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, 1.0f);
        matrix.setRotate(angle);
        for (int i = 0; i < spritesArray.length; i++) {
            Log.e("gureli", "rotateImage 1: " + width + ":" + height);
            bOutput[i] = Bitmap.createBitmap(spritesArray[i], 0, 0, spritesArray[i].getWidth(),
                    spritesArray[i].getHeight(), matrix, true);
            Log.e("gureli", "rotateImage 2: " + bOutput[i].getWidth() + ":" + bOutput[i].getHeight());
        }
        spritesArray = bOutput;
        animation.setSprites(spritesArray);
    }

    /**
     * Setters and Getters *******************************************************************
     */
    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    // this will retrieve the current sprite image
    public Bitmap getImage() {
        return animation.getImage();
    }

}
