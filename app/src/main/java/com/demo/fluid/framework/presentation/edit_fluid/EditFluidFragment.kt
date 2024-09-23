package com.demo.fluid.framework.presentation.edit_fluid

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.demo.fluid.R
import com.demo.fluid.databinding.FragmentEditFluidBinding
import com.demo.fluid.framework.presentation.common.BaseFragment
import com.demo.fluid.framework.presentation.model.TextViewData
import com.demo.fluid.util.BundleKey
import com.demo.fluid.util.Constant
import com.magicfluids.Config
import com.magicfluids.NativeInterface
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditFluidFragment : BaseFragment<FragmentEditFluidBinding, EditFluidViewModel>(
    FragmentEditFluidBinding::inflate,
    EditFluidViewModel::class.java
) {

    var config: Config? = null
    var nameWallpaper: String = "AbstractAdventure"
    var nativeInterface = NativeInterface()

    override fun init(view: View) {
        nameWallpaper = arguments?.getString(BundleKey.KEY_FLUID_NAME_EDIT) ?: "AbstractAdventure"
        initView()
        setUpSurfaceView()
        startHandAnim()
        onBackEvent()
        onScreenEvent()
        addTextEvent()
        setWallpaperEvent()
    }

    override fun subscribeObserver(view: View) {

    }

    fun applySettingsToLwp() {
        Config.LWPCurrent.copyValuesFrom(this.config)
    }

    override fun onResume() {
        super.onResume()
//        val data = TextViewData(
//            text = "Hello",
//            color = Color.parseColor("#9C60A0"),
//            typeFaceSource = R.font.poppins_bold,
//            positionX = 100.0f,
//            positionY = 200.0f,
//            textSize = 20f
//        )
//        val textView = TextView(requireContext())
//        textView.text = data.text
//        textView.setTextColor(data.color)
//        textView.typeface = ResourcesCompat.getFont(requireContext(), data.typeFaceSource)
//        textView.textSize = data.textSize
//        binding.flAddText.removeView(textView)
//        binding.flAddText.addView(textView)
        binding.flAddText.removeAllViews()
        binding.flAddText.post {
            for (item in Constant.textViewList) {
                val parent = item.parent as? ViewGroup
                parent?.removeView(item)
                binding.flAddText.addView(item)
            }
        }
        binding.surfaceView.onResume()
        nativeInterface.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        nativeInterface.onDestroy()
    }

}
