package com.jonasjuffinger.timelapse;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ma1co.pmcademo.app.BaseActivity;
import com.sony.scalar.hardware.CameraEx;
import com.sony.scalar.sysutil.ScalarProperties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends BaseActivity implements SurfaceHolder.Callback
{
    private SurfaceHolder       m_surfaceHolder;
    private CameraEx            m_camera;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler))
            Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler());

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        m_surfaceHolder = surfaceView.getHolder();
        m_surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        m_camera = CameraEx.open(0, null);
        m_surfaceHolder.addCallback(this);

    }

    @Override
    protected boolean onMenuKeyUp()
    {
        onBackPressed();
        return true;
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        m_surfaceHolder.removeCallback(this);
        m_camera.setAutoPictureReviewControl(null);
        m_camera.getNormalCamera().stopPreview();
        m_camera.release();
        m_camera = null;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        try
        {
            Camera cam = m_camera.getNormalCamera();
            cam.setPreviewDisplay(holder);
            cam.startPreview();
        }
        catch (IOException e)
        {
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
}
