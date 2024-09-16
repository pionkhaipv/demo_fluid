package com.demo.fluid.gl;

import android.app.Application;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.WindowManager;


public final class OrientationSensor implements SensorEventListener {
    private float AccelerationX;
    private float AccelerationY;
    private int Orientation;
    private float[] acceleration;
    private Sensor accelerometer;
    private Application application;
    private boolean isRegistered;
    private SensorManager sensorManager;

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public OrientationSensor(Context context, Application application2) {
        Object systemService = context.getSystemService("sensor");
        SensorManager sensorManager2 = (SensorManager) systemService;
        this.sensorManager = sensorManager2;
        Sensor defaultSensor = sensorManager2.getDefaultSensor(1);
        this.accelerometer = defaultSensor;
        this.application = application2;
    }

    public float getAccelerationX() {
        return this.AccelerationX;
    }

    public void setAccelerationX(float f) {
        this.AccelerationX = f;
    }

    public float getAccelerationY() {
        return this.AccelerationY;
    }

    public void setAccelerationY(float f) {
        this.AccelerationY = f;
    }

    public int getOrientation() {
        return this.Orientation;
    }

    public void setOrientation(int i) {
        this.Orientation = i;
    }

    public void register() {
        if (!this.isRegistered) {
            this.sensorManager.registerListener(this, this.accelerometer, 3);
            this.isRegistered = true;
        }
    }

    public void unregister() {
        if (this.isRegistered) {
            this.sensorManager.unregisterListener(this);
            this.isRegistered = false;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] fArr = (float[]) sensorEvent.values.clone();
        this.acceleration = fArr;
        this.AccelerationX = fArr[1];
        this.AccelerationY = fArr[0];
        Object systemService = this.application.getSystemService("window");
        this.Orientation = ((WindowManager) systemService).getDefaultDisplay().getRotation();
    }
}
