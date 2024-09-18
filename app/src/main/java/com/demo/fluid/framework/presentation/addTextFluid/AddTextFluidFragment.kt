package com.demo.fluid.framework.presentation.addTextFluid

import android.view.View
import android.widget.TextView
import com.demo.fluid.databinding.FragmentAddTextFluidBinding
import com.demo.fluid.databinding.FragmentSplashBinding
import com.demo.fluid.framework.presentation.addTextFluid.adapter.ColorAdapter
import com.demo.fluid.framework.presentation.addTextFluid.adapter.FontFamilyAdapter
import dagger.hilt.android.AndroidEntryPoint
import com.demo.fluid.framework.presentation.common.BaseFragment
import com.demo.fluid.util.BundleKey
import com.magicfluids.Config
import com.magicfluids.NativeInterface

@AndroidEntryPoint
class AddTextFluidFragment : BaseFragment<FragmentAddTextFluidBinding, AddTextFluidViewModel>(
    FragmentAddTextFluidBinding::inflate,
    AddTextFluidViewModel::class.java
) {

    var config: Config? = null
    var nameWallpaper: String = "AbstractAdventure"
    var nativeInterface = NativeInterface()


    var currentEditTextView:TextView? = null

    val colorAdapter = ColorAdapter()
    val fontFamilyAdapter = FontFamilyAdapter()
    override fun init(view: View) {
        nameWallpaper =
            arguments?.getString(BundleKey.KEY_FLUID_NAME_ADD_TEXT) ?: "AbstractAdventure"
        initView()
        setUpSurfaceView()
        onBackEvent()
        addTextEvent()
        setUpCustomTextView()
        onApplyEvent()
        onDoneEvent()
        setUpAdapter()
    }

    override fun subscribeObserver(view: View) {

    }

    override fun onResume() {
        super.onResume()
        binding.surfaceView.onResume()
        nativeInterface.onResume()
    }

}
