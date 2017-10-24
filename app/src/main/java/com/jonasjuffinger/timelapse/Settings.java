package com.jonasjuffinger.timelapse;

import android.content.Intent;

/**
 * Created by jonas on 2/18/17.
 */

class Settings {
    private static final String EXTRA_INTERVAL = "com.jonasjuffinger.timelapse.INTERVAL";
    private static final String EXTRA_SHOTCOUNT = "com.jonasjuffinger.timelapse.SHOTCOUNT";
    private static final String EXTRA_DISPLAYOFF = "com.jonasjuffinger.timelapse.DISPLAYOFF";
    private static final String EXTRA_SILENTSHUTTER = "com.jonasjuffinger.timelapse.SILENTSHUTTER";

    int interval;
    int shotCount;
    boolean displayOff;
    boolean silentShutter;

    Settings() {
        interval = 1;
        shotCount = 1;
        displayOff = false;
        silentShutter = true;
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
}
