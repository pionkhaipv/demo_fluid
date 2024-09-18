package com.demo.fluid.util.gl;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import com.magicfluids.MotionEventWrapper;
import com.magicfluids.NativeInterface;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public final class GLES20RendererLWP implements GLSurfaceView.Renderer {
    private Activity activity;
    private int avgTimeNumSamples;
    private long avgTimeSamplesSum;
    private boolean ignoreNextFrameTime;
    private InputLWP input = new InputLWP();
    private long lastNanoTime;
    private long maxTime;
    private NativeInterface nativeInterface;
    private OrientationSensor orientationSensor;
    private int screenHeight;
    private int screenWidth;

    public GLES20RendererLWP(NativeInterface nativeInterface, OrientationSensor orientationSensor) {
        this.nativeInterface = nativeInterface;
        this.orientationSensor = orientationSensor;
    }

    public NativeInterface getNativeInterface() {
        return this.nativeInterface;
    }

    public void setNativeInterface(NativeInterface nativeInterface2) {
        this.nativeInterface = nativeInterface2;
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
        InputBufferLWP.Instance.getCurrentInputState(this.input);
        int numEvents = this.input.getNumEvents();
        for (int i2 = 0; i2 < numEvents; i2++) {
            if (i2 >= 32) {
                MotionEventWrapper motionEventWrapper = this.input.getEvents()[i2];
                if (motionEventWrapper.Type == 2) {
                }
            }
            this.nativeInterface.onMotionEvent(this.input.getEvents()[i2]);
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
