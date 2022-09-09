package academy.learnprogramming.bubblesplit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BatteryBroadcastReceiver extends BroadcastReceiver {
    /**
     * check for low battery when received, and if it is low a toast is shown.
     * @param context the context of the class
     * @param intent the intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BATTERY_LOW.equals(intent.getAction()) ) {
            Toast.makeText(context, "Low Battery!", Toast.LENGTH_LONG).show();
        }
    }

}
