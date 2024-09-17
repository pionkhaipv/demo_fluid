package com.demo.fluid.activity.main;

import android.app.ActivityManager;
import android.app.Application;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.demo.fluid.service.NewWallpaperService;
import com.demo.fluid.util.Common;
import com.demo.fluid.R;
import com.demo.fluid.databinding.ActivityMainBinding;
import com.jakewharton.rxbinding4.view.RxView;
import com.magicfluids.Config;
import com.magicfluids.NativeInterface;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.functions.Consumer;
import kotlin.Unit;

import com.demo.fluid.util.gl.GLES20Renderer;
import com.demo.fluid.util.gl.OrientationSensor;
import com.demo.fluid.util.gl.SettingsStorage;

public final class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private Config config;
    private Handler handler = new Handler(Looper.getMainLooper());

    public String nameWallpaper = "AbstractAdventure";
    private NativeInterface nativeInterface;
    private OrientationSensor orientationSensor;
    private GLES20Renderer renderer;

    public final ActivityResultLauncher<Intent> startForResult;

    public MainActivity() {
        Config config2 = Config.Current;
        this.config = config2;
        ActivityResultLauncher<Intent> registerForActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult activityResult) {
                if (activityResult.getResultCode() == -1) {
                    Intent intent = new Intent(MainActivity.this, ThemeApplyActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        this.startForResult = registerForActivityResult;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ViewDataBinding contentView = DataBindingUtil.setContentView(this, R.layout.activity_main);
        this.binding = (ActivityMainBinding) contentView;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorlight));
        }


        //Reguler Banner Ads

        String stringExtra = getIntent().getStringExtra("nameWallpaper");
        if (stringExtra == null) {
            stringExtra = "AbstractAdventure";
        }
        this.nameWallpaper = stringExtra;
        NativeInterface.init();
        loadConfigPreset();
        initSettingController();
        clickListener();
    }

    private void clickListener() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView textView = binding.btnApply;
        RxView.clicks(textView).throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe(new Consumer<Unit>() {
            @Override
            public void accept(Unit unit) throws Throwable {
                try {
                    WallpaperManager.getInstance(MainActivity.this).clear();
                    applySettingsToLwp();
                    Common.INSTANCE.setNameWallpaper(nameWallpaper);
                    Common.INSTANCE.setNameWallpaper(MainActivity.this, nameWallpaper);
                    ComponentName componentName = new ComponentName(getPackageName(), NewWallpaperService.class.getName());
                    Intent intent = new Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER");
                    intent.putExtra("android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT", componentName);
                    startForResult.launch(intent);
                } catch (Exception unused) {
                    Toast.makeText(MainActivity.this, "This device not supported", 0).show();
                }
            }
        });


    }

    private void initSettingController() {
        binding.surfaceView.setPreserveEGLContextOnPause(wantToPreserveEGLContext());
        NativeInterface nativeInterface2 = new NativeInterface();
        this.nativeInterface = nativeInterface2;
        nativeInterface2.setAssetManager(getAssets());
        Application application = getApplication();
        this.orientationSensor = new OrientationSensor(this, application);
        ActivityMainBinding activityMainBinding3 = this.binding;
        if (activityMainBinding3 == null) {
            activityMainBinding3 = null;
        }
        activityMainBinding3.surfaceView.setEGLContextClientVersion(2);
        NativeInterface nativeInterface3 = this.nativeInterface;
        OrientationSensor orientationSensor2 = this.orientationSensor;
        this.renderer = new GLES20Renderer(nativeInterface3, orientationSensor2);
        binding.surfaceView.setRenderer(this.renderer);
        GLES20Renderer gLES20Renderer = this.renderer;
        gLES20Renderer.setInitialScreenSize(300, 200);
        NativeInterface nativeInterface4 = this.nativeInterface;
        nativeInterface4.onCreate(300, 200, false);
        NativeInterface nativeInterface5 = this.nativeInterface;
        nativeInterface5.updateConfig(this.config);
    }

    private void loadConfigPreset() {
        SettingsStorage.loadConfigFromInternalPreset(this.nameWallpaper, getAssets(), this.config);
    }


    public void applySettingsToLwp() {
        Config.LWPCurrent.copyValuesFrom(this.config);
    }

    private boolean wantToPreserveEGLContext() {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        Object systemService = getSystemService("activity");
        ((ActivityManager) systemService).getMemoryInfo(memoryInfo);
        return memoryInfo.totalMem / ((long) 1048576) > 3000;
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.surfaceView.onResume();
        NativeInterface nativeInterface2 = this.nativeInterface;
        if (nativeInterface2 != null) {
            nativeInterface2.onResume();
        }
    }

    @Override
    public void onDestroy() {
        try {
            this.handler.removeMessages(0);
            NativeInterface nativeInterface2 = this.nativeInterface;
            if (nativeInterface2 != null) {
                nativeInterface2.onDestroy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
