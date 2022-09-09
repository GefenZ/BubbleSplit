package academy.learnprogramming.bubblesplit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class PowerConnectionReceiver extends BroadcastReceiver {

    /**
     * check if the device is getting connected or disconnect to a charger, and if one of t he cases is correct a toast that specifies it is shown.
     * @param context the context of the class
     * @param intent the intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
            Toast.makeText(context, "The device is charging", Toast.LENGTH_SHORT).show();
        } else {
            intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED);
            Toast.makeText(context, "The device is not charging", Toast.LENGTH_SHORT).show();
        }
    }


}
