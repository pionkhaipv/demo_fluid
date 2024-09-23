package com.demo.fluid.framework.presentation.home

import android.view.View
import com.demo.fluid.R
import com.demo.fluid.databinding.FragmentHomeBinding
import com.demo.fluid.framework.presentation.common.BaseFragment
import com.demo.fluid.framework.presentation.home.adapter.ModeWallpaperAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(
    FragmentHomeBinding::inflate,
    HomeViewModel::class.java
), ModeWallpaperAdapter.Listener {

    val adapter = ModeWallpaperAdapter()

    override fun init(view: View) {
        setupViewPager()
        onNextEvent()
        onPreviousEvent()
        onBackEvent()
        settingEvent()

    }

    override fun subscribeObserver(view: View) {

    }

    override fun onFluidWallpaperClick() {
        safeNav(R.id.homeFragment, R.id.action_homeFragment_to_listFluidFragment)
    }

    override fun onTransparentWallPaperClick() {
        safeNav(R.id.homeFragment, R.id.action_homeFragment_to_transparentWallpaperFragment)
    }

    override fun onLiveWallPaperClick() {
    }

}
