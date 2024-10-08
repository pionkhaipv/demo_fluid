package com.demo.fluid.framework.presentation.addTextFluid

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.demo.fluid.R
import com.demo.fluid.customview.DraggableTouchListener
import com.demo.fluid.framework.presentation.addTextFluid.adapter.ColorAdapter
import com.demo.fluid.framework.presentation.addTextFluid.adapter.FontFamilyAdapter
import com.demo.fluid.framework.presentation.edit_fluid.adjustSurfaceViewSizeWithActionBar
import com.demo.fluid.util.Constant
import com.demo.fluid.util.gl.GLES20Renderer
import com.demo.fluid.util.gl.OrientationSensor
import com.demo.fluid.util.gl.SettingsStorage
import com.demo.fluid.util.setPreventDoubleClickScaleView
import com.magicfluids.Config
import com.magicfluids.NativeInterface
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import pion.tech.fluid_wallpaper.util.hideKeyboard


fun AddTextFluidActivity.setUpCustomTextView() {
    binding.sbFontSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            binding.edtChangeText.textSize = progress.toFloat()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            // No action needed
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            // No action needed
        }
    })

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

        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER
        }

        // Gán layout params cho TextView
        newTextView.layoutParams = layoutParams
        newTextView.y -= 20

        // Thêm TextView vào flContainer
        binding.flContainer.addView(newTextView)

    }
}

fun AddTextFluidActivity.onApplyEvent() {
    binding.tvApply.setPreventDoubleClickScaleView {
        binding.clEditText.isVisible = false
        binding.tvApply.isVisible = false
        binding.tvDone.isVisible = true
        binding.tvApply.hideKeyboard()
        binding.llAddText.isVisible = true
        binding.llEditText.isVisible = false

        currentEditTextView?.text = binding.edtChangeText.text
        currentEditTextView?.typeface = binding.edtChangeText.typeface
        currentEditTextView?.setTextColor(binding.edtChangeText.textColors)
        currentEditTextView?.textSize = binding.edtChangeText.textSize / 2
    }
}

fun AddTextFluidActivity.onDoneEvent() {
    binding.tvDone.setPreventDoubleClickScaleView {
        backEvent()

        Constant.textViewList.clear()
        for (i in 0 until binding.flContainer.childCount) {
            val view = binding.flContainer.getChildAt(i)
            if (view is TextView) {
                Constant.textViewList.add(view)
            }
        }
    }
}

fun AddTextFluidActivity.onEditTextEvent(){
    binding.btnEditText.setPreventDoubleClickScaleView {
        if(currentEditTextView != null){
            binding.tvApply.isVisible = true
            binding.tvDone.isVisible = false
            binding.clEditText.isVisible = true
            binding.sbFontSize.progress = (currentEditTextView!!.textSize / 2).toInt()

            binding.edtChangeText.setText(currentEditTextView!!.text)
            binding.edtChangeText.setTextColor(currentEditTextView!!.textColors)
            binding.edtChangeText.typeface = currentEditTextView!!.typeface
        }
    }
}

fun AddTextFluidActivity.onDeleteTextEvent(){
    binding.btnDelete.setPreventDoubleClickScaleView {
        if(currentEditTextView != null){
            binding.flContainer.removeView(currentEditTextView)
        }
    }
}

@SuppressLint("ClickableViewAccessibility")
fun AddTextFluidActivity.createNextTextView(): TextView {
    val newTextView = TextView(this).apply {
        text = "Text"
        textSize = 20f
        setTextColor(Color.WHITE)
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
    }

    newTextView.setOnTouchListener(
        DraggableTouchListener(onClick = {
            currentEditTextView = newTextView
            if (binding.llAddText.isVisible) {
                binding.llAddText.isVisible = false
                binding.llEditText.isVisible = true
            } else {
                binding.llAddText.isVisible = true
                binding.llEditText.isVisible = false
            }
        }
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
            binding.edtChangeText.setTextColor(Color.parseColor(item))
        }
    })

    fontFamilyAdapter.submitList(this.getTypeFaceMap().keys.toList())
    binding.rvFontFamily.adapter = fontFamilyAdapter
    fontFamilyAdapter.setListener(object : FontFamilyAdapter.Listener {
        override fun onItemClick(item: Int) {
            val typeface = ResourcesCompat.getFont(binding.root.context, item)
            binding.edtChangeText.typeface = typeface
        }
    })
}

fun AddTextFluidActivity.initView() {
    NativeInterface.init()
    setEventListener(
        this,
        this,
        KeyboardVisibilityEventListener { isShowKeyboard ->
            binding.clInfoText.isVisible = !isShowKeyboard
        })

    binding.surfaceView.post {
        adjustSurfaceViewSizeWithActionBar(activity = this, view = binding.cvMain)
    }

    binding.flContainer.removeAllViews()
    binding.flContainer.post {
        for (item in Constant.textViewList) {
            val textView = createNextTextView()
            textView.text = item.text
            textView.x = item.x
            textView.y = item.y
            textView.setTextColor(item.currentTextColor)
            textView.typeface = item.typeface
            textView.textSize = item.textSize / 2
            val parent = textView.parent as? ViewGroup
            parent?.removeView(textView)
            binding.flContainer.addView(textView)
        }
    }
}

fun Context.getTypeFaceMap(): Map<Int, Typeface?> {
    val typefaceMap = mapOf(
        R.font.font_digital_number to ResourcesCompat.getFont(this, R.font.font_digital_number),
        pion.tech.commonres.R.font.font_100 to ResourcesCompat.getFont(
            this,
            pion.tech.commonres.R.font.font_100
        ),
        pion.tech.commonres.R.font.font_200 to ResourcesCompat.getFont(
            this,
            pion.tech.commonres.R.font.font_200
        )
    )
    return typefaceMap
}