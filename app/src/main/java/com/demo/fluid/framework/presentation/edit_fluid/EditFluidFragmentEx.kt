package com.demo.fluid.framework.presentation.edit_fluid

import android.annotation.SuppressLint
import android.app.Instrumentation
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.opengl.GLSurfaceView
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.InputDevice
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentResultListener
import androidx.navigation.fragment.findNavController
import com.demo.fluid.R
import com.demo.fluid.framework.presentation.addTextFluid.AddTextFluidActivity
import com.demo.fluid.framework.presentation.onScreenFluid.OnScreenActivity
import com.demo.fluid.framework.presentation.wallpaper_service.NewWallpaperService
import com.demo.fluid.util.BundleKey
import com.demo.fluid.util.Common
import com.demo.fluid.util.displayToast
import com.demo.fluid.util.gl.GLES20Renderer
import com.demo.fluid.util.gl.InputBuffer
import com.demo.fluid.util.gl.OrientationSensor
import com.demo.fluid.util.gl.SettingsStorage
import com.magicfluids.Config
import com.demo.fluid.util.setPreventDoubleClickScaleView
import pion.tech.fluid_wallpaper.util.loadImage
import pion.tech.fluid_wallpaper.util.safeDelay
import java.io.File

fun EditFluidFragment.initView() {
    if (commonViewModel.currentAddedTextFilePath != null) {
        binding.ivAddedText.loadImage(File(commonViewModel.currentAddedTextFilePath!!))
    }
}

fun EditFluidFragment.onBackEvent() {
    binding.ivBack.setPreventDoubleClickScaleView {
        backEvent()
    }
    activity?.onBackPressedDispatcher?.addCallback(this, true) {
        backEvent()
    }
}

fun EditFluidFragment.backEvent() {
    findNavController().popBackStack(R.id.listFluidFragment, false)
}

fun EditFluidFragment.setUpSurfaceView() {
    config = Config.Current
    SettingsStorage.loadConfigFromInternalPreset(
        nameWallpaper,
        requireContext().assets,
        config
    )

    binding.surfaceView.preserveEGLContextOnPause = true
    nativeInterface.setAssetManager(requireContext().assets)
    val orientationSensor = OrientationSensor(requireContext(), requireActivity().application)
    binding.surfaceView.setEGLContextClientVersion(2)
    val renderer = GLES20Renderer(nativeInterface, orientationSensor)
    binding.surfaceView.setRenderer(renderer)
    renderer.setInitialScreenSize(300, 200)
    nativeInterface.onCreate(300, 200, false)
    nativeInterface.updateConfig(config)
//    safeDelay(3000) {
//        displayToast("Vuot")
//        simulateSwipe(xStart = 0.0f, yStart = 0.0f, xEnd = 248f, yEnd = 423f)
//    }
}


@SuppressLint("ClickableViewAccessibility")
fun EditFluidFragment.startHandAnim() {
//    if (!isShowedTutorial) {
//        binding.clTutorial.isVisible = true
//        binding.ivHand.post {
//            safeDelay(500) {
//                val slideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_left)
//                binding.ivHand.startAnimation(slideUp)
//                isShowedTutorial = true
//                safeDelay(3000) {
//                    binding.ivHand.clearAnimation()
//                    binding.clTutorial.isVisible = false
//                }
//            }
//        }
//    } else {
//        binding.clTutorial.isVisible = false
//    }
//
//
//    binding.clTutorial.setOnTouchListener { _, _ ->
//        binding.clTutorial.isVisible = false
//        true
//    }
}

fun EditFluidFragment.onScreenEvent() {
    binding.btnOnscreen.setPreventDoubleClickScaleView {
        val intent = Intent(requireContext(), OnScreenActivity::class.java)
        intent.putExtra(BundleKey.KEY_FLUID_NAME_ON_SCREEN, nameWallpaper)
        startActivity(intent)
    }
}

fun EditFluidFragment.addTextEvent() {
    binding.btnAddText.setPreventDoubleClickScaleView {
        val intent = Intent(requireContext(), AddTextFluidActivity::class.java)
        intent.putExtra(BundleKey.KEY_FLUID_NAME_ADD_TEXT, nameWallpaper)
        startActivity(intent)
    }
}

fun EditFluidFragment.setWallpaperEvent() {
    binding.btnSetWallpaper.setPreventDoubleClickScaleView {
        try {
            WallpaperManager.getInstance(requireContext()).clear()
            applySettingsToLwp()
            Common.INSTANCE.nameWallpaper = nameWallpaper
            Common.INSTANCE.setNameWallpaper(requireContext(), nameWallpaper)
            val componentName =
                ComponentName(requireContext().packageName, NewWallpaperService::class.java.name)
            val intent = Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER")
            intent.putExtra(
                "android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT",
                componentName
            )
            startActivity(intent)
        } catch (unused: Exception) {
            displayToast("Device not support")
        }
    }
}