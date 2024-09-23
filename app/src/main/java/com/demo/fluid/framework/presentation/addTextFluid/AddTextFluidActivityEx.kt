package com.demo.fluid.framework.presentation.addTextFluid

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.demo.fluid.R
import com.demo.fluid.customview.DraggableTouchListener
import com.demo.fluid.framework.presentation.addTextFluid.adapter.ColorAdapter
import com.demo.fluid.framework.presentation.addTextFluid.adapter.FontFamilyAdapter
import com.demo.fluid.util.getBitmapFromView
import com.demo.fluid.util.gl.GLES20Renderer
import com.demo.fluid.util.gl.OrientationSensor
import com.demo.fluid.util.gl.SettingsStorage
import com.demo.fluid.util.saveBitmapToFile
import com.demo.fluid.util.setPreventDoubleClickScaleView
import com.magicfluids.Config
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import pion.tech.fluid_wallpaper.util.hideKeyboard


fun AddTextFluidActivity.setUpCustomTextView() {
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

fun AddTextFluidActivity.setUpSurfaceView() {
    config = Config.Current
    SettingsStorage.loadConfigFromInternalPreset(
        nameWallpaper,
        assets,
        config
    )

    binding.surfaceView.preserveEGLContextOnPause = true
    nativeInterface.setAssetManager(assets)
    val orientationSensor = OrientationSensor(this, this.application)
    binding.surfaceView.setEGLContextClientVersion(2)
    val renderer = GLES20Renderer(nativeInterface, orientationSensor)
    binding.surfaceView.setRenderer(renderer)
    renderer.setInitialScreenSize(300, 200)
    nativeInterface.onCreate(300, 200, false)
    nativeInterface.updateConfig(config)
}

fun AddTextFluidActivity.onBackEvent() {
    binding.ivBack.setPreventDoubleClickScaleView {
        backEvent()
    }
}

fun AddTextFluidActivity.backEvent() {
    finish()
}

fun AddTextFluidActivity.addTextEvent() {
    binding.llAddText.setPreventDoubleClickScaleView {
        val newTextView = createNextTextView()
        binding.flContainer.addView(newTextView)
    }
}

fun AddTextFluidActivity.onApplyEvent() {
    binding.tvApply.setPreventDoubleClickScaleView {
        binding.clEditText.isVisible = false
        binding.tvApply.isVisible = false
        binding.tvDone.isVisible = true
        binding.tvApply.hideKeyboard()
    }
}

fun AddTextFluidActivity.onDoneEvent() {
    binding.tvDone.setPreventDoubleClickScaleView {
        val bitmap = binding.flContainer.getBitmapFromView()
        val filePath =
            saveBitmapToFile(bitmap, System.currentTimeMillis().toString())
//        commonViewModel.currentAddedTextFilePath = filePath
        backEvent()
        Log.d("asgagwagwgagwa", "onDoneEvent: $filePath")
    }
}

@SuppressLint("ClickableViewAccessibility")
fun AddTextFluidActivity.createNextTextView(): TextView {
    val newTextView = TextView(this).apply {
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
            context = this,
            removeArea = binding.flAddText,
            container = binding.flContainer,
            onDoubleClick = {
                binding.tvApply.isVisible = true
                binding.tvDone.isVisible = false
                binding.clEditText.isVisible = true
                currentEditTextView = newTextView
                binding.sbFontSize.progress = (currentEditTextView!!.textSize / 2).toInt()

                binding.edtChangeText.setText(currentEditTextView!!.text)
            },
            onDragStart = {
                binding.llDeleteText.isVisible = true
                binding.llAddText.isVisible = false
            },
            onDragEnd = {
                binding.llDeleteText.isVisible = false
                binding.llAddText.isVisible = true
            },

            )
    )

    return newTextView
}

fun AddTextFluidActivity.setUpAdapter() {
    val listColor = listOf(
        "#DC3535",
        "#D9DC35",
        "#99DC35",
        "#35DCC3",
        "#3594DC",
        "#8B35DC",
        "#FFFFFF",
        "#000000"
    )
    colorAdapter.submitList(listColor)
    binding.rvColor.adapter = colorAdapter
    colorAdapter.setListener(object : ColorAdapter.Listener {
        override fun onItemClick(item: String) {
            currentEditTextView?.setTextColor(Color.parseColor(item))
        }
    })

    val listFontFamily = listOf(
        R.font.font_digital_number, pion.tech.commonres.R.font.font_100,
        pion.tech.commonres.R.font.font_200
    )
    fontFamilyAdapter.submitList(listFontFamily)
    binding.rvFontFamily.adapter = fontFamilyAdapter
    fontFamilyAdapter.setListener(object : FontFamilyAdapter.Listener {
        override fun onItemClick(item: Int) {
            val typeface = ResourcesCompat.getFont(binding.root.context, item)
            currentEditTextView?.typeface = typeface
        }
    })
}

fun AddTextFluidActivity.initView() {
    setEventListener(
        this,
        this,
        KeyboardVisibilityEventListener { isShowKeyboard ->
            binding.clInfoText.isVisible = !isShowKeyboard
        })
}