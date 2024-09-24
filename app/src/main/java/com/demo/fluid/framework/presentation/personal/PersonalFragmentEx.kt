package com.demo.fluid.framework.presentation.personal

import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.demo.fluid.customview.GridSpacingItemDecoration
import com.demo.fluid.util.convertDpToPx
import com.demo.fluid.util.setPreventDoubleClickScaleView

fun PersonalFragment.setUpAdapter() {
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
}

fun PersonalFragment.onBackEvent() {
    binding.ivBack.setPreventDoubleClickScaleView {
        backEvent()
    }
    activity?.onBackPressedDispatcher?.addCallback(this, true) {
        backEvent()
    }
}

fun PersonalFragment.backEvent() {
    findNavController().navigateUp()
}