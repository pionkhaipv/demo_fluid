package com.demo.fluid.framework.presentation.edit_fluid

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.demo.fluid.R
import com.demo.fluid.framework.presentation.wallpaper_service.NewWallpaperService
import com.demo.fluid.util.BundleKey
import com.demo.fluid.util.Common
import com.demo.fluid.util.displayToast
import com.demo.fluid.util.gl.GLES20Renderer
import com.demo.fluid.util.gl.OrientationSensor
import com.demo.fluid.util.gl.SettingsStorage
import com.magicfluids.Config
import com.demo.fluid.util.setPreventDoubleClickScaleView

fun EditFluidFragment.onBackEvent() {
    binding.ivBack.setPreventDoubleClickScaleView {
        backEvent()
    }
    activity?.onBackPressedDispatcher?.addCallback(this, true) {
        backEvent()
    }
}

fun EditFluidFragment.backEvent() {
    findNavController().popBackStack(R.id.listFluidFragment,false)
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
        val bundle = Bundle()
        bundle.putString(BundleKey.KEY_FLUID_NAME_ON_SCREEN, nameWallpaper)
        safeNav(R.id.editFluidFragment, R.id.action_editFluidFragment_to_onScreenFragment, bundle)
    }
}

fun EditFluidFragment.addTextEvent() {
    binding.btnAddText.setPreventDoubleClickScaleView {
        val bundle = Bundle()
        bundle.putString(BundleKey.KEY_FLUID_NAME_ADD_TEXT, nameWallpaper)
        safeNav(R.id.editFluidFragment, R.id.action_editFluidFragment_to_addTextFluidFragment, bundle)
    }
}

fun EditFluidFragment.setWallpaperEvent(){
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