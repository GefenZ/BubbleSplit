package academy.learnprogramming.bubblesplit;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Stack;

/**
 * The scene manger is responsible to control all the scenes and pop ups. The scene manager has a stack of pop ups
 * and a stacl of scenes, and it always updates and draws the scene that is at the top of the stack, or if there is a
 * pop up than the pop up that is in the top of the stuck.
 */

public class SceneManager {
    private Stack<Scene> scenes;
    private Stack<PopUp> popUps;

    public SceneManager(){
        scenes = new Stack<>();
        popUps = new Stack<>();
    }

    /**
     * receive touches from the top scene from the stack, or the top pop up.
     * @param event
     */
    public void receiveTouch(MotionEvent event) {
        if(!popUps.isEmpty()) popUps.peek().receiveTouch(event);
        else scenes.peek().receiveTouch(event);
    }
    /**
     * update the top scene from the stack, or the top pop up.
     */
    public void update() {
        if(!popUps.isEmpty()) popUps.peek().update();
        else scenes.peek().update();
    }

    /**
     * draw the top scene from the stack, or the top pop up.
     * @param canvas
     */
    public void draw(Canvas canvas) {
        scenes.peek().draw(canvas);
        if(!popUps.isEmpty()) popUps.peek().draw(canvas);
    }

    /**
     * enter a scene
     * @param scene
     */
    public void enterScene(Scene scene){
        scenes.push(scene);
    }

    /**
     * exit the current scene
     */
    public void onBackPressed(){
        scenes.pop();
    }

    /**
     * open a pop up
     * @param popUp
     */
    public void openPopUp(PopUp popUp){
        popUps.push(popUp);
    }

    /**
     * close the current pop up
     */
    public void closePopUp(){
        popUps.pop();
    }

    /**
     * terminate the current scene
     */
    public void terminate(){
        scenes.peek().terminate();
    }


}
