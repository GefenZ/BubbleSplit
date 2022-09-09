package academy.learnprogramming.bubblesplit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;

import java.util.Random;
import java.util.Set;

/**
 * * *****************************************************************
 * * Class description:
 * * this class GameLogic class, all the logic of the game, the game rules and BRAIN
 * * are handled here !!!
 * * *****************************************************************
 */

public class GameLogic {

    public static final String TAG = "GameLogic";

    private GamePanel gamePanel;

    // random for dices roll results
    Random random = new Random();

    // random number we get 1..6
    int randomNumber;

    /**
     * Constructor
     */
    public GameLogic(GamePanel gamePanel) {
        // here we point to gamePanel object
        this.gamePanel = gamePanel;
    }

    /**
     * Calculate the distance between two given points with the formula of distance.
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @return
     */
    public static double distanceBetweenTwoPoints(double x, double y, double x1, double y1) {
        return Math.sqrt(((x-x1)*(x-x1))+((y-y1)*(y-y1)));
    }




    /**********************************************************************************************
     * COLLISION DETECTION METHODS
     * ********************************************************************************************
     *
     * check for collision between two image objects
     * in these methods with overloading methods, we check a collision without the transparent
     * areas of the image, and also, we can scale the touching area
     */

    /**
     *
     * @param bm the bitmap image for resizing
     * @param newWidth resize size width
     * @param newHeight resize size height
     * @return
     */
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    /**
     * @param obj1 first object
     * x1 x-position of bitmap1 on screen.
     *  y1 y-position of bitmap1 on screen.
     * @param scaleW1 resize width bitmap11
     * @param scaleH1
     * @param obj2 Second object.
     * @param scaleW2
     * @param scaleH2
     *  x2 x-position of bitmap2 on screen.
     *  y2 y-position of bitmap2 on screen.
     *
     *           Overloaded method for isCollisionDetected, with resize
     */
    public boolean isCollisionDetected(AnimatedSpritesObject obj1, int scaleW1, int scaleH1,
                                       AnimatedSpritesObject obj2, int scaleW2, int scaleH2) {
        Bitmap bitmap11;
        int x1, y1;
        Bitmap bitmap22;
        int x2,y2;

        bitmap11 = obj1.getImage();
        x1 = obj1.getX();
        y1 = obj1.getY();
        bitmap22 = obj2.getImage();
        x2 = obj2.getX();
        y2 = obj2.getY();


        // copy bitmap
        Bitmap bitmap1 = bitmap11.copy(bitmap11.getConfig(), true);
        Bitmap bitmap2 = bitmap22.copy(bitmap22.getConfig(), true);

        // scale bitmap
        bitmap1 = getResizedBitmap(bitmap1 , scaleW1, scaleH1);
        bitmap2 = getResizedBitmap(bitmap2 , scaleW2, scaleH2);

        Rect bounds1 = new Rect(x1, y1, x1+bitmap1.getWidth(), y1+bitmap1.getHeight());
        Rect bounds2 = new Rect(x2, y2, x2+bitmap2.getWidth(), y2+bitmap2.getHeight());

        if (Rect.intersects(bounds1, bounds2)) {
            Rect collisionBounds = getCollisionBounds(bounds1, bounds2);
            for (int i = collisionBounds.left; i < collisionBounds.right; i++) {
                for (int j = collisionBounds.top; j < collisionBounds.bottom; j++) {
                    int bitmap1Pixel = bitmap1.getPixel(i-x1, j-y1);
                    int bitmap2Pixel = bitmap2.getPixel(i-x2 , j-y2);
                    if (isFilled(bitmap1Pixel) && isFilled(bitmap2Pixel)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isCollisionDetected(AnimatedSpritesObject obj1,
                                              Bubble bubble, Context context) {
        Bitmap bitmap1;
        int x1, y1;
        Bitmap bitmap2;
        int x2,y2;


        bitmap1 = obj1.getImage();
        x1 = obj1.getX();
        y1 = obj1.getY();

        bitmap2 = drawableToBitmap(bubble.drawable);
        x2 = bubble.getX();
        y2 = bubble.getY();


        Rect bounds1 = new Rect(x1, y1, x1+bitmap1.getWidth(), y1+bitmap1.getHeight());
        Rect bounds2 = new Rect(x2 - bubble.radius, y2 - bubble.radius, x2 + bubble.radius, y2 + bubble.radius);

        Log.e(TAG, "isCollisionDetected: " + BitmapFactory.decodeResource(context.getResources(), R.drawable.medium_bubble));

        if (Rect.intersects(bounds1, bounds2)) {
//            Log.e(TAG, "isCollisionDetected: checking filled pixels");
            Rect collisionBounds = getCollisionBounds(bounds1, bounds2);
            for (int i = collisionBounds.left; i < collisionBounds.right; i++) {
                for (int j = collisionBounds.top; j < collisionBounds.bottom; j++) {
                    int bitmap1Pixel = bitmap1.getPixel(i-x1, j-y1);
                    int bitmap2Pixel = bitmap2.getPixel(i-x2 - bubble.radius, j-y2 - bubble.radius);
                    Log.e(TAG, "isCollisionDetected: checking");
                    if (isFilled(bitmap1Pixel) && isFilled(bitmap2Pixel)) {
                        Log.e(TAG, "isCollisionDetected: colission happend");
                        return true;
                    }
                }
            }
        }
        Log.e(TAG, "isCollisionDetected: there was no collision");
        return false;
    }


    private static Rect getCollisionBounds(Rect rect1, Rect rect2) {
        int left = (int) Math.max(rect1.left, rect2.left);
        int top = (int) Math.max(rect1.top, rect2.top);
        int right = (int) Math.min(rect1.right, rect2.right);
        int bottom = (int) Math.min(rect1.bottom, rect2.bottom);
        return new Rect(left, top, right, bottom);
    }

    private static boolean isFilled(int pixel) {
        return pixel != Color.TRANSPARENT;
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

//        Canvas canvas = new Canvas(bitmap);
//        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//        drawable.draw(canvas);
        return bitmap;
    }

/**********************************************************************************************
 */

}

