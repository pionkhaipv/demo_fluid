package com.demo.fluid.framework.presentation.home

import android.view.View
import androidx.activity.addCallback
import androidx.viewpager2.widget.ViewPager2
import com.demo.fluid.R
import com.demo.fluid.framework.MainActivity
import com.demo.fluid.framework.presentation.home.adapter.HorizontalMarginItemDecoration
import com.demo.fluid.util.setPreventDoubleClick
import kotlin.math.abs

fun HomeFragment.onBackEvent() {
    activity?.onBackPressedDispatcher?.addCallback(this, true) {
        backEvent()
    }
}

fun HomeFragment.backEvent() {
    //Show exit dialog
}

fun HomeFragment.setupViewPager(){
    adapter.setMainActivity(requireActivity() as MainActivity)
    adapter.setListener(this)
    binding.vpMain.adapter = adapter

    binding.vpMain.offscreenPageLimit = 1

    val nextItemVisiblePx = resources.getDimension(htkien.autodimens.R.dimen._50dp)

    val currentItemHorizontalMarginPx =
        resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)

    val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx

    val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
        page.translationX = -pageTranslationX * position
        page.scaleY = 1 - (0.23f * abs(position))
    }

    binding.vpMain.setPageTransformer(pageTransformer)

    val itemDecoration = HorizontalMarginItemDecoration(
        requireContext(),
        htkien.autodimens.R.dimen._50dp
    )

    binding.vpMain.addItemDecoration(itemDecoration)

    binding.vpMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {
            if (adapter.currentPosition != -1) {
                adapter.notifyItemChanged(adapter.currentPosition)
            }
            when (position) {
                0 -> {
                    adapter.currentPosition = 0
                    adapter.notifyItemChanged(adapter.currentPosition)
                    binding.tvModeTitle.setText(R.string.fluid_wallpaper)
                }

                else -> {
                    adapter.currentPosition = 1
                    adapter.notifyItemChanged(adapter.currentPosition)
                    binding.tvModeTitle.text = getString(R.string.transparent_wallpaper)

                }

            }
        }
    })
}

fun HomeFragment.onNextEvent(){
    binding.ivNextPage.setPreventDoubleClick {
        binding.vpMain.currentItem += 1
    }
}


fun HomeFragment.onPreviousEvent(){
    binding.ivPreviousPage.setPreventDoubleClick {
        binding.vpMain.currentItem -= 1
    }
}
