package com.demo.fluid.framework.presentation.home

import androidx.recyclerview.widget.GridLayoutManager
import com.demo.fluid.framework.presentation.home.HomeFragment

fun HomeFragment.initView() {
    binding.rvMain.layoutManager = GridLayoutManager(requireContext(),2)
    binding.rvMain.adapter = adapter

}
