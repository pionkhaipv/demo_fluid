package com.demo.fluid.service;

import android.app.Application;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
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
        Config config2 = Config.Current;
        this.config = config2;
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
            SurfaceHolder surfaceHolder = engine.getSurfaceHolder();
            return surfaceHolder;
        }
    }

    public final class MyEngine extends Engine {
        public MyEngine() {
            super();
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            NewWallpaperService.this.setMGLSurfaceView(new WallpaperGLSurfaceView(NewWallpaperService.this));
            NativeInterface.init();
            setTouchEventsEnabled(true);
            loadConfigPreset();
            NewWallpaperService.this.nativeInterface = new NativeInterface();
            NativeInterface accessgetNativeInterfacep = NewWallpaperService.this.nativeInterface;
            accessgetNativeInterfacep.setAssetManager(NewWallpaperService.this.getAssets());
            NewWallpaperService newWallpaperService = NewWallpaperService.this;
            NewWallpaperService newWallpaperService2 = NewWallpaperService.this;
            Application application = newWallpaperService2.getApplication();
            newWallpaperService.setOrientationSensor(new OrientationSensor(newWallpaperService2, application));
            GLSurfaceView mGLSurfaceView = NewWallpaperService.this.getMGLSurfaceView();
            mGLSurfaceView.setEGLContextClientVersion(2);
            NewWallpaperService newWallpaperService3 = NewWallpaperService.this;
            NativeInterface accessgetNativeInterfacep2 = NewWallpaperService.this.nativeInterface;
            OrientationSensor orientationSensor = NewWallpaperService.this.getOrientationSensor();
            newWallpaperService3.setRenderer(new GLES20RendererLWP(accessgetNativeInterfacep2, orientationSensor));
            GLSurfaceView mGLSurfaceView2 = NewWallpaperService.this.getMGLSurfaceView();
            mGLSurfaceView2.setRenderer(NewWallpaperService.this.getRenderer());
            if (NewWallpaperService.this.getRenderer() != null) {
                GLES20RendererLWP renderer = NewWallpaperService.this.getRenderer();
                renderer.setInitialScreenSize(300, 200);
                Log.d("sagawgwagagw", "onCreate: chay vao 1");
            }
            if (NewWallpaperService.this.nativeInterface != null) {
                NativeInterface accessgetNativeInterfacep3 = NewWallpaperService.this.nativeInterface;
                accessgetNativeInterfacep3.onCreate(300, 200, false);
                Log.d("sagawgwagagw", "onCreate: chay vao 2");
            }
            if (NewWallpaperService.this.nativeInterface != null) {
                NativeInterface accessgetNativeInterfacep4 = NewWallpaperService.this.nativeInterface;
                accessgetNativeInterfacep4.updateConfig(NewWallpaperService.this.config);
                Log.d("sagawgwagagw", "onCreate: chay vao 3");
            }
            GLSurfaceView mGLSurfaceView3 = NewWallpaperService.this.getMGLSurfaceView();
            mGLSurfaceView3.onResume();
            NativeInterface accessgetNativeInterfacep5 = NewWallpaperService.this.nativeInterface;
            accessgetNativeInterfacep5.onResume();
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder surfaceHolder) {
            super.onSurfaceCreated(surfaceHolder);
        }

        @Override
        public void onVisibilityChanged(boolean z) {
            super.onVisibilityChanged(z);
            if (z) {
                if (NewWallpaperService.this.nativeInterface != null) {
                    NativeInterface accessgetNativeInterfacep = NewWallpaperService.this.nativeInterface;
                    accessgetNativeInterfacep.updateConfig(Config.LWPCurrent);
                }
                if (NewWallpaperService.this.nativeInterface != null) {
                    NativeInterface accessgetNativeInterfacep2 = NewWallpaperService.this.nativeInterface;
                    accessgetNativeInterfacep2.onResume();
                }
            } else if (NewWallpaperService.this.nativeInterface != null) {
                NativeInterface accessgetNativeInterfacep3 = NewWallpaperService.this.nativeInterface;
                accessgetNativeInterfacep3.onResume();
            }
        }

        private void loadConfigPreset() {
            String nameWallpaper = Common.INSTANCE.getNameWallpaper(NewWallpaperService.this);
            SettingsStorage.loadConfigFromInternalPreset(nameWallpaper, NewWallpaperService.this.getAssets(), NewWallpaperService.this.config);
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
