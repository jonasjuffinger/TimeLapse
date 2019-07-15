package com.jonasjuffinger.timelapse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.concurrent.atomic.AtomicInteger;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by jonas on 2/18/17.
 */

class Settings {
    private static final String EXTRA_INTERVAL = "com.jonasjuffinger.timelapse.INTERVAL";
    private static final String EXTRA_SHOTCOUNT = "com.jonasjuffinger.timelapse.SHOTCOUNT";
    private static final String EXTRA_DISPLAYOFF = "com.jonasjuffinger.timelapse.DISPLAYOFF";
    private static final String EXTRA_SILENTSHUTTER = "com.jonasjuffinger.timelapse.SILENTSHUTTER";
    private static final String EXTRA_AEL = "com.jonasjuffinger.timelapse.AEL";
    private static final String EXTRA_BRS = "com.jonasjuffinger.timelapse.BRS";

    int interval, rawInterval;
    int shotCount, rawShotCount;
    boolean displayOff;
    boolean silentShutter;
    boolean ael;
    int fps;    // index
    boolean brs;

    Settings() {
        interval = 1;
        rawInterval = 1;
        shotCount = 1;
        rawShotCount = 1;
        displayOff = false;
        silentShutter = true;
        ael = true;
        fps = 0;
        brs = true;
    }

    public Settings(int interval, int shotCount, boolean displayOff, boolean silentShutter, boolean ael, boolean brs) {
        this.interval = interval;
        this.shotCount = shotCount;
        this.displayOff = displayOff;
        this.silentShutter = silentShutter;
        this.ael = ael;
        this.brs = brs;
    }

    void putInIntent(Intent intent) {
        intent.putExtra(EXTRA_INTERVAL, interval);
        intent.putExtra(EXTRA_SHOTCOUNT, shotCount);
        intent.putExtra(EXTRA_DISPLAYOFF, displayOff);
        intent.putExtra(EXTRA_SILENTSHUTTER, silentShutter);
        intent.putExtra(EXTRA_AEL, ael);
        intent.putExtra(EXTRA_BRS, brs);
    }

    static Settings getFromIntent(Intent intent) {
        return new Settings(
                intent.getIntExtra(EXTRA_INTERVAL, 1),
                intent.getIntExtra(EXTRA_SHOTCOUNT, 1),
                intent.getBooleanExtra(EXTRA_DISPLAYOFF, false),
                intent.getBooleanExtra(EXTRA_SILENTSHUTTER, false),
                intent.getBooleanExtra(EXTRA_AEL, false),
                intent.getBooleanExtra(EXTRA_BRS, false)
        );
    }

    void save(Context context)
    {
        SharedPreferences sharedPref = getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("interval", rawInterval);
        editor.putInt("shotCount", rawShotCount);
        editor.putBoolean("silentShutter", silentShutter);
        editor.putBoolean("ael", ael);
        editor.putInt("fps", fps);
        editor.putBoolean("brs", brs);
        editor.apply();
    }

    void load(Context context)
    {
        SharedPreferences sharedPref = getDefaultSharedPreferences(context);
        rawInterval = sharedPref.getInt("interval", rawInterval);
        rawShotCount = sharedPref.getInt("shotCount", rawShotCount);
        silentShutter = sharedPref.getBoolean("silentShutter", silentShutter);
        ael = sharedPref.getBoolean("ael", ael);
        fps = sharedPref.getInt("fps", fps);
        brs = sharedPref.getBoolean("brs", brs);
    }
}
