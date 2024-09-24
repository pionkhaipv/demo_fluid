package com.demo.fluid.framework.presentation.personal

import android.os.Bundle
import android.view.View
import com.demo.fluid.R
import com.demo.fluid.databinding.FragmentPersonalBinding
import com.demo.fluid.framework.database.entities.WallpaperWithTextView
import com.demo.fluid.framework.presentation.common.BaseFragment
import com.demo.fluid.framework.presentation.personal.adapter.PersonalAdapter
import com.demo.fluid.util.BundleKey
import dagger.hilt.android.AndroidEntryPoint
import pion.tech.fluid_wallpaper.util.collectFlowOnView

@AndroidEntryPoint
class PersonalFragment : BaseFragment<FragmentPersonalBinding, PersonalViewModel>(
    FragmentPersonalBinding::inflate,
    PersonalViewModel::class.java
), PersonalAdapter.Listener {
    val adapter = PersonalAdapter()
    override fun init(view: View) {
        onBackEvent()
        setUpAdapter()
    }

    override fun subscribeObserver(view: View) {
        viewModel.wallpaperWithTextViewStateFlow.collectFlowOnView(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.getAllWallpaper()
    }

    override fun onItemClick(item: WallpaperWithTextView) {
        val bundle = Bundle()
        bundle.putParcelable(BundleKey.KEY_WALLPAPER_WITH_TEXTVIEW, item)
        safeNav(R.id.personalFragment, R.id.action_personalFragment_to_editFluidFragment, bundle)
    }

}
