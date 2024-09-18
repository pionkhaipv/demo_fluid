package com.demo.fluid.framework.presentation.onScreenFluid

import android.os.Bundle
import androidx.activity.addCallback
import com.demo.fluid.R
import com.demo.fluid.util.BundleKey
import com.demo.fluid.util.gl.GLES20Renderer
import com.demo.fluid.util.gl.OrientationSensor
import com.demo.fluid.util.gl.SettingsStorage
import com.magicfluids.Config
import com.demo.fluid.util.setPreventDoubleClickScaleView

fun OnScreenFluidFragment.onBackEvent() {
    binding.ivBack.setPreventDoubleClickScaleView {
        backEvent()
    }
    activity?.onBackPressedDispatcher?.addCallback(this, true) {
        backEvent()
    }
}

fun OnScreenFluidFragment.backEvent() {
    val bundle = Bundle()
    bundle.putString(BundleKey.KEY_FLUID_NAME_EDIT, nameWallpaper)
    safeNav(R.id.onScreenFragment, R.id.action_onScreenFragment_to_editFluidFragment, bundle)
}


fun OnScreenFluidFragment.setUpSurfaceView() {
    config = Config.Current
    SettingsStorage.loadConfigFromInternalPreset(
        nameWallpaper,
        requireContext().assets,
        config
    )

    binding.surfaceView.preserveEGLContextOnPause = false
    nativeInterface.setAssetManager(requireContext().assets)
    val orientationSensor = OrientationSensor(requireContext(), requireActivity().application)
    binding.surfaceView.setEGLContextClientVersion(2)
    val renderer = GLES20Renderer(nativeInterface, orientationSensor)
    binding.surfaceView.setRenderer(renderer)
    renderer.setInitialScreenSize(300, 200)
    nativeInterface.onCreate(300, 200, false)
    nativeInterface.updateConfig(config)
}