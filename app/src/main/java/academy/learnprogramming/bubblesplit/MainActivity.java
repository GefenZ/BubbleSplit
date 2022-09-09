package academy.learnprogramming.bubblesplit;

/**
 * This is the Main Activity of our game
 */


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
//import androidx.core.view.WindowCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.core.view.WindowInsetsControllerCompat;

public class MainActivity extends AppCompatActivity {

    BatteryBroadcastReceiver batteryBroadcastReceiver = new BatteryBroadcastReceiver();
    PowerConnectionReceiver powerConnectionReceiver = new PowerConnectionReceiver();


    public static Activity activity = null;

    private static int WIDTH;
    private static int HEIGHT;

    public static Handler handler = new Handler();
    public static Runnable runnable;

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // set screen landscape view
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);


        //set to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // get device screen size for game panel use
        Display display = getWindowManager().getDefaultDisplay();
        // display size in pixels
        Point size = new Point();
        display.getRealSize(size);
        WIDTH = size.x;
        HEIGHT = size.y;

        hideSystemUI();

        //in this lesson  we will set the content view and we will create a new class
        setContentView(new GamePanel(this, WIDTH, HEIGHT));
    }

    /**
     * this methods used for full screen enable and disable
     */
    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        ((View) decorView).setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
//        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
//        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
//        controller.hide(WindowInsetsCompat.Type.statusBars() | WindowInsetsCompat.Type.navigationBars());
//        controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
//        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
//        controller.show(WindowInsetsCompat.Type.statusBars() | WindowInsetsCompat.Type.navigationBars());

    }

    /**
     * register the BatteryBroadcastReceiver when start
     */
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter batteryIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_LOW);
        IntentFilter powerIntentfilter = new IntentFilter();
        powerIntentfilter.addAction(Intent.ACTION_POWER_CONNECTED);
        powerIntentfilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(powerConnectionReceiver, powerIntentfilter);
        registerReceiver(batteryBroadcastReceiver, batteryIntentFilter);
    }

    /**
     * unregister the BatteryBroadcastReceiver when stop
     */
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(powerConnectionReceiver);
        unregisterReceiver(batteryBroadcastReceiver);
    }

}
