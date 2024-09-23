package com.demo.fluid.framework.presentation.listFluid

import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.demo.fluid.customview.GridSpacingItemDecoration
import com.demo.fluid.util.convertDpToPx
import com.demo.fluid.util.setPreventDoubleClickScaleView

fun ListFluidFragment.onBackEvent() {
    binding.ivBack.setPreventDoubleClickScaleView {
        backEvent()
    }
    activity?.onBackPressedDispatcher?.addCallback(this, true) {
        backEvent()
    }
}

fun ListFluidFragment.backEvent() {
    findNavController().navigateUp()
}

fun ListFluidFragment.initView() {
    commonViewModel.currentAddedTextFilePath = null
}

fun ListFluidFragment.setUpAdapter() {
    adapter.setListener(this)
    binding.rvMain.layoutManager = GridLayoutManager(requireContext(), 3)
    binding.rvMain.adapter = adapter
    binding.rvMain.addItemDecoration(
        GridSpacingItemDecoration(
            3,
            requireContext().convertDpToPx(12),
            false
        )
    )
    adapter.submitList(viewModel.listFluidWallpaper)
}