package com.magicfluids;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;

public class NativeInterface {
    public static boolean loadingFailed = false;
    public static final Object nativeLock = new Object();
    private static int nextID = 0;
    public static Object nextIdLock = new Object();
    private static byte[] tmpBuf = new byte[2097152];
    private int ID;
    private AssetManager mAssetMgr;
    private volatile boolean mDrawOnly = false;
    private volatile boolean mPaused = true;

    private native void clearScreenImpl(int i);

    private native boolean isConfigBackgroundBrightImpl(int i, Config config);

    private native void onCreateImpl(int i, int i2, int i3, boolean z);

    private native void onDestroyImpl(int i);

    private native void onGLContextRestartedImpl(int i);

    private native void onMotionEventImpl(int i, MotionEventWrapper motionEventWrapper);

    private native void onPauseImpl(int i);

    private native void onResumeImpl(int i);

    private native int perfHeuristicImpl(int i);

    private native void setAvailableGPUExtensionsImpl(int i, boolean z, boolean z2);

    private native void updateAppImpl(int i, boolean z, boolean z2, boolean z3, float f, float f2, int i2);

    private native void updateConfigImpl(int i, Config config, boolean z);

    private native void windowChangedImpl(int i, int i2, int i3);

    public static void init() {
        try {
            System.loadLibrary("nativelib");
        } catch (UnsatisfiedLinkError ignored) {
        }
    }

    public int getID() {
        return this.ID;
    }

    public NativeInterface() {
        synchronized (nextIdLock) {
            int i = nextID;
            nextID = i + 1;
            this.ID = i;
        }
    }

    private boolean startPause() {
        boolean z = this.mPaused;
        if (!this.mPaused) {
            this.mPaused = true;
        }
        return z;
    }

    private void endPause(boolean z) {
        if (!z) {
            this.mPaused = false;
        }
    }

    public void windowChanged(int i, int i2) {
        if (!loadingFailed) {
            synchronized (nativeLock) {
                windowChangedImpl(this.ID, i, i2);
            }
        }
    }

    public void updateApp(boolean z, boolean z2, float f, float f2, int i) {
        if (!loadingFailed && !this.mPaused) {
            synchronized (nativeLock) {
                updateAppImpl(this.ID, z, this.mDrawOnly, z2, f, f2, i);
            }
        }
    }

    public void onMotionEvent(MotionEventWrapper motionEventWrapper) {
        if (!loadingFailed) {
            synchronized (nativeLock) {
                onMotionEventImpl(this.ID, motionEventWrapper);
            }
        }
    }

    public void onCreate(int i, int i2, boolean z) {
        if (!loadingFailed) {
            if (!this.mPaused) {
                this.mPaused = true;
                synchronized (nativeLock) {
                    onCreateImpl(this.ID, i, i2, z);
                }
                this.mPaused = false;
                return;
            }
            synchronized (nativeLock) {
                onCreateImpl(this.ID, i, i2, z);
            }
        }
    }

    public void onDestroy() {
        if (!loadingFailed) {
            if (!this.mPaused) {
                this.mPaused = true;
                synchronized (nativeLock) {
                    onDestroyImpl(this.ID);
                }
                this.mPaused = false;
                return;
            }
            synchronized (nativeLock) {
                onDestroyImpl(this.ID);
            }
        }
    }

    public void onPause() {
        if (!loadingFailed && !this.mPaused) {
            this.mPaused = true;
            synchronized (nativeLock) {
                onPauseImpl(this.ID);
            }
        }
    }

    public void onResume() {
        if (!loadingFailed && this.mPaused) {
            this.mPaused = false;
            synchronized (nativeLock) {
                onResumeImpl(this.ID);
            }
        }
    }

    public void clearScreen() {
        if (!loadingFailed) {
            if (!this.mPaused) {
                this.mPaused = true;
                synchronized (nativeLock) {
                    clearScreenImpl(this.ID);
                }
                this.mPaused = false;
                return;
            }
            synchronized (nativeLock) {
                clearScreenImpl(this.ID);
            }
        }
    }

    public void onGLContextRestarted() {
        if (!loadingFailed) {
            synchronized (nativeLock) {
                onGLContextRestartedImpl(this.ID);
            }
        }
    }

    private void updateRandomizeConfig(Config config, boolean z) {
        if (!loadingFailed) {
            config.updateNativeArrays();
            if (!this.mPaused) {
                this.mPaused = true;
                synchronized (nativeLock) {
                    updateConfigImpl(this.ID, config, z);
                }
                this.mPaused = false;
                return;
            }
            synchronized (nativeLock) {
                updateConfigImpl(this.ID, config, z);
            }
        }
    }

    public void updateConfig(Config config) {
        updateRandomizeConfig(config, false);
    }

    public void randomizeConfig(Config config) {
        updateRandomizeConfig(config, true);
    }

    public boolean isConfigBackgroundBright(Config config) {
        boolean z;
        if (loadingFailed) {
            return false;
        }
        config.updateNativeArrays();
        if (!this.mPaused) {
            this.mPaused = true;
            synchronized (nativeLock) {
                z = isConfigBackgroundBrightImpl(this.ID, config);
            }
            this.mPaused = false;
        } else {
            synchronized (nativeLock) {
                z = isConfigBackgroundBrightImpl(this.ID, config);
            }
        }
        return z;
    }

    public void setAvailableGPUExtensions(boolean z, boolean z2) {
        if (!loadingFailed) {
            if (!this.mPaused) {
                this.mPaused = true;
                synchronized (nativeLock) {
                    setAvailableGPUExtensionsImpl(this.ID, z, z2);
                }
                this.mPaused = false;
                return;
            }
            synchronized (nativeLock) {
                setAvailableGPUExtensionsImpl(this.ID, z, z2);
            }
        }
    }

    public void onPauseAnim() {
        this.mDrawOnly = true;
    }

    public void onResumeAnim() {
        this.mDrawOnly = false;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.mAssetMgr = assetManager;
    }

    public ByteArrayWithSize loadFileContentsFromAssets(String str) {
        ByteArrayWithSize byteArrayWithSize = new ByteArrayWithSize((byte[]) null, 0);
        try {
            int read = this.mAssetMgr.open(str).read(tmpBuf);
            byte[] bArr = new byte[read];
            System.arraycopy(tmpBuf, 0, bArr, 0, read);
            return new ByteArrayWithSize(bArr, read);
        } catch (IOException e) {
            e.printStackTrace();
            return byteArrayWithSize;
        }
    }

    public boolean loadTexture2DFromAssets(String str) {
        String str2 = str;
        try {
            AssetManager assetManager = this.mAssetMgr;
            InputStream open = assetManager.open("textures/" + str2);
            try {
                if (str2.contains("detail/")) {
                    Bitmap decodeStream = BitmapFactory.decodeStream(open);
                    Bitmap createBitmap = Bitmap.createBitmap(decodeStream.getWidth(), decodeStream.getHeight(), Bitmap.Config.ALPHA_8);
                    int width = decodeStream.getWidth();
                    int height = decodeStream.getHeight();
                    int i = width * height;
                    int[] iArr = new int[i];
                    int[] iArr2 = iArr;
                    decodeStream.getPixels(iArr, 0, width, 0, 0, width, height);
                    for (int i2 = 0; i2 < i; i2++) {
                        iArr2[i2] = (iArr2[i2] & 16711680) << 8;
                    }
                    Bitmap bitmap = createBitmap;
                    createBitmap.setPixels(iArr2, 0, width, 0, 0, width, height);
                    GLUtils.texImage2D(3553, 0, 6406, bitmap, 0);
                    decodeStream.recycle();
                    bitmap.recycle();
                    open.close();
                } else {
                    Bitmap decodeStream2 = BitmapFactory.decodeStream(open);
                    GLUtils.texImage2D(3553, 0, decodeStream2, 0);
                    decodeStream2.recycle();
                    open.close();
                }
            } catch (IOException unused) {
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
