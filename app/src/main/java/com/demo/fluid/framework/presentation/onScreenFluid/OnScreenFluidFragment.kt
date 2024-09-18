package com.demo.fluid.framework.presentation.onScreenFluid

import android.view.View
import android.view.WindowManager
import com.demo.fluid.databinding.FragmentOnScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import com.demo.fluid.framework.presentation.common.BaseFragment
import com.demo.fluid.util.BundleKey
import com.magicfluids.Config
import com.magicfluids.NativeInterface

@AndroidEntryPoint
class OnScreenFluidFragment : BaseFragment<FragmentOnScreenBinding, OnScreenFluidViewModel>(
    FragmentOnScreenBinding::inflate,
    OnScreenFluidViewModel::class.java
) {

    var config: Config? = null
    var nameWallpaper: String = "AbstractAdventure"
    var nativeInterface = NativeInterface()

    override fun init(view: View) {
        nameWallpaper =
            arguments?.getString(BundleKey.KEY_FLUID_NAME_ON_SCREEN) ?: "AbstractAdventure"
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setUpSurfaceView()
        onBackEvent()
    }

    override fun subscribeObserver(view: View) {

    }

    override fun onResume() {
        super.onResume()
        binding.surfaceView.onResume()
        nativeInterface.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        nativeInterface.onDestroy()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

}
