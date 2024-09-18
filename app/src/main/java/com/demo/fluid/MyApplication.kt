package com.demo.fluid

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.demo.fluid.framework.MainActivity
import com.magicfluids.NativeInterface
import com.zxy.recovery.core.Recovery
import dagger.hilt.android.HiltAndroidApp
import pion.tech.fluid_wallpaper.framework.presentation.common.lifecycleCallback.ActivityLifecycleCallbacksImpl
import pion.tech.fluid_wallpaper.util.PrefUtil
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {

    @Inject
    lateinit var prefUtil : PrefUtil

    override fun onCreate() {
        super.onCreate()
        //TODO: enable/disable dark theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        NativeInterface.init()
        if (BuildConfig.DEBUG) {
            Recovery.getInstance()
                .debug(true)
                .recoverInBackground(false)
                .recoverStack(true)
                .mainPage(MainActivity::class.java)
                .recoverEnabled(true)
                .silent(false, Recovery.SilentMode.RECOVER_ACTIVITY_STACK)
                .init(this)

            Timber.plant(Timber.DebugTree())
        }
        registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksImpl())
    }

}
