package com.demo.fluid.framework.presentation.wallpaper_service;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.opengl.GLSurfaceView;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.demo.fluid.R;
import com.magicfluids.Config;
import com.magicfluids.NativeInterface;

import kotlin.jvm.internal.DefaultConstructorMarker;

import com.demo.fluid.util.gl.GLES20RendererLWP;
import com.demo.fluid.util.gl.InputBufferLWP;
import com.demo.fluid.util.gl.OrientationSensor;
import com.demo.fluid.util.gl.SettingsStorage;
import com.demo.fluid.util.Common;

public final class NewWallpaperService extends WallpaperService {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);

    public static MyEngine engine;

    public final Config config;
    private GLSurfaceView mGLSurfaceView;

    public NativeInterface nativeInterface;
    private OrientationSensor orientationSensor;
    private GLES20RendererLWP renderer;
    public NewWallpaperService() {
        this.config = Config.Current;
    }

    public GLSurfaceView getMGLSurfaceView() {
        return this.mGLSurfaceView;
    }

    public void setMGLSurfaceView(GLSurfaceView gLSurfaceView) {
        this.mGLSurfaceView = gLSurfaceView;
    }

    public OrientationSensor getOrientationSensor() {
        return this.orientationSensor;
    }

    public void setOrientationSensor(OrientationSensor orientationSensor2) {
        this.orientationSensor = orientationSensor2;
    }

    public GLES20RendererLWP getRenderer() {
        return this.renderer;
    }

    public void setRenderer(GLES20RendererLWP gLES20RendererLWP) {
        this.renderer = gLES20RendererLWP;
    }

    public Engine onCreateEngine() {
        MyEngine myEngine = new MyEngine();
        engine = myEngine;
        return myEngine;
    }

    public static final class WallpaperGLSurfaceView extends GLSurfaceView {
        public WallpaperGLSurfaceView(Context context) {
            super(context);
        }

        public SurfaceHolder getHolder() {
            MyEngine engine = NewWallpaperService.Companion.getEngine();
            return engine.getSurfaceHolder();
        }
    }

    public final class MyEngine extends Engine {
        public MyEngine() {
            super();
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            setMGLSurfaceView(new WallpaperGLSurfaceView(NewWallpaperService.this));
            NativeInterface.init();
            setTouchEventsEnabled(true);
            loadConfigPreset();
            nativeInterface = new NativeInterface();
            nativeInterface.setAssetManager(getAssets());
            NewWallpaperService newWallpaperService = NewWallpaperService.this;
            Application application = newWallpaperService.getApplication();
            newWallpaperService.setOrientationSensor(new OrientationSensor(newWallpaperService, application));
            GLSurfaceView mGLSurfaceView = getMGLSurfaceView();
            mGLSurfaceView.setEGLContextClientVersion(2);
            OrientationSensor orientationSensor = getOrientationSensor();
            Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_fluid_home);

            newWallpaperService.setRenderer(new GLES20RendererLWP(nativeInterface, orientationSensor,imageBitmap));
            mGLSurfaceView.setRenderer(getRenderer());
            if (getRenderer() != null) {
                GLES20RendererLWP renderer = getRenderer();
                renderer.setInitialScreenSize(300, 200);
            }
            if (nativeInterface != null) {
                nativeInterface.onCreate(300, 200, false);
                nativeInterface.updateConfig(NewWallpaperService.this.config);
            }
            getMGLSurfaceView().onResume();
            nativeInterface.onResume();

        }

        @Override
        public void onSurfaceCreated(SurfaceHolder surfaceHolder) {
            super.onSurfaceCreated(surfaceHolder);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
        }
        @Override
        public void onVisibilityChanged(boolean z) {
            super.onVisibilityChanged(z);
            if (z) {
                if (nativeInterface != null) {
                    nativeInterface.updateConfig(Config.LWPCurrent);
                    nativeInterface.onResume();
                }
            } else if (nativeInterface != null) {
                nativeInterface.onResume();
            }
        }

        private void loadConfigPreset() {
            String nameWallpaper = Common.INSTANCE.getNameWallpaper(NewWallpaperService.this);
            SettingsStorage.loadConfigFromInternalPreset(nameWallpaper, getAssets(), config);
        }

        @Override
        public void onTouchEvent(MotionEvent motionEvent) {
            super.onTouchEvent(motionEvent);
            Log.d("TAG=====", "onTouchEvent: ");
            InputBufferLWP.Instance.addEvent(motionEvent);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }
    }

    public static final class Companion {
        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public MyEngine getEngine() {
            return NewWallpaperService.engine;
        }

        public void setEngine(MyEngine myEngine) {
            NewWallpaperService.engine = myEngine;
        }
    }


}

