//package pion.tech.camera_detector.framework.presentation.onboarding.viewpager
//
//import android.graphics.Color
//import pion.datlt.libads.utils.adsuntils.showLoadedNative
//import pion.tech.camera_detector.databinding.PagerOnboardView2Binding
//import com.demo.fluid.framework.presentation.onboarding.OnboardingFragment
//import com.demo.fluid.framework.presentation.onboarding.adapter.OnboardingViewpagerAdapter
//import com.demo.fluid.framework.presentation.onboarding.onNextViewPagerEvent
//import com.demo.fluid.framework.presentation.onboarding.onPreviousViewPagerEvent
//import pion.tech.camera_detector.framework.presentation.setting.dialog.PermissionDialog
//import pion.tech.camera_detector.util.changeTextColor
//import pion.tech.camera_detector.util.setPreventDoubleClickScaleView
//
//class OnboardViewHolder2(val binding: PagerOnboardView2Binding, val fragment: OnboardingFragment) :
//    OnboardingViewpagerAdapter.ViewHolder(binding.root) {
//
//    override fun bind() {
//        initView()
//        showAdsNative()
//        nextEvent()
//        previousEvent()
//        grandPermissionEvent()
//    }
//
//    private fun initView(){
//        runCatching {
//            binding.tvDes.changeTextColor(start = 25, end = 42, color = Color.parseColor("#FFD656"))
//        }
//    }
//
//    private fun nextEvent() {
//        binding.ivNext.setPreventDoubleClickScaleView {
//            fragment.onNextViewPagerEvent()
//        }
//    }
//
//    private fun previousEvent() {
//        binding.ivPrevious.setPreventDoubleClickScaleView {
//            fragment.onPreviousViewPagerEvent()
//        }
//    }
//
//    private fun grandPermissionEvent() {
//        binding.tvGrandPermission.setPreventDoubleClickScaleView {
//            val dialog = PermissionDialog()
//            dialog.show(fragment.childFragmentManager)
//        }
//    }
//
//    override fun showAdsReload() {
//
//    }
//
//    private fun showAdsNative() {
//        fragment.showLoadedNative(
//            spaceNameConfig = "Onboard2",
//            spaceName = "onboard2_native",
//            layoutToAttachAds = binding.adViewGroup,
//            layoutContainAds = binding.layoutAds,
//            onAdsClick = {})
//    }
//}