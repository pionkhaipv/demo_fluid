package com.demo.fluid.framework.presentation.addTextFluid

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.demo.fluid.R
import com.demo.fluid.customview.DraggableTouchListener
import com.demo.fluid.util.BundleKey
import com.demo.fluid.util.getBitmapFromView
import com.demo.fluid.util.gl.GLES20Renderer
import com.demo.fluid.util.gl.OrientationSensor
import com.demo.fluid.util.gl.SettingsStorage
import com.demo.fluid.util.saveBitmapToFile
import com.magicfluids.Config
import com.demo.fluid.util.setPreventDoubleClickScaleView
import pion.tech.fluid_wallpaper.util.hideKeyboard

fun AddTextFluidFragment.setUpCustomTextView() {
    binding.sbFontSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            currentEditTextView?.textSize = progress.toFloat()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            // No action needed
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            // No action needed
        }
    })

    binding.edtChangeText.doOnTextChanged { text, start, before, count ->
        currentEditTextView?.text = text
    }
}

fun AddTextFluidFragment.setUpSurfaceView() {
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

fun AddTextFluidFragment.onBackEvent() {
    binding.ivBack.setPreventDoubleClickScaleView {
        backEvent()
    }
    activity?.onBackPressedDispatcher?.addCallback(this, true) {
        backEvent()
    }
}

fun AddTextFluidFragment.backEvent() {
    val bundle = Bundle()
    bundle.putString(BundleKey.KEY_FLUID_NAME_EDIT, nameWallpaper)
    safeNav(
        R.id.addTextFluidFragment,
        R.id.action_addTextFluidFragment_to_editFluidFragment,
        bundle
    )
}

fun AddTextFluidFragment.addTextEvent() {
    binding.llAddText.setPreventDoubleClickScaleView {
        val newTextView = createNextTextView()
        binding.flContainer.addView(newTextView)
    }
}

fun AddTextFluidFragment.onApplyEvent() {
    binding.tvApply.setPreventDoubleClickScaleView {
        binding.clEditText.isVisible = false
        binding.tvApply.isVisible = false
        binding.tvDone.isVisible = true
        binding.tvApply.hideKeyboard()
    }
}

fun AddTextFluidFragment.onDoneEvent(){
    binding.tvDone.setPreventDoubleClickScaleView {
        val bitmap = binding.flContainer.getBitmapFromView()
        val filePath = requireContext().saveBitmapToFile(bitmap)
        Log.d("asgagwagwgagwa", "onDoneEvent: $filePath")
    }
}

@SuppressLint("ClickableViewAccessibility")
fun AddTextFluidFragment.createNextTextView(): TextView {
    val newTextView = TextView(requireContext()).apply {
        text = "Draggable Text"
        textSize = 20f
        setTextColor(Color.WHITE)
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
    }

    newTextView.setOnTouchListener(
        DraggableTouchListener(
            context = requireContext(),
            removeArea = binding.flAddText,
            container = binding.flContainer,
            onDoubleClick = {
                binding.tvApply.isVisible = true
                binding.tvDone.isVisible = false
                binding.clEditText.isVisible = true
                currentEditTextView = newTextView
                binding.sbFontSize.progress = (currentEditTextView!!.textSize / 2).toInt()

                binding.edtChangeText.setText(currentEditTextView!!.text)
            }
        )
    )

    return newTextView
}