package academy.learnprogramming.bubblesplit;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * a class for objects which are from drawable type and they are a circle. it is helpful to check things with the functions of a circle, such as the exact center x and y, and the radius.
 */
public class Circle extends SingleDrawableObject {

    protected int radius;

    public Circle(Drawable image) {
        super(image);
        radius = width / 2;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        drawable.setBounds(screenRect());
        if(shown) drawable.draw(canvas);
    }

    /**
     * the rectangle that is around the circle.
     * @return
     */
    @Override
    public Rect screenRect() {
        return new Rect(
                x - radius, y - radius,
                x + radius, y + radius);
    }

    public int getRadius(){
        return radius;
    }

    public void setRadius(int radius){
        this.radius = radius;
    }

    /**
     * checks if a point is in this circle object.
     * @param x
     * @param y
     * @return
     */
    public boolean containsPoint(double x, double y){
        return GameLogic.distanceBetweenTwoPoints(x, y, getX(), getY()) < radius;
    }

    @Override
    public void setDrawable(Drawable drawable) {
        super.setDrawable(drawable);
        radius = width / 2;
    }
}
