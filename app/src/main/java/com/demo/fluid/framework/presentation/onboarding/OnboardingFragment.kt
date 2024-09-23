package com.demo.fluid.framework.presentation.onboarding

import android.view.View
import com.demo.fluid.R
import com.demo.fluid.databinding.FragmentOnboardingBinding
import com.demo.fluid.framework.presentation.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : BaseFragment<FragmentOnboardingBinding, OnboardingViewModel>(
    FragmentOnboardingBinding::inflate,
    OnboardingViewModel::class.java
) {
//    val adapter = OnboardingViewpagerAdapter(this)

    override fun init(view: View) {
//        binding.btnHomeScreen.post {
//            safeNav(R.id.onboardingFragment, R.id.action_onboardingFragment_to_homeFragment)
//        }
        initViewPager()
        onBackEvent()
        binding.vpMain.post {
            safeNav(R.id.onboardingFragment,R.id.action_to_homeFragment)
        }
    }

    override fun subscribeObserver(view: View) {

    }

    override fun onResume() {
        super.onResume()
//        try {
//            val viewHolder =
//                (binding.vpMain[0] as RecyclerView).findViewHolderForAdapterPosition(binding.vpMain.currentItem) as OnboardingViewpagerAdapter.ViewHolder
//            viewHolder.showAdsReload()
//        } catch (e: Exception) {
//
//        }
    }

}
