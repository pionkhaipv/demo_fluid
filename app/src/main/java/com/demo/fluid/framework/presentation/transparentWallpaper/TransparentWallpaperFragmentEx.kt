package com.demo.fluid.framework.presentation.transparentWallpaper

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.demo.fluid.R
import com.demo.fluid.framework.presentation.transparentWallpaper.dialog.UnlockTransparentDialog
import com.demo.fluid.framework.presentation.wallpaper_service.DecompiledCameraWallpaper
import com.demo.fluid.util.displayToast
import com.demo.fluid.util.setPreventDoubleClickScaleView
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.controls.Audio
import com.otaliastudios.cameraview.controls.Engine
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.controls.Preview
import com.otaliastudios.cameraview.markers.DefaultAutoFocusMarker
import okio.Buffer
import java.io.IOException

fun TransparentWallpaperFragment.onBackEvent() {
    binding.ivBack.setPreventDoubleClickScaleView {
        findNavController().navigateUp()
    }
}

fun TransparentWallpaperFragment.initView() {

    binding.sbWallpaper.isChecked = isWallpaperSet()

    binding.sbWallpaper.setOnClickListener {
        val isChecked = binding.sbWallpaper.isChecked
        if (!isChecked) {
            try {
                WallpaperManager.getInstance(context).clear()
                displayToast(getString(R.string.success))
            } catch (e: IOException) {
                displayToast(getString(R.string.something_error))
                e.printStackTrace()
            }
        } else {
            binding.sbWallpaper.isChecked = false
            if (!isAlreadyUnlock) {
                val dialog = UnlockTransparentDialog()
                dialog.setListener(listener = object : UnlockTransparentDialog.Listener {
                    override fun onWatchAdsClick() {
                        isAlreadyUnlock = true
                        safeNav(
                            R.id.transparentWallpaperFragment,
                            R.id.action_transparentWallpaperFragment_to_previewTransparentFragment
                        )
                    }

                    override fun onBuyVipVersion() {
                    }
                })
                dialog.show(childFragmentManager)
            } else {
                safeNav(
                    R.id.transparentWallpaperFragment,
                    R.id.action_transparentWallpaperFragment_to_previewTransparentFragment
                )
            }
        }

        binding.viewBlur.isVisible = !binding.sbWallpaper.isChecked
    }
    binding.viewBlur.isVisible = !binding.sbWallpaper.isChecked

}

fun TransparentWallpaperFragment.isWallpaperSet(): Boolean {
    val wallpaperManager = WallpaperManager.getInstance(requireContext())
    val wallpaperInfo = wallpaperManager.wallpaperInfo
    return wallpaperInfo?.packageName == requireContext().packageName &&
            wallpaperInfo?.serviceName == DecompiledCameraWallpaper::class.java.name
}

//Viet ham kiem tra service running hay khong
fun TransparentWallpaperFragment.setUpCameraView() {
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
