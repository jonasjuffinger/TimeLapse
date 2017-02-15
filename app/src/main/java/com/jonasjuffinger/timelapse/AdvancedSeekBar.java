package com.jonasjuffinger.timelapse;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;

/**
 * Created by jonas on 2/12/17.
 */

public class AdvancedSeekBar extends SeekBar {
    public AdvancedSeekBar(Context context) {
        super(context);
    }

    public AdvancedSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdvancedSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void dialChanged(int value) {
        if(!isFocused())
            return;

        setProgress(getProgress() + value);
    }
}
