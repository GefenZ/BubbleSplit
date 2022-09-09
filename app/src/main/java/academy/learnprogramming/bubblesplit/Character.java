package academy.learnprogramming.bubblesplit;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * the main character of the game.
 */
public class Character extends AnimatedSpritesObject {

    public static String TAG = "Character";

    Context context;

    private long startTime;
    private long buttonTimer = 0;

    private WalkingDirection walkingDirection = WalkingDirection.STANDING;

    private CircleButton leftCircleButton;
    private CircleButton rightCircleButton;
    private CircleButton shootCircleButton;

    private BitmapDrawable bulletDrawable;

    private Runnable runnable1;

    private Set<Bullet> bullets;

    private  Set<Bubble> bubbles;

    private Explosion explosion;
    private boolean isExploding;

    public boolean gameResult;

    /**
     * the constructor of character. the character controls a lot of things in the game.
     * @param imageThatHasSprites
     * @param numberOfSprites
     * @param rowLength
     * @param leftCircleButton - a button for the character to move left
     * @param rightCircleButton - a button for the character to move right
     * @param shootCircleButton - a button for the character to shoot bullets
     * @param bulletDrawable - the bullet drawable, so that the character can create the bullets he shoots
     * @param bubbles - the set of bubbles, because it is needed to check if the bubbles got hit or not because the character is the one shooting bullets
     * @param context
     *
     * the functions which control the movement of the character and the shooting of bullets are implemented within this constructor, because it is easier and makes sense this way.
     */
    public Character(Bitmap imageThatHasSprites, int numberOfSprites, int rowLength, CircleButton leftCircleButton, CircleButton rightCircleButton, CircleButton shootCircleButton, BitmapDrawable bulletDrawable, Set<Bubble> bubbles, Context context){
        super(imageThatHasSprites, numberOfSprites, rowLength);
        this.context = context;
        bullets = new HashSet<>();

        this.bulletDrawable = bulletDrawable;

        this.bubbles = bubbles;

        this.leftCircleButton = leftCircleButton;
        this.rightCircleButton = rightCircleButton;
        this.shootCircleButton = shootCircleButton;
        leftCircleButton.setOnButtonTouchListener(new CircleButton.OnButtonTouchListener() {
            @Override
            public void onTouchDown() {
                animation.setSpriteRow(1);
                walkingDirection = WalkingDirection.LEFT;
            }

            @Override
            public void onTouchUp() {
                animation.setSpriteRow(0);
                walkingDirection = WalkingDirection.STANDING;
            }
        });
        rightCircleButton.setOnButtonTouchListener(new CircleButton.OnButtonTouchListener() {
            @Override
            public void onTouchDown() {
                animation.setSpriteRow(3);
                walkingDirection = WalkingDirection.RIGHT;
            }

            @Override
            public void onTouchUp() {
                animation.setSpriteRow(0);
                walkingDirection = WalkingDirection.STANDING;
            }
        });
        shootCircleButton.setOnButtonTouchListener(new CircleButton.OnButtonTouchListener() {
            @Override
            public void onTouchDown() {
                MainActivity.handler.postDelayed(runnable1 = new Runnable() {
                    @Override
                    public void run() {
                        buttonTimer = System.nanoTime();
                        shoot();

                        MainActivity.handler.postDelayed(MainActivity.runnable = new Runnable() {
                            @Override
                            public void run() {
                                buttonTimer = System.nanoTime();
                                shoot();

                                MainActivity.handler.postDelayed(MainActivity.runnable, Constants.SHOOTING_SPEED_DELAY);
                            }
                        }, Constants.SHOOTING_SPEED_DELAY);
                    }
                }, Math.max(0, Constants.SHOOTING_SPEED_DELAY - ((System.nanoTime() - buttonTimer) / 1000000)));
            }

            @Override
            public void onTouchUp() {
                MainActivity.handler.removeCallbacks(runnable1);
                MainActivity.handler.removeCallbacks(MainActivity.runnable);
            }
        });

        startTime = System.nanoTime();
    }

    /**
     * when the game is stopped and than resumed, we want to make sure we reset the animation, because if we don't the character takes a big leap forward in time because the time keeps on ticking while
     * the game is stopped.
     */
    public void resetAnimation(){
        startTime= System.nanoTime();
        for(Bullet bullet:bullets){
            bullet.resetAnimation();
        }
    }

    /**
     * in the update function a lot of cases of the games are taken care of.
     * first of all, the character is being moved if one of the move buttons are pressed.
     * it is checked if bullets go out of bounds of the game map, and if they are we remove them so it because we don't need garbage.
     * it is checked if a bubble got hit, and if it did it activates an explosion.
     * it is checked if a bubble hit the character, and if it did it applies the needed changes.
     * it is checked if the character won the game, and if he did it progresses.
     */
    @Override
    public void update() {
        super.update();

        long elapsed = (System.nanoTime() - startTime) / 1000000;

        // taking care of the movement of the character
        if(walkingDirection != WalkingDirection.STANDING) {
            animation.update();
        }
        if(leftCircleButton.isPressed() && x> -50  ) {
            x-= (300 *elapsed) / 1000;
            if(x<= -50) x= -49;
        }
        if(rightCircleButton.isPressed() && x < Constants.ORIGINAL_SCREEN_WIDTH - this.width + 50) {
            x += (300 * elapsed) / 1000;
            if(x >= Constants.ORIGINAL_SCREEN_WIDTH - this.width + 50) x = Constants.ORIGINAL_SCREEN_WIDTH - this.width +49;
        }

        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
        ArrayList<Bubble> bubblesToRemove = new ArrayList<>();

        // out of bounds
        for(Bullet bullet:bullets){
            bullet.update();
            if(bullet.y < -bullet.height){
                bulletsToRemove.add(bullet);
            }
        }

        // hit a target + update bubbles

        for(Bullet bullet:bullets){
            for(Bubble bubble:bubbles){
                if(GameLogic.distanceBetweenTwoPoints(bullet.getX(), bullet.getY(), bubble.getX(), bubble.getY()) <= bullet.getRadius() + bubble.getRadius()){
                    this.explosion = explode(bubble, context);
                    isExploding = true;
                    bulletsToRemove.add(bullet);
                    bubblesToRemove.add(bubble);
                    break;
                }
            }
        }

        // remove all useless objects
        for(Bullet bullet:bulletsToRemove){
            bullets.remove(bullet);
        }
        for(Bubble bubble:bubblesToRemove){
            bubbleHit(bubble);
            bubbles.remove(bubble);
        }

        if(isExploding){
            explosion.update();
            if(explosion.playedOnce || bubbles.isEmpty()) {
                isExploding = false;
                explosion = null;
            }
        }

        //check if bubble hit character
        int left = this.x + (this.width-Constants.CHARACTER_HITBOX_WIDTH)/2;
        int right = this.x + (this.width-Constants.CHARACTER_HITBOX_WIDTH)/2 + Constants.CHARACTER_HITBOX_WIDTH ;
        int top = this.y  + (this.height-Constants.CHARACTER_HITBOX_HEIGHT)/2;
        int bottom = this.y  + (this.height-Constants.CHARACTER_HITBOX_HEIGHT)/2 + Constants.CHARACTER_HITBOX_HEIGHT;
        Rect rect = new Rect(left, top, right, bottom);


        for(Bubble bubble:bubbles){
            if(intersects(bubble, rect)){
                Log.e(TAG, "update: game ended");
                GamePlayScene.gameState = GameState.GAME_ENDED;
                gameResult = false;
                SharedPreferences savedSettings = context.getApplicationContext().getSharedPreferences("SavedSettings", Context.MODE_PRIVATE);
                if (savedSettings.getBoolean("Sound", true)) {
                    try {
                        if (GamePanel.gameLostSound.isPlaying()) {
                            GamePanel.gameLostSound.stop();
                            GamePanel.gameLostSound.release();
                            GamePanel.gameLostSound = MediaPlayer.create(context.getApplicationContext(), R.raw.game_lost_sound);
                        }
                        GamePanel.gameLostSound.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                restart();
            }
        }

        // character wins the level
        if(bubbles.isEmpty()){
            GamePlayScene.gameState = GameState.GAME_ENDED;
            GamePlayScene.currentLevel++;
            GamePlayScene.levelProgressed = true;
            gameResult = true;
            restart();
        }

        startTime = System.nanoTime();
    }

    /**
     * a function to shoot a bullet - creates a bullet, determines it's starting x and y, and adding it to that array so it can exist.
     */
    public void shoot(){
        Bullet bullet = new Bullet(bulletDrawable);
        bullet.setX(x + width/2);
        bullet.setY(y);
        bullets.add(bullet);
    }

    /**
     * a function to explode a bubble.
     * the size of the bubble is checked here, and the size of the explosion is determined by the size of the bubble.
     * than, an explosion sound is also played.
     * @param bubble
     * @param context
     * @return
     */
    public Explosion explode(Bubble bubble, Context context){
        int scaledWidth = 0, scaledHeight = 0;
        switch (bubble.bubbleSize){
            case BIG:
                scaledHeight = 200;
                scaledWidth = 800;
                break;
            case MEDIUM:
                scaledHeight = 150;
                scaledWidth = 600;
                break;
            case SMALL:
                scaledHeight = 100;
                scaledWidth = 400;
                break;
            case VERYSMALL:
                scaledHeight = 50;
                scaledWidth = 200;
                break;
        }
        Explosion explosion = new Explosion(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_sprite),scaledWidth, scaledHeight, false), 4, 4);
        explosion.setX(bubble.x - (scaledWidth/4 -scaledWidth/8));
        explosion.setY(bubble.y - scaledHeight/4);

        SharedPreferences savedSettings = context.getApplicationContext().getSharedPreferences("SavedSettings", Context.MODE_PRIVATE);
        if(savedSettings.getBoolean("Sound",true)) {
            try {
                if (GamePanel.explodeSound.isPlaying()) {
                    GamePanel.explodeSound.stop();
                    GamePanel.explodeSound.release();
                    GamePanel.explodeSound = MediaPlayer.create(context.getApplicationContext(), R.raw.pop_sound);
                } GamePanel.explodeSound.start();
            } catch(Exception e) { e.printStackTrace(); }
        }
        return explosion;
    }

    /**
     * if the bubble got hit. this function creates two bubbles in a smaller size
     * @param bubble
     */
    public void bubbleHit(Bubble bubble){
        Bubble a = null, b= null;
        if(bubble.bubbleSize == BubbleSize.BIG){
            a = new Bubble(ResourcesCompat.getDrawable(context.getResources(), R.drawable.medium_bubble, null), 900, -130, 80, BubbleSize.MEDIUM, bubble.x ,bubble.y);
            b = new Bubble(ResourcesCompat.getDrawable(context.getResources(), R.drawable.medium_bubble, null), 900, 130, 80, BubbleSize.MEDIUM,bubble.x , bubble.y);
        }
        if(bubble.bubbleSize == BubbleSize.MEDIUM){
            a = new Bubble(ResourcesCompat.getDrawable(context.getResources(), R.drawable.small_bubble, null), 1000, -130, 120, BubbleSize.SMALL,bubble.x,bubble.y);
            b = new Bubble(ResourcesCompat.getDrawable(context.getResources(), R.drawable.small_bubble, null), 1000, 130, 120, BubbleSize.SMALL,bubble.x,bubble.y);
        }
        if(bubble.bubbleSize == BubbleSize.SMALL){
            a = new Bubble(ResourcesCompat.getDrawable(context.getResources(), R.drawable.very_small_bubble, null), 900, -130, 140, BubbleSize.VERYSMALL,bubble.x,bubble.y);
            b = new Bubble(ResourcesCompat.getDrawable(context.getResources(), R.drawable.very_small_bubble, null), 900, 130, 140, BubbleSize.VERYSMALL,bubble.x,bubble.y);
        }
        if(bubble.bubbleSize!=BubbleSize.VERYSMALL) {
            bubbles.add(a);
            a.isOnSplit = true;
            bubbles.add(b);
            b.isOnSplit = true;
        }
    }

    /**
     * if the bubble and the character collided.
     * @param bubble
     * @param rect
     * @return
     */
    public boolean intersects(Bubble bubble, Rect rect)
    {
        int circleDistanceX = Math.abs(bubble.getX() - (int)rect.exactCenterX());
        int circleDistanceY = Math.abs(bubble.getY() - (int)rect.exactCenterY());

        if (circleDistanceX > (rect.width()/2 + bubble.radius)) { return false; }
        if (circleDistanceY > (rect.height()/2 + bubble.radius)) { return false; }

        if (circleDistanceX <= (rect.width()/2)) { return true; }
        if (circleDistanceY <= (rect.height()/2)) { return true; }

        int cornerDistance_sq = (circleDistanceX - rect.width()/2)*(circleDistanceX - rect.width()/2) + (circleDistanceY - rect.height()/2)*(circleDistanceY - rect.height()/2);

        return (cornerDistance_sq <= (bubble.radius*bubble.radius));
    }

//    public boolean ellipseCircleIntersects(double ax, double ay, double bx, double by, double cx, double cy, double r, double twoA){
//        return  ellipseCircleIntersectsHelper(ax, ay, bx, by, cx, cy, r, twoA) ||
//                ellipseCircleIntersectsHelper(bx, by, ax, ay, cx, cy, r, twoA);
//    }
//
//    public boolean ellipseCircleIntersectsHelper(double ax, double ay, double bx, double by, double cx, double cy, double r, double twoA){
//        double angleCBY = Math.atan(Math.abs(ay - cy) / Math.abs(cx - ax));
//        double bx2 = ax - cx, by2 = ay - cy; // b by centered c
//        if(bx2 > 0 && by2 > 0) angleCBY = 360 - angleCBY;
//        if(bx2 <= 0){
//            if(by2 <= 0) angleCBY = 180 - angleCBY;
//            else angleCBY += 180;
//        }
//        double px = cx + r * Math.cos(angleCBY), py = cy + r * Math.sin(angleCBY);
//        return GameLogic.distanceBetweenTwoPoints(ax, ay, px, py) + GameLogic.distanceBetweenTwoPoints(bx, by, px, py) <= twoA;
//    }

    /**
     * when the current level is finished, for some reason. it cleans everything there is on the screen, bullets and bubbles - so some level could be loaded later on.
     */
    public void finish(){
        ArrayList<Bubble> bubblesToRemove = new ArrayList<>();
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
        for(Bubble bubble:bubbles){
            bubblesToRemove.add(bubble);
        }
        for(Bullet bullet:bullets){
            bulletsToRemove.add(bullet);
        }
        for(Bullet bullet:bulletsToRemove){
            bullets.remove(bullet);
        }
        for(Bubble bubble:bubblesToRemove){
            bubbles.remove(bubble);
        }
        isExploding = false;
        explosion = null;
        x = (Constants.ORIGINAL_SCREEN_WIDTH/2) - width;
        y = Constants.GAMEPLAY_GROUND_Y - height;

        GamePlayScene.gameState = GameState.GAME_NOT_STARTED;
        GamePlayScene.levelLoaded = false;
    }

    /**
     * to restart, the finish function is called, but we wait 2 seconds with it, so there can be time to display some massage to the user.
     */
    public void restart(){
        Log.e(TAG, "restart: "+ gameResult + "and: " + GamePlayScene.currentLevel);
        MainActivity.handler.removeCallbacks(MainActivity.runnable);
        if(gameResult && GamePlayScene.currentLevel > Constants.NO_OF_LEVELS) finish();
        else {
            MainActivity.handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 2000);
        }
    }


    @Override
    public void draw(Canvas canvas) {
        for(Bullet bullet:bullets){
            bullet.draw(canvas);
        }
        if(isExploding){
            explosion.draw(canvas);
        }
        super.draw(canvas);
    }

    /**
     * gettets and setters of walking direction.
     */
    public WalkingDirection getWalkingDirection() {
        return walkingDirection;
    }

    public void setWalkingDirection(WalkingDirection walkingDirection) {
        this.walkingDirection = walkingDirection;
    }
}
