package com.jonasjuffinger.timelapse;

import android.os.Handler;

import com.sony.scalar.hardware.avio.DisplayManager;

/**
 * Created by jonas on 2/18/17.
 */

public class Display {
    public static final int NO_AUTO_OFF = 0;

    private DisplayManager displayManager;
    private int autoOffDelay;

    Display(DisplayManager displayManager) {
        this.displayManager = displayManager;
        autoOffDelay = 0;
    }

    private Handler turnOffRunnableHandler = new Handler();
    private final Runnable turnOffRunnable = new Runnable() {
        @Override
        public void run() {
            off();
        }
    };

    public void turnAutoOff(int delay) {
        autoOffDelay = delay;

        if(delay == 0) {
            turnOffRunnableHandler.removeCallbacks(turnOffRunnable);
        }
        if(delay > 0) {
            turnOffRunnableHandler.postDelayed(turnOffRunnable, delay);
        }
    }

    public int getAutoOffDelay() {
        return autoOffDelay;
    }

    public void off() {
        try {
            Logger.info("turn display off");
            displayManager.switchDisplayOutputTo(DisplayManager.DEVICE_ID_NONE);
        } catch (Exception e) {
            Logger.error("avioDisplayManager.switchDisplayOutputTo(currentOutput);");
            Logger.error(e.getMessage());
            displayManager.switchDisplayOutputTo(DisplayManager.DEVICE_ID_PANEL);
        }
    }

    public void on() {
        Logger.info("turn display on");
        displayManager.switchDisplayOutputTo(DisplayManager.DEVICE_ID_PANEL);
    }

    public void on(boolean autoOff) {
        Logger.info("turn display on");
        displayManager.switchDisplayOutputTo(DisplayManager.DEVICE_ID_PANEL);

        if(autoOff && autoOffDelay > 0) {
            turnOffRunnableHandler.postDelayed(turnOffRunnable, autoOffDelay);
        }
    }
}
