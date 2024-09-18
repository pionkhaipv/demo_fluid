package com.demo.fluid.framework.presentation.transparentWallpaper

import android.view.View
import com.demo.fluid.databinding.FragmentTransparentWallpaperBinding
import com.demo.fluid.framework.presentation.common.BaseFragment
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransparentWallpaperFragment : BaseFragment<FragmentTransparentWallpaperBinding, TransparentWallpaperViewModel>(
        FragmentTransparentWallpaperBinding::inflate,
        TransparentWallpaperViewModel::class.java
    ) {

    var cameraView: CameraView? = null

    var isAlreadyUnlock = false

    override fun init(view: View) {
        onBackEvent()
        setUpCameraView()
    }


    inner class Listener : CameraListener()

    override fun subscribeObserver(view: View) {

    }

    override fun onResume() {
        super.onResume()
        initView()
    }

}
