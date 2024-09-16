package com.demo.fluid.framework.presentation.preview_fluid

import android.os.Handler
import android.os.Looper
import android.view.View
import com.demo.fluid.databinding.FragmentPreviewFluidBinding
import com.demo.fluid.gl.GLES20Renderer
import com.demo.fluid.gl.OrientationSensor
import com.magicfluids.Config
import com.magicfluids.NativeInterface
import dagger.hilt.android.AndroidEntryPoint
import com.demo.fluid.framework.presentation.common.BaseFragment


@AndroidEntryPoint
class PreviewFluidFragment : BaseFragment<FragmentPreviewFluidBinding, PreviewFluidViewModel>(
    FragmentPreviewFluidBinding::inflate,
    PreviewFluidViewModel::class.java
) {

    private val config: Config? = null
    private val handler = Handler(Looper.getMainLooper())

    var nameWallpaper: String = "AbstractAdventure"
    private val nativeInterface: NativeInterface? = null
    private val orientationSensor: OrientationSensor? = null
    private val renderer: GLES20Renderer? = null
    override fun init(view: View) {
        NativeInterface.init()

    }

    override fun subscribeObserver(view: View) {

    }

}
