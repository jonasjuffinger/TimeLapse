package com.jonasjuffinger.timelapse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by jonas on 2/18/17.
 */

class Settings {
    private static final String EXTRA_INTERVAL = "com.jonasjuffinger.timelapse.INTERVAL";
    private static final String EXTRA_SHOTCOUNT = "com.jonasjuffinger.timelapse.SHOTCOUNT";
    private static final String EXTRA_DISPLAYOFF = "com.jonasjuffinger.timelapse.DISPLAYOFF";
    private static final String EXTRA_SILENTSHUTTER = "com.jonasjuffinger.timelapse.SILENTSHUTTER";

    int interval, rawInterval;
    int shotCount, rawShotCount;
    boolean displayOff;
    boolean silentShutter;
    int fps;    // index

    Settings() {
        interval = 1;
        rawInterval = 1;
        shotCount = 1;
        rawShotCount = 1;
        displayOff = false;
        silentShutter = true;
        fps = 0;
    }

    public Settings(int interval, int shotCount, boolean displayOff, boolean silentShutter) {
        this.interval = interval;
        this.shotCount = shotCount;
        this.displayOff = displayOff;
        this.silentShutter = silentShutter;
    }

    void putInIntent(Intent intent) {
        intent.putExtra(EXTRA_INTERVAL, interval);
        intent.putExtra(EXTRA_SHOTCOUNT, shotCount);
        intent.putExtra(EXTRA_DISPLAYOFF, displayOff);
        intent.putExtra(EXTRA_SILENTSHUTTER, silentShutter);
    }

    static Settings getFromIntent(Intent intent) {
        return new Settings(
                intent.getIntExtra(EXTRA_INTERVAL, 1),
                intent.getIntExtra(EXTRA_SHOTCOUNT, 1),
                intent.getBooleanExtra(EXTRA_DISPLAYOFF, false),
                intent.getBooleanExtra(EXTRA_SILENTSHUTTER, false)
        );
    }

    void save(Context context)
    {
        SharedPreferences sharedPref = getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("interval", rawInterval);
        editor.putInt("shotCount", rawShotCount);
        editor.putBoolean("silentShutter", silentShutter);
        editor.putInt("fps", fps);
        editor.apply();
    }

    void load(Context context)
    {
        SharedPreferences sharedPref = getDefaultSharedPreferences(context);
        rawInterval = sharedPref.getInt("interval", rawInterval);
        rawShotCount = sharedPref.getInt("shotCount", rawShotCount);
        silentShutter = sharedPref.getBoolean("silentShutter", silentShutter);
        fps = sharedPref.getInt("fps", fps);
    }
}
