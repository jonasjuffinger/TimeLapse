package com.jonasjuffinger.timelapse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import com.github.ma1co.pmcademo.app.BaseActivity;
import com.sony.scalar.hardware.CameraEx;

import org.w3c.dom.Text;

public class SettingsActivity extends BaseActivity
{
    private SettingsActivity that = this;

    private Settings settings;

    private TabHost tabHost;

    private Button bnStart, bnClose;

    private AdvancedSeekBar sbDelay;
    private TextView tvDelayValue, tvDelayUnit;

    private AdvancedSeekBar sbInterval;
    private TextView tvIntervalValue, tvIntervalUnit;

    private AdvancedSeekBar sbShots;
    private TextView tvShotsValue;
    private TextView lblShots;

    private TextView tvDurationValue, tvDurationUnit;
    private TextView tvVideoTimeValue, tvVideoTimeUnit;

    private int fps;
    private Spinner spnFps;

    private CheckBox cbSilentShutter;
    private CheckBox cbAEL;
    private CheckBox cbBRS;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler))
            Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler());

        Logger.info("Hello World");

        settings = new Settings();
        settings.load(this);
        fps = 24;

        bnStart = (Button) findViewById(R.id.bnStart);
        bnStart.setOnClickListener(bnStartOnClickListener);

        bnClose = (Button) findViewById(R.id.bnClose);
        bnClose.setOnClickListener(bnCloseOnClickListener);

        tvIntervalValue = (TextView) findViewById(R.id.tvIntervalValue);
        tvIntervalUnit = (TextView) findViewById(R.id.tvIntervalUnit);

        tvDelayValue = (TextView) findViewById(R.id.tvDelayValue);
        tvDelayUnit = (TextView) findViewById(R.id.tvDelayUnit);

        tvDurationValue = (TextView) findViewById(R.id.tvDurationValue);
        tvDurationUnit = (TextView) findViewById(R.id.tvDurationUnit);
        tvVideoTimeValue = (TextView) findViewById(R.id.tvVideoTimeValue);
        tvVideoTimeUnit = (TextView) findViewById(R.id.tvVideoTimeUnit);
        sbDelay = (AdvancedSeekBar) findViewById((R.id.sbDelay));
        sbInterval = (AdvancedSeekBar) findViewById(R.id.sbInterval);
        tvShotsValue = (TextView) findViewById(R.id.tvShotsValue);
        sbShots = (AdvancedSeekBar) findViewById(R.id.sbShots);
        lblShots = (TextView) findViewById(R.id.lblShots);
        spnFps = (Spinner) findViewById(R.id.spnFps);

        cbSilentShutter = (CheckBox) findViewById(R.id.cbSilentShutter);
        cbAEL = (CheckBox) findViewById(R.id.cbAEL);
        cbBRS = (CheckBox) findViewById(R.id.cbBRC);

        sbDelay.setMax(59*5 + 2);
        sbDelay.setOnSeekBarChangeListener(sbDelayOnSeekBarChangeListener);
        sbDelay.setProgress(settings.rawDelay);
        sbDelayOnSeekBarChangeListener.onProgressChanged(sbDelay, settings.rawDelay, false);

        sbInterval.setMax(119);
        sbInterval.setOnSeekBarChangeListener(sbIntervalOnSeekBarChangeListener);
        sbInterval.setProgress(settings.rawInterval);
        sbIntervalOnSeekBarChangeListener.onProgressChanged(sbInterval, settings.rawInterval, false);

        sbShots.setMax(130);
        sbShots.setOnSeekBarChangeListener(sbShotsOnSeekBarChangeListener);
        sbShots.setProgress(settings.rawShotCount);
        sbShotsOnSeekBarChangeListener.onProgressChanged(sbShots, settings.rawShotCount, false);

        spnFps.setSelection(settings.fps);
        spnFps.setOnItemSelectedListener(spnFpsOnItemSelectedListener);

        cbSilentShutter.setChecked(settings.silentShutter);
        cbSilentShutter.setOnCheckedChangeListener(cbSilentShutterOnCheckListener);
        //cbSilentShutter.setVisibility(View.INVISIBLE);

        cbAEL.setChecked(settings.ael);
        cbAEL.setOnCheckedChangeListener(cbAELOnCheckListener);

        cbBRS.setChecked(settings.brs);
        cbBRS.setOnCheckedChangeListener(cbBRSOnCheckListener);

        //try {
            //CameraEx cameraEx = CameraEx.open(0, null);
            //final CameraEx.ParametersModifier modifier = cameraEx.createParametersModifier(cameraEx.getNormalCamera().getParameters());
            //if(modifier.isSupportedSilentShutterMode())
            //    cbSilentShutter.setVisibility(View.VISIBLE);
        /*}
        catch(Exception ignored)
        {}*/
    }

    View.OnClickListener bnStartOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            settings.save(that);
            Intent intent = new Intent(that, ShootActivity.class);
            settings.putInIntent(intent);
            startActivity(intent);
        }
    },
    bnCloseOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            settings.save(that);
            finish();
        }
    };

    SeekBar.OnSeekBarChangeListener sbIntervalOnSeekBarChangeListener
            = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
            String intervalTextValue = "";
            String intervalUnit = "";

            settings.rawInterval = i;

            i++;

            if (i == 1) {
                settings.interval = 0;
                intervalTextValue = "burst";
                intervalUnit = "";
            }
            else if(i < 41) {
                settings.interval = i * 0.5;
                intervalTextValue = Double.toString(settings.interval);
                intervalUnit = "s";

            }
            else if(i < 60) {
                settings.interval = (i-40) + 20;
                intervalTextValue = Double.toString(settings.interval);
                intervalUnit = "s";
            }
            else if(i < 76) {
                settings.interval = (i-60) * 5 + 40;
                intervalTextValue = Double.toString(settings.interval);
                intervalUnit = "s";
            }
            else if(i < 93) {
                settings.interval = (i-76) * 30 + 120;
                intervalTextValue = Double.toString((i-76) * 0.5 + 2);
                intervalUnit = "min";
            }
            else if(i < 120) {
                settings.interval = (i-93) * 60 + 660;
                intervalTextValue = Integer.toString(i-93+11);
                intervalUnit = "min";
            }
            tvIntervalValue.setText(intervalTextValue);
            tvIntervalUnit.setText(intervalUnit);

            if(settings.interval == 0)
                lblShots.setText("Dur. (s)");
            else
                lblShots.setText("Shots");

            updateTimes();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    },

    sbDelayOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            String delayTextValue = "";
            String delayUnit = "";

            if(i <= 59) {
                settings.rawDelay = i;
                settings.delay = i * 1000;
                delayTextValue = Integer.toString(settings.rawDelay);
                delayUnit = "s";
            }else if(i <= 59+179) {
                settings.rawDelay = i - 59;
                settings.delay = settings.rawDelay * 60 * 1000;
                delayTextValue = Integer.toString(settings.rawDelay);
                delayUnit = "m";
            }else if(i > 59+179) {
                settings.rawDelay = i - 59-180 + 2;
                settings.delay = settings.rawDelay * 60 * 60 * 1000;
                delayTextValue = Integer.toString(settings.rawDelay);
                delayUnit = "h";
            }

            tvDelayValue.setText(delayTextValue);
            tvDelayUnit.setText(delayUnit);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    },

    sbShotsOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
            String shotsText;

            settings.rawShotCount = i;

            i++;

            if(i <= 20)
                settings.shotCount = i;

            else if(i <= 36)
                settings.shotCount = (i-20) * 5 + 20;

            else if(i <= 81)
                settings.shotCount = (i-36) * 20 + 100;

            else if(i <= 100)
                settings.shotCount = (i-81) * 50 + 1000;

            else if(i <= 130)
                settings.shotCount = (i-100) * 100 + 2000;

            shotsText = Integer.toString(settings.shotCount);

            if(i == 131) {
                settings.shotCount = Integer.MAX_VALUE;
                shotsText = "inf";
            }

            tvShotsValue.setText(shotsText);
            updateTimes();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };

    AdapterView.OnItemSelectedListener spnFpsOnItemSelectedListener
            = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String sfps = getResources().getStringArray(R.array.fps)[i];
            fps = Integer.parseInt(sfps);
            settings.fps = i;
            updateTimes();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    };

    CheckBox.OnCheckedChangeListener cbSilentShutterOnCheckListener = new CheckBox.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            settings.silentShutter = b;
        }
    };

    CheckBox.OnCheckedChangeListener cbAELOnCheckListener = new CheckBox.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            settings.ael = b;
        }
    };

    CheckBox.OnCheckedChangeListener cbBRSOnCheckListener = new CheckBox.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            settings.brs = b;
        }
    };

    void updateTimes() {
        if(settings.shotCount == Integer.MAX_VALUE)
        {
            tvDurationValue.setText("inf");
            tvDurationUnit.setText("");
            tvVideoTimeValue.setText("inf");
            tvVideoTimeUnit.setText("");
            return;
        }

        int duration = 0;
        int videoTime = 0;
        if(settings.interval == 0) {
            duration = settings.shotCount;
            videoTime = -1;
        }
        else {
            duration = (int) Math.round(settings.interval * settings.shotCount);
            videoTime = settings.shotCount / fps;
        }

        if(duration < 60) {
            tvDurationValue.setText("" + duration);
            tvDurationUnit.setText("s");
        }
        else {
            tvDurationValue.setText("" + (duration / 60));
            tvDurationUnit.setText("min");
        }

        if(videoTime == -1) {
            tvVideoTimeValue.setText("");
            tvVideoTimeUnit.setText("");
        }
        else if(videoTime < 120) {
            tvVideoTimeValue.setText("" + videoTime);
            tvVideoTimeUnit.setText("s");
        }
        else {
            tvVideoTimeValue.setText("" + (videoTime / 60));
            tvVideoTimeUnit.setText("min");
        }
    }

    protected boolean onUpperDialChanged(int value) {
        sbDelay.dialChanged(value);
        sbInterval.dialChanged(value);
        sbShots.dialChanged(value);
        return true;
    }

    protected boolean onLowerDialChanged(int value) {
        sbDelay.dialChanged(value);
        sbInterval.dialChanged(value);
        sbShots.dialChanged(value);
        return true;
    }

    protected boolean onThirdDialChanged(int value) {
        sbDelay.dialChanged(value);
        sbInterval.dialChanged(value);
        sbShots.dialChanged(value);
        return true;
    }

    protected boolean onKuruDialChanged(int value) {
        sbDelay.dialChanged(value);
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
