package com.demo.fluid.framework.presentation.previewTransparent

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.FrameLayout
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.demo.fluid.framework.presentation.wallpaper_service.DecompiledCameraWallpaper
import com.demo.fluid.util.setPreventDoubleClickScaleView
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.controls.Audio
import com.otaliastudios.cameraview.controls.Engine
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.controls.Preview
import com.otaliastudios.cameraview.markers.DefaultAutoFocusMarker
import java.io.IOException

fun PreviewTransparentFragment.setUpCameraView() {
    cameraView = CameraView(requireContext())
    binding.cameraContainer.post {
        val layoutParam = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )

        cameraView?.apply {
            keepScreenOn = true
            audio = Audio.OFF
            setAutoFocusMarker(DefaultAutoFocusMarker())
            engine = Engine.CAMERA2
            setExperimental(true)
            facing = Facing.BACK
            flash = Flash.OFF
            preview = Preview.GL_SURFACE
            layoutParams = layoutParam
        }
        binding.cameraContainer.addView(cameraView)
        cameraView?.setLifecycleOwner(viewLifecycleOwner)
        cameraView?.addCameraListener(Listener())
    }
}

fun PreviewTransparentFragment.onBackEvent() {
    binding.ivBack.setPreventDoubleClickScaleView {
        backEvent()
    }
    activity?.onBackPressedDispatcher?.addCallback(this, true) {
        backEvent()
    }
}

fun PreviewTransparentFragment.backEvent() {
    findNavController().navigateUp()
}

fun PreviewTransparentFragment.applyEvent() {
    binding.tvApply.setPreventDoubleClickScaleView {
        setCameraToWallPaper(requireContext())
    }
}

fun setCameraToWallPaper(context: Context) {
    Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
        putExtra(
            WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
            ComponentName(context, DecompiledCameraWallpaper::class.java)
        )
    }.also {
        context.startActivity(it)
    }
    try {
        WallpaperManager.getInstance(context).clear()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}
