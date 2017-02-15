package com.jonasjuffinger.timelapse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sony.scalar.hardware.avio.DisplayManager;
import com.github.ma1co.pmcademo.app.BaseActivity;

public class SettingsActivity extends BaseActivity
{
    private SettingsActivity that = this;

    private int interval; // in seconds
    private AdvancedSeekBar sbInterval;
    private TextView tvIntervalValue, tvIntervalUnit;

    private int shots; // in seconds
    private AdvancedSeekBar sbShots;
    private TextView tvShotsValue;

    private TextView tvDurationValue, tvDurationUnit;
    private TextView tvVideoTimeValue, tvVideoTimeUnit;

    private int fps;
    private Spinner spnFps;

    private Button bnStart, bnClose, bnDisplayOutput;
    private ToggleButton tbBatterySaving;

    private DisplayManager avioDisplayManager;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler))
            Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler());

        Logger.info("Hello World");

        avioDisplayManager = new DisplayManager();

        Logger.info(avioDisplayManager.getActiveDevice());

        interval = 1;
        shots = 1;
        fps = 24;

        tvIntervalValue = (TextView) findViewById(R.id.tvIntervalValue);
        tvIntervalUnit = (TextView) findViewById(R.id.tvIntervalUnit);

        sbInterval = (AdvancedSeekBar) findViewById(R.id.sbInterval);
        sbInterval.setMax(46);
        sbInterval.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int intervalTextValue = 0;
                String intervalUnit = "";

                i++;

                if(i <= 20) {
                    interval = i;
                    intervalTextValue = interval;
                    intervalUnit = "s";
                }
                else if(i <= 28) {
                    interval = (i-20) * 5 + 20;
                    intervalTextValue = interval;
                    intervalUnit = "s";
                }
                else if(i <= 37) {
                    interval = (i-27) * 60;
                    intervalTextValue = (i-27);
                    intervalUnit = "min";
                }
                else if(i <= 47) {
                    interval = ((i-37) * 5 + 10) * 60;
                    intervalTextValue = (i-37) * 5 + 10;
                    intervalUnit = "min";
                }

                tvIntervalValue.setText(""+intervalTextValue);
                tvIntervalUnit.setText(intervalUnit);
                updateTimes();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        tvShotsValue = (TextView) findViewById(R.id.tvShotsValue);

        sbShots = (AdvancedSeekBar) findViewById(R.id.sbShots);
        sbShots.setMax(130);
        sbShots.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String shotsText;

                i++;

                if(i <= 20)
                    shots = i;

                else if(i <= 36)
                    shots = (i-20) * 5 + 20;

                else if(i <= 80)
                    shots = (i-36) * 20 + 120;

                else if(i <= 100)
                    shots = (i-80) * 50 + 1000;

                else if(i <= 130)
                    shots = (i-100) * 100 + 2000;

                shotsText = Integer.toString(shots);

                if(i == 301) {
                    shots = Integer.MAX_VALUE;
                    shotsText = "inf";
                }

                tvShotsValue.setText(shotsText);
                updateTimes();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        tvDurationValue = (TextView) findViewById(R.id.tvDurationValue);
        tvDurationUnit = (TextView) findViewById(R.id.tvDurationUnit);
        tvVideoTimeValue = (TextView) findViewById(R.id.tvVideoTimeValue);
        tvVideoTimeUnit = (TextView) findViewById(R.id.tvVideoTimeUnit);

        spnFps = (Spinner) findViewById(R.id.spnFps);
        spnFps.setSelection(0);
        spnFps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String sfps = getResources().getStringArray(R.array.fps)[i];
                fps = Integer.parseInt(sfps);
                updateTimes();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        bnStart = (Button) findViewById(R.id.bnStart);
        bnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(that, ShootActivity.class);
                intent.putExtra(ShootActivity.EXTRA_INTERVAL, interval);
                intent.putExtra(ShootActivity.EXTRA_SHOTCOUNT, shots);
                startActivity(intent);
            }
        });

        bnClose = (Button) findViewById(R.id.bnClose);
        bnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bnDisplayOutput = (Button) findViewById(R.id.bnDisplayOutput);
        bnDisplayOutput.setOnClickListener(displayOutputOnClickListener);

        tbBatterySaving = (ToggleButton) findViewById(R.id.tbBatterySaving);
        tbBatterySaving.setOnCheckedChangeListener(batterySavingOnClickListener);
    }

    View.OnClickListener displayOutputOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String output = "";

            if(avioDisplayManager.getActiveDevice().equals(DisplayManager.DEVICE_ID_PANEL))
                output = DisplayManager.DEVICE_ID_FINDER;
            else if(avioDisplayManager.getActiveDevice().equals(DisplayManager.DEVICE_ID_NONE))
                output = DisplayManager.DEVICE_ID_PANEL;

            try {
                Logger.info("change output to " + output);
                avioDisplayManager.switchDisplayOutputTo(output);
            } catch (Exception e) {
                Logger.error("avioDisplayManager.switchDisplayOutputTo(currentOutput);");
                Logger.error(e.getMessage());
                avioDisplayManager.switchDisplayOutputTo(DisplayManager.DEVICE_ID_PANEL);
            }
        }
    };

    CompoundButton.OnCheckedChangeListener batterySavingOnClickListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(b) {
                avioDisplayManager.setSavingBatteryMode(DisplayManager.SAVING_BATTERY_ON);
            }
            else {
                avioDisplayManager.setSavingBatteryMode(DisplayManager.SAVING_BATTERY_OFF);
            }
        }
    };

    void updateTimes() {
        int duration = interval * shots;
        int videoTime = shots / fps;

        if(duration < 60) {
            tvDurationValue.setText("" + duration);
            tvDurationUnit.setText("s");
        }
        else {
            tvDurationValue.setText("" + (duration / 60));
            tvDurationUnit.setText("min");
        }

        if(videoTime < 120) {
            tvVideoTimeValue.setText("" + videoTime);
            tvVideoTimeUnit.setText("s");
        }
        else {
            tvVideoTimeValue.setText("" + (videoTime / 60));
            tvVideoTimeUnit.setText("min");
        }
    }

    protected boolean onLowerDialChanged(int value) {
        sbInterval.dialChanged(value);
        sbShots.dialChanged(value);
        return true;
    }


    @Override
    protected boolean onMenuKeyUp()
    {
        onBackPressed();
        return true;
    }
}
