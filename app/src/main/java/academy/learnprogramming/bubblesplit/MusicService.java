package academy.learnprogramming.bubblesplit;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * The service that played background music in the background.
 */
public class MusicService extends Service {

    public SharedPreferences savedSettings = MainActivity.activity.getSharedPreferences("SavedSettings", Context.MODE_PRIVATE);


    public static MediaPlayer backgroundMusic;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * this function is called when calling the startService function.
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int lvl = intent.getIntExtra("currLvl", 1);

        if(lvl >= 0 && lvl <= 5) backgroundMusic = MediaPlayer.create(this, R.raw.background_music1);
        else if (lvl >=6 && lvl <= 10) backgroundMusic = MediaPlayer.create(this, R.raw.background_music2);
        else if (lvl >= 11  && lvl <= 15) backgroundMusic = MediaPlayer.create(this, R.raw.background_music3);

        backgroundMusic.start();
        backgroundMusic.setLooping(true);
        final float volume = (float) (1 - (Math.log(Constants.MAX_VOLUME - savedSettings.getInt("MusicVolume", 80)) / Math.log(Constants.MAX_VOLUME)));
        backgroundMusic.setVolume(volume, volume);
        return START_STICKY;
    }

    /**
     * this function is called when calling the stopService function.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        backgroundMusic.stop();
        backgroundMusic.release();
    }

}
