//package pion.tech.camera_detector.framework.presentation.onboarding.viewpager
//
//import android.graphics.Color
//import pion.datlt.libads.utils.AdsConstant
//import pion.datlt.libads.utils.adsuntils.safePreloadAds
//import pion.datlt.libads.utils.adsuntils.show3NativeUsePriority
//import pion.tech.camera_detector.databinding.PagerOnboardView1Binding
//import com.demo.fluid.framework.presentation.onboarding.OnboardingFragment
//import com.demo.fluid.framework.presentation.onboarding.adapter.OnboardingViewpagerAdapter
//import com.demo.fluid.framework.presentation.onboarding.onNextViewPagerEvent
//import pion.tech.camera_detector.util.changeTextColor
//import pion.tech.camera_detector.util.setPreventDoubleClickScaleView
//
//class OnboardViewHolder1(val binding: PagerOnboardView1Binding, val fragment: OnboardingFragment) :
//    OnboardingViewpagerAdapter.ViewHolder(binding.root) {
//
//    private var isClickAds = false
//    private var isShowReloadAds = false
//
//    override fun bind() {
//        initView()
//        nextEvent()
//        showAdsNative()
//    }
//
//    private fun initView(){
//        runCatching {
//            binding.tvDes.changeTextColor(start = 23, end = 43, color = Color.parseColor("#FFD656"))
//        }
//    }
//
//    private fun nextEvent() {
//        binding.ivNext.setPreventDoubleClickScaleView {
//            fragment.onNextViewPagerEvent()
//        }
//    }
//
//    private fun showAdsNative() {
//        fragment.show3NativeUsePriority(
//            spaceNameConfig = "onboard1.1",
//            spaceName1 = "onboard1_native_ID1",
//            spaceName2 = "onboard1_native_ID2",
//            spaceName3 = "onboard1_native_ID3",
//            layoutToAttachAds = binding.adViewGroup,
//            layoutContainAds = binding.layoutAds,
//            onAdsClick = {
//                isClickAds = true
//
//                fragment.safePreloadAds(
//                    spaceNameConfig = "onboard1.2",
//                    spaceNameAds = "onboard1_native_ID4"
//                )
//                fragment.safePreloadAds(
//                    spaceNameConfig = "onboard1.2",
//                    spaceNameAds = "onboard1_native_ID5"
//                )
//                fragment.safePreloadAds(
//                    spaceNameConfig = "onboard1.2",
//                    spaceNameAds = "onboard1_native_ID6"
//                )
//            })
//    }
//
//    override fun showAdsReload() {
//        if (!isShowReloadAds && isClickAds && AdsConstant.listConfigAds["onboard1.1"]?.isOn == true) {
//            fragment.show3NativeUsePriority(
//                spaceNameConfig = "onboard1.2",
//                spaceName1 = "onboard1_native_ID4",
//                spaceName2 = "onboard1_native_ID5",
//                spaceName3 = "onboard1_native_ID6",
//                layoutToAttachAds = binding.adViewGroup,
//                layoutContainAds = binding.layoutAds,
//                onAdsClick = {})
//        }
//    }
//}