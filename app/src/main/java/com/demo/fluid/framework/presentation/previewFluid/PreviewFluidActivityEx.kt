package com.demo.fluid.framework.presentation.previewFluid

import android.app.Application
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.demo.fluid.framework.presentation.edit_fluid.EditFluidFragment
import com.demo.fluid.framework.presentation.edit_fluid.adjustSurfaceViewSizeWithActionBar
import com.demo.fluid.framework.presentation.wallpaper_service.NewWallpaperService
import com.demo.fluid.util.BundleKey
import com.demo.fluid.util.Common
import com.demo.fluid.util.Constant
import com.demo.fluid.util.getBitmapFromView
import com.demo.fluid.util.gl.GLES20Renderer
import com.demo.fluid.util.gl.OrientationSensor
import com.demo.fluid.util.gl.SettingsStorage
import com.demo.fluid.util.saveBitmapToFile
import com.demo.fluid.util.setPreventDoubleClickScaleView
import com.magicfluids.Config
import com.magicfluids.NativeInterface

fun PreviewFluidActivity.initView() {
    NativeInterface.init()
    nameWallpaper = intent.getStringExtra(BundleKey.KEY_FLUID_NAME_PREVIEW) ?: "AbstractAdventure"
    config = Config.Current
    loadConfigPreset()
    initSettingController()

    binding.btnApply.setPreventDoubleClickScaleView {
        try {
            val bitmap = binding.flContainer.getBitmapFromView()
            val filePath = saveBitmapToFile(bitmap, System.currentTimeMillis().toString())

            WallpaperManager.getInstance(this).clear()
            applySettingsToLwp()
            Common.INSTANCE.nameWallpaper = nameWallpaper
            Common.INSTANCE.filePathTextView = filePath

            val componentName =
                ComponentName(packageName, NewWallpaperService::class.java.name)
            val intent = Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER")
            intent.putExtra(
                "android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT",
                componentName
            )
            startActivity(intent)
        } catch (unused: Exception) {
            Toast.makeText(this, "Device not support", Toast.LENGTH_SHORT).show()
        }
    }

    binding.cvMain.post {
        adjustSurfaceViewSizeWithActionBar(activity = this, view = binding.cvMain)
    }
}

fun PreviewFluidActivity.setUpViewForListTextView() {
    // Giả sử bạn có một layout nhỏ trước đó và cần tính toán dựa trên tỷ lệ
    val oldWidth = Constant.smallFrameWidth
    val oldHeight = Constant.smallFrameHeight

    binding.flContainer.removeAllViews()
    binding.flContainer.post {
        // Kích thước của layout lớn hiện tại
        val newWidth = binding.flContainer.width
        val newHeight = binding.flContainer.height

        // Tính tỷ lệ thay đổi giữa layout nhỏ và layout lớn
        val scaleX = newWidth / oldWidth.toFloat()
        val scaleY = newHeight / oldHeight.toFloat()

        for (item in Constant.textViewList) {
            val textView = TextView(this)
            textView.text = item.text

            // Tính lại vị trí x và y dựa trên tỷ lệ thay đổi
            textView.x = item.x * scaleX
            textView.y = item.y * scaleY

            textView.setTextColor(item.currentTextColor)
            textView.typeface = item.typeface
            textView.textSize = item.textSize / 2

            // Loại bỏ view khỏi parent nếu có
            val parent = textView.parent as? ViewGroup
            parent?.removeView(textView)

            // Thêm TextView vào layout lớn
            binding.flContainer.addView(textView)
        }
    }
}


fun PreviewFluidActivity.onBackEvent() {
    binding.ivBack.setPreventDoubleClickScaleView {
        finish()
    }
}

fun PreviewFluidActivity.loadConfigPreset() {
    SettingsStorage.loadConfigFromInternalPreset(
        this.nameWallpaper,
        assets,
        config
    )
}

fun PreviewFluidActivity.initSettingController() {
    binding.surfaceView.preserveEGLContextOnPause = false
    nativeInterface = NativeInterface()
    nativeInterface!!.setAssetManager(assets)
    val application: Application = application
    this.orientationSensor = OrientationSensor(this, application)
    binding.surfaceView.setEGLContextClientVersion(2)
    val nativeInterface3 = this.nativeInterface
    val orientationSensor2 = this.orientationSensor
    this.renderer = GLES20Renderer(nativeInterface3, orientationSensor2)
    binding.surfaceView.setRenderer(this.renderer)
    val gLES20Renderer = this.renderer
    gLES20Renderer!!.setInitialScreenSize(300, 200)
    val nativeInterface4 = this.nativeInterface
    nativeInterface4!!.onCreate(300, 200, false)
    val nativeInterface5 = this.nativeInterface
    nativeInterface5!!.updateConfig(this.config)
}

fun PreviewFluidActivity.applySettingsToLwp() {
    Config.LWPCurrent.copyValuesFrom(this.config)
}