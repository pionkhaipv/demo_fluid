package com.demo.fluid.framework.presentation.onScreenFluid

import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.demo.fluid.util.gl.GLES20Renderer
import com.demo.fluid.util.gl.OrientationSensor
import com.demo.fluid.util.gl.SettingsStorage
import com.demo.fluid.util.setPreventDoubleClickScaleView
import com.demo.fluid.util.simulateSwipe
import com.magicfluids.Config
import com.magicfluids.NativeInterface
import kotlin.math.abs
import kotlin.random.Random

fun OnScreenActivity.initView() {
    NativeInterface.init()
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    setUpSurfaceView()
}

fun OnScreenActivity.setUpSurfaceView() {
    config = Config.Current
    SettingsStorage.loadConfigFromInternalPreset(
        nameWallpaper,
        assets,
        config
    )

    binding.surfaceView.preserveEGLContextOnPause = false
    nativeInterface.setAssetManager(assets)
    val orientationSensor = OrientationSensor(this, application)
    binding.surfaceView.setEGLContextClientVersion(2)
    val renderer = GLES20Renderer(nativeInterface, orientationSensor)
    binding.surfaceView.setRenderer(renderer)
    renderer.setInitialScreenSize(300, 200)
    nativeInterface.onCreate(300, 200, false)
    nativeInterface.updateConfig(config)
}

fun OnScreenActivity.onBackEvent() {
    binding.ivBack.setPreventDoubleClickScaleView {
        backEvent()
    }
}

fun OnScreenActivity.backEvent() {
    finish()
}

fun OnScreenActivity.randomSwipe() {
    randomHandler = Handler(Looper.getMainLooper())
    randomRunnable = object : Runnable {
        override fun run() {
            var startX: Float
            var startY: Float
            var endX: Float
            var endY: Float

            do {
                startX = Random.nextFloat() * 300 // Adjust the range as needed
                startY = Random.nextFloat() * 300 // Adjust the range as needed
                endX = Random.nextFloat() * 500 // Adjust the range as needed
                endY = Random.nextFloat() * 500 // Adjust the range as needed
            } while (abs(endX - startX) < 100 || abs(endY - startY) < 100) // Ensure points are far apart

            simulateSwipe(xStart = startX, yStart = startY, xEnd = endX, yEnd = endY)

            // Schedule the next swipe after 1 second
            randomHandler!!.postDelayed(this, 500)
        }
    }

// Start the first swipe
    randomHandler!!.post(randomRunnable as Runnable)
}
