package com.demo.fluid.util.gl;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.MotionEvent;

import com.demo.fluid.R;
import com.magicfluids.MotionEventWrapper;
import com.magicfluids.NativeInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public final class GLES20RendererLWP implements GLSurfaceView.Renderer {
    private Activity activity;
    private Application application;
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
    private int texCoordHandle;
    private int textureHandle;
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;

    private int[] textures = new int[1]; // Mảng lưu texture

    private float[] triangleVertices = {
            -1.0f, 1.0f, 0.0f,  // Đỉnh trên cùng trái
            -1.0f, -1.0f, 0.0f, // Đỉnh dưới cùng trái
            1.0f, -1.0f, 0.0f,  // Đỉnh dưới cùng phải
            1.0f, 1.0f, 0.0f    // Đỉnh trên cùng phải
    };


    private float[] textureCoords = {
            0.0f, 0.0f,   // Top-left
            0.0f, 1.0f,   // Bottom-left
            1.0f, 1.0f,   // Bottom-right
            1.0f, 0.0f    // Top-right
    };

    public GLES20RendererLWP(NativeInterface nativeInterface, OrientationSensor orientationSensor, Bitmap bitmap, Application application) {
        this.nativeInterface = nativeInterface;
        this.orientationSensor = orientationSensor;
        this.imageBitmap = bitmap;
        this.application = application;

        // Khởi tạo vertexBuffer
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleVertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleVertices);
        vertexBuffer.position(0);

        // Khởi tạo textureBuffer
        ByteBuffer tb = ByteBuffer.allocateDirect(textureCoords.length * 4);
        tb.order(ByteOrder.nativeOrder());
        textureBuffer = tb.asFloatBuffer();
        textureBuffer.put(textureCoords);
        textureBuffer.position(0);
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
        // Kích hoạt blending
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        // Tạo shader và chương trình shader
        String vertexShaderCode =
                "attribute vec4 vPosition;" +
                        "attribute vec2 aTexCoord;" +
                        "varying vec2 vTexCoord;" +
                        "void main() {" +
                        "  gl_Position = vPosition;" +
                        "  vTexCoord = aTexCoord;" +
                        "}";

        String fragmentShaderCode =
                "precision mediump float;" +
                        "uniform sampler2D uTexture;" +
                        "varying vec2 vTexCoord;" +
                        "void main() {" +
                        "    vec4 texColor = texture2D(uTexture, vTexCoord);" +
                        "    if (texColor.a < 0.1) {" +
                        "        discard;" +
                        "    }" +
                        "    gl_FragColor = texColor;" +
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
        texCoordHandle = GLES20.glGetAttribLocation(shaderProgram, "aTexCoord");
        textureHandle = GLES20.glGetUniformLocation(shaderProgram, "uTexture");

        // Kích hoạt blending
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        // Tạo texture
        loadTexture(application, R.drawable.aaaaa); // Thay bằng ảnh PNG của bạn
        // Kích hoạt blending
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
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
        drawTexture(); // Gọi phương thức vẽ texture
    }

    private void drawTexture() {
        GLES20.glUseProgram(shaderProgram);

        // Kích hoạt và gán dữ liệu cho vị trí đỉnh
        vertexBuffer.position(0);
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        // Gán dữ liệu cho tọa độ texture
        textureBuffer.position(0);
        GLES20.glEnableVertexAttribArray(texCoordHandle);
        GLES20.glVertexAttribPointer(texCoordHandle, 2, GLES20.GL_FLOAT, false, 0, textureBuffer);

        // Kích hoạt texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLES20.glUniform1i(textureHandle, 0);

        // Vẽ hình vuông
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);

        // Tắt các attribute
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(texCoordHandle);
    }

    private void loadTexture(Context context, int resourceId) {
        GLES20.glGenTextures(1, textures, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

        // Chuyển đổi ảnh thành texture
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bitmap.getWidth(), bitmap.getHeight(), 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();
    }



    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        this.screenWidth = i;
        this.screenHeight = i2;
        Log.d("asgaggawgwa", "onSurfaceChanged: width" +i);
        Log.d("asgaggawgwa", "onSurfaceChanged: width" +i2);
        this.nativeInterface.windowChanged(i, i2);
    }

    private void checkGPUExtensions() {
        GLES20.glGetString(GLES20.GL_EXTENSIONS);
        this.nativeInterface.setAvailableGPUExtensions(true, true);
    }

    private void onEGLContextCreated() {
        this.nativeInterface.onGLContextRestarted();
    }
}
