package com.demo.fluid.framework.presentation.listFluid

import android.os.Bundle
import android.view.View
import com.demo.fluid.R
import com.demo.fluid.databinding.FragmentListFluidBinding
import dagger.hilt.android.AndroidEntryPoint
import com.demo.fluid.framework.presentation.common.BaseFragment
import com.demo.fluid.framework.presentation.listFluid.adapter.FluidAdapter
import com.demo.fluid.framework.presentation.model.HomeModel
import com.demo.fluid.util.BundleKey

@AndroidEntryPoint
class ListFluidFragment : BaseFragment<FragmentListFluidBinding, ListFluidViewModel>(
    FragmentListFluidBinding::inflate,
    ListFluidViewModel::class.java
), FluidAdapter.Listener {

    val adapter = FluidAdapter()
    override fun init(view: View) {
        initView()
        onBackEvent()
        viewModel.initData()
        setUpAdapter()

    }

    override fun subscribeObserver(view: View) {

    }

    override fun onItemClick(item: HomeModel) {
        val bundle = Bundle()
        bundle.putString(BundleKey.KEY_FLUID_NAME_EDIT, item.title)
        safeNav(R.id.listFluidFragment, R.id.action_listFluidFragment_to_editFluidFragment, bundle)
    }

}
