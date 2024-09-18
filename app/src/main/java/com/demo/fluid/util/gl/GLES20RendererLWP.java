package com.demo.fluid.util.gl;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.SurfaceHolder;

import com.magicfluids.MotionEventWrapper;
import com.magicfluids.NativeInterface;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

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

    private Bitmap imageBitmap;

    private int shaderProgram;
    private int positionHandle;
    private int colorHandle;
    private FloatBuffer vertexBuffer;

    private float[] triangleVertices = {
            0.0f,  0.5f, 0.0f,  // Đỉnh trên cùng
            -0.5f, -0.5f, 0.0f,  // Đỉnh trái
            0.5f, -0.5f, 0.0f   // Đỉnh phải
    };


    public GLES20RendererLWP(NativeInterface nativeInterface, OrientationSensor orientationSensor, Bitmap bitmap) {
        this.nativeInterface = nativeInterface;
        this.orientationSensor = orientationSensor;
        this.imageBitmap = bitmap;

        // Trong constructor
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleVertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleVertices);
        vertexBuffer.position(0);
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
    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        if (!isLWP()) {
            checkGPUExtensions();
            onEGLContextCreated();
        }

        // Tạo shader và chương trình shader
        String vertexShaderCode =
                "attribute vec4 vPosition;" +
                        "void main() {" +
                        "  gl_Position = vPosition;" +
                        "}";

        String fragmentShaderCode =
                "precision mediump float;" +
                        "uniform vec4 uColor;" +
                        "void main() {" +
                        "  gl_FragColor = uColor;" +
                        "}";

        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShader, vertexShaderCode);
        GLES20.glCompileShader(vertexShader);

        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader, fragmentShaderCode);
        GLES20.glCompileShader(fragmentShader);

        shaderProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(shaderProgram, vertexShader);
        GLES20.glAttachShader(shaderProgram, fragmentShader);
        GLES20.glLinkProgram(shaderProgram);

        // Xác định các handles
        positionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
        colorHandle = GLES20.glGetUniformLocation(shaderProgram, "uColor");
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
        drawTriangle();
    }

    private void drawTriangle() {
        GLES20.glUseProgram(shaderProgram);

        vertexBuffer.position(0);
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        GLES20.glUniform4f(colorHandle, 1.0f, 1.0f, 1.0f, 1.0f); // Màu trắng
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

        GLES20.glDisableVertexAttribArray(positionHandle);
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

