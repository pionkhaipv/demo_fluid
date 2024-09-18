package com.demo.fluid.framework.presentation.wallpaper_service;

import android.hardware.Camera;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class DecompiledCameraWallpaper extends WallpaperService {

    public class CameraService extends Engine implements Camera.PreviewCallback {
        private int currentCameraId = 0;
        public Camera mCamera;

        public void startCamera() {
            try {
                Camera open = Camera.open(currentCameraId);
                this.mCamera = open;

                if (currentCameraId != 1) {
                    Camera.Parameters parameters = open.getParameters();
                    parameters.setFocusMode("continuous-picture");
                    this.mCamera.setParameters(parameters);
                }

                this.mCamera.setDisplayOrientation(90);
                this.mCamera.setPreviewDisplay(getSurfaceHolder());
                this.mCamera.startPreview();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        public void stopCamera() {
            Camera camera = this.mCamera;
            if (camera != null) {
                try {
                    camera.stopPreview();
//                    this.mCamera.setPreviewCallback((Camera.PreviewCallback) null);
                    this.mCamera.release();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                this.mCamera = null;
            }
        }

        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            currentCameraId = 0;
            startCamera();
            setTouchEventsEnabled(true);
        }

        public void onDestroy() {
            super.onDestroy();
            stopCamera();
        }

        public void onPreviewFrame(byte[] bArr, Camera camera) {
            camera.addCallbackBuffer(bArr);
        }

        public void onTouchEvent(MotionEvent motionEvent) {
            super.onTouchEvent(motionEvent);
        }

        public void onVisibilityChanged(boolean z) {
            if (z) {
                startCamera();
            }
            else {
                stopCamera();
            }
        }
    }

    public Engine onCreateEngine() {
        return new CameraService();
    }
}
