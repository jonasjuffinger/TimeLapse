package com.jonasjuffinger.timelapse;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.github.ma1co.pmcademo.app.BaseActivity;

import com.sony.scalar.hardware.CameraEx;

import java.io.IOException;
import java.util.List;

public class ShootActivity  extends BaseActivity implements SurfaceHolder.Callback, CameraEx.ShutterListener
{
    public static final String EXTRA_INTERVAL = "com.jonasjuffinger.timelapse.INTERVAL";
    public static final String EXTRA_SHOTCOUNT = "com.jonasjuffinger.timelapse.SHOTCOUNT";

    Logger log;

    private int interval, maxShotCount;
    private int shotCount;

    private TextView tvCount, tvInfo;

    private SurfaceHolder cameraSurfaceHolder;
    private CameraEx cameraEx;
    private Camera camera;
    private CameraEx.AutoPictureReviewControl autoReviewControl;
    private int pictureReviewTime;


    private boolean stopPicturePreview;
    private boolean takingPicture;

    private long shootTime;

    private Handler shootRunnableHandler = new Handler();
    private final Runnable shootRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            if(stopPicturePreview) {
                stopPicturePreview = false;
                camera.stopPreview();
            }

            if(shotCount < maxShotCount) {
                long remainingTime = shootTime + interval * 1000 - System.currentTimeMillis();

                log("  Remaining Time: " + Long.toString(remainingTime));

                if (remainingTime <= 0) {
                    shoot();
                } else {
                    shootRunnableHandler.postDelayed(this, remainingTime);
                }
            }
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvCount.setText("FINISHED! press MENU");
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        log("onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoot);

        Intent intent = getIntent();
        interval = intent.getIntExtra(EXTRA_INTERVAL, 1);
        maxShotCount = intent.getIntExtra(EXTRA_SHOTCOUNT, 0);

        shotCount = 0;
        takingPicture = false;

        tvCount = (TextView) findViewById(R.id.tvCount);
        tvInfo = (TextView) findViewById(R.id.tvInfo);

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        cameraSurfaceHolder = surfaceView.getHolder();
        cameraSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    @Override
    protected void onResume() {
        log("onResume");

        super.onResume();
        cameraEx = CameraEx.open(0, null);
        cameraEx.setShutterListener(this);
        cameraSurfaceHolder.addCallback(this);
        autoReviewControl = new CameraEx.AutoPictureReviewControl();
        cameraEx.setAutoPictureReviewControl(autoReviewControl);

        pictureReviewTime = autoReviewControl.getPictureReviewTime();
        log(Integer.toString(pictureReviewTime));

        shootRunnableHandler.postDelayed(shootRunnable, interval * 1000);

    }

    @Override
    protected boolean onMenuKeyUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        log("on pause");

        shootRunnableHandler.removeCallbacks(shootRunnable);

        if(cameraSurfaceHolder == null)
            log("cameraSurfaceHolder == null");
        else {
            cameraSurfaceHolder.removeCallback(this);
        }

        autoReviewControl = null;

        if(camera == null)
            log("camera == null");
        else {
            camera.stopPreview();
            camera = null;
        }

        if(cameraEx == null)
            log("cameraEx == null");
        else {
            cameraEx.setAutoPictureReviewControl(null);
            cameraEx.release();
            cameraEx = null;
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = cameraEx.getNormalCamera();
            camera.setPreviewDisplay(holder);
        }
        catch (IOException e) {}
    }


    private void shoot() {
        if(takingPicture)
            return;

        shootTime = System.currentTimeMillis();

        cameraEx.burstableTakePicture();

        shotCount++;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvCount.setText(Integer.toString(shotCount));
            }
        });
    }


    @Override
    public void onShutter(int i, CameraEx cameraEx) {
        this.cameraEx.cancelTakePicture();

        camera.startPreview();

        if(shotCount < maxShotCount) {

            // remaining time to the next shot
            long remainingTime = shootTime + interval * 1000 - System.currentTimeMillis();

            log("Remaining Time: " + Long.toString(remainingTime));

            // if the remaining time is negative immediately take the next picture
            if (remainingTime < 0) {
                stopPicturePreview = false;
                shootRunnableHandler.post(shootRunnable);
            }
            // show the preview picture for some time
            else {
                long previewPictureShowTime = Math.min(remainingTime, pictureReviewTime * 1000);
                stopPicturePreview = true;
                shootRunnableHandler.postDelayed(shootRunnable, previewPictureShowTime);
            }
        }
        else {
            stopPicturePreview = true;
            shootRunnableHandler.postDelayed(shootRunnable, pictureReviewTime * 1000);
        }
    }



    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {}

    @Override
    protected void setColorDepth(boolean highQuality)
    {
        super.setColorDepth(false);
    }


    private void log(String s) {
        Logger.info(s);
    }

    private void dumpList(List list, String name) {
        log(name);
        log(": ");
        if (list != null)
        {
            for (Object o : list)
            {
                log(o.toString());
                log(" ");
            }
        }
        else
            log("null");
        log("\n");
    }
}
