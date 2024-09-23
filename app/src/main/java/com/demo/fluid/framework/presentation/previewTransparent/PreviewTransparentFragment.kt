package com.demo.fluid.framework.presentation.previewTransparent

import android.view.View
import com.demo.fluid.databinding.FragmentPreviewTransparentBinding
import com.demo.fluid.framework.presentation.common.BaseFragment
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviewTransparentFragment : BaseFragment<FragmentPreviewTransparentBinding, PreviewTransparentViewModel>(
    FragmentPreviewTransparentBinding::inflate,
    PreviewTransparentViewModel::class.java
) {

    var cameraView: CameraView? = null

    override fun init(view: View) {
        setUpCameraView()
        onBackEvent()
        applyEvent()
    }

    inner class Listener : CameraListener()

    override fun subscribeObserver(view: View) {

    }

}
