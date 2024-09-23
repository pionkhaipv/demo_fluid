package com.demo.fluid.framework.presentation.edit_fluid

import android.view.View
import com.demo.fluid.databinding.FragmentEditFluidBinding
import com.demo.fluid.framework.presentation.common.BaseFragment
import com.demo.fluid.util.BundleKey
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
        binding.surfaceView.onResume()
        nativeInterface.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        nativeInterface.onDestroy()
    }

}
