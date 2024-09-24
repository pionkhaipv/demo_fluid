package com.demo.fluid.framework.presentation.previewFluid

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.demo.fluid.R
import com.demo.fluid.databinding.ActivityPreviewFluidBinding
import com.demo.fluid.util.addActionUp
import com.demo.fluid.util.gl.GLES20Renderer
import com.demo.fluid.util.gl.OrientationSensor
import com.magicfluids.Config
import com.magicfluids.NativeInterface

class PreviewFluidActivity : AppCompatActivity() {

    lateinit var binding: ActivityPreviewFluidBinding

    var config: Config? = null

    var nameWallpaper: String = "AbstractAdventure"
    var nativeInterface: NativeInterface? = null
    var orientationSensor: OrientationSensor? = null
    var renderer: GLES20Renderer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewFluidBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        onBackEvent()
    }

    override fun onResume() {
        super.onResume()
        setUpViewForListTextView()
        binding.surfaceView.onResume()
        nativeInterface?.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        nativeInterface?.onDestroy()
    }

}