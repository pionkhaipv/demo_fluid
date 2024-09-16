package com.demo.fluid.gl;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import com.magicfluids.MotionEventWrapper;
import com.magicfluids.NativeInterface;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public final class GLES20Renderer implements GLSurfaceView.Renderer {
    private Activity activity;
    private int avgTimeNumSamples;
    private long avgTimeSamplesSum;
    private boolean ignoreNextFrameTime;
    private Input input = new Input();
    private long lastNanoTime;
    private long maxTime;
    private final NativeInterface nativeInterface;
    private final OrientationSensor orientationSensor;
    private int screenHeight;
    private int screenWidth;

    public GLES20Renderer(NativeInterface nativeInterface2, OrientationSensor orientationSensor2) {
        this.nativeInterface = nativeInterface2;
        this.orientationSensor = orientationSensor2;
    }

    public Activity getActivity() {
        return this.activity;
    }

    public void setActivity(Activity activity2) {
        this.activity = activity2;
    }

    private boolean isLWP() {
        return this.activity == null;
    }

    public void setInitialScreenSize(int i, int i2) {
        this.screenWidth = i;
        this.screenHeight = i2;
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        long nanoTime = System.nanoTime();
        long j = nanoTime - this.lastNanoTime;
        this.avgTimeSamplesSum += j;
        int i = this.avgTimeNumSamples + 1;
        this.avgTimeNumSamples = i;
        if (j > this.maxTime) {
            this.maxTime = j;
        }
        this.lastNanoTime = nanoTime;
        if (i == 25) {
            this.avgTimeSamplesSum = 0;
            this.avgTimeNumSamples = 0;
            this.maxTime = 0;
        }
        InputBuffer.Instance.getCurrentInputState(this.input);
        int i2 = this.input.NumEvents;
        for (int i3 = 0; i3 < i2; i3++) {
            if (i3 >= 32) {
                MotionEventWrapper motionEventWrapper = this.input.Events[i3];
                if (motionEventWrapper.Type == 2) {
                }
            }
            this.nativeInterface.onMotionEvent(this.input.Events[i3]);
        }
        this.nativeInterface.updateApp(this.ignoreNextFrameTime, false, this.orientationSensor.getAccelerationX(), this.orientationSensor.getAccelerationY(), this.orientationSensor.getOrientation());
        this.ignoreNextFrameTime = false;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        if (!isLWP()) {
            checkGPUExtensions();
            onEGLContextCreated();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        this.screenWidth = i;
        this.screenHeight = i2;
        this.nativeInterface.windowChanged(i, i2);
    }

    private void checkGPUExtensions() {
        GLES20.glGetString(7939);
        this.nativeInterface.setAvailableGPUExtensions(true, true);
    }

    private void onEGLContextCreated() {
        this.nativeInterface.onGLContextRestarted();
    }
}
