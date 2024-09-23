//package pion.tech.camera_detector.framework.presentation.onboarding.viewpager
//
//import android.view.LayoutInflater
//import pion.datlt.libads.R
//import pion.datlt.libads.utils.AdsConstant
//import pion.datlt.libads.utils.adsuntils.safePreloadAds
//import pion.datlt.libads.utils.adsuntils.show3NativeFullScreenUsePriority
//import pion.tech.camera_detector.databinding.PagerOnboardFullNativeBinding
//import com.demo.fluid.framework.presentation.onboarding.OnboardingFragment
//import com.demo.fluid.framework.presentation.onboarding.adapter.OnboardingViewpagerAdapter
//import com.demo.fluid.framework.presentation.onboarding.onNextViewPagerEvent
//import com.demo.fluid.framework.presentation.onboarding.onPreviousViewPagerEvent
//import pion.tech.camera_detector.util.setPreventDoubleClickScaleView
//
//class OnboardFullNativeViewHolder(val binding : PagerOnboardFullNativeBinding, val fragment : OnboardingFragment) : OnboardingViewpagerAdapter.ViewHolder(binding.root) {
//
//    private var isClickAds = false
//    private var isShowReloadAds = false
//
//    override fun bind() {
//        showAdsNative()
//        nextEvent()
//        previousEvent()
//    }
//
//    private fun nextEvent(){
//        binding.btnNext.setPreventDoubleClickScaleView {
//            fragment.onNextViewPagerEvent()
//        }
//    }
//
//    private fun previousEvent(){
//        binding.btnPrevious.setPreventDoubleClickScaleView {
//            fragment.onPreviousViewPagerEvent()
//        }
//    }
//
//    private fun showAdsNative() {
//        //load and show native
//        fragment.show3NativeFullScreenUsePriority(
//            spaceNameConfig = "onboardfull1.1",
//            spaceName1 = "onboardfull_native_ID1",
//            spaceName2 = "onboardfull_native_ID2",
//            spaceName3 = "onboardfull_native_ID3",
//            layoutToAttachAds = binding.adViewGroup,
//            layoutContainAds = binding.layoutAds,
//            viewAdsInflateFromXml = LayoutInflater.from(fragment.context).inflate(R.layout.layout_native_full_screen, null),
//            onAdsClick = {
//                isClickAds = true
//
//                fragment.safePreloadAds(spaceNameConfig = "onboardfull1.2", spaceNameAds = "onboardfull_native_ID4", adChoice = AdsConstant.TOP_LEFT)
//                fragment.safePreloadAds(spaceNameConfig = "onboardfull1.2", spaceNameAds = "onboardfull_native_ID5", adChoice = AdsConstant.TOP_LEFT)
//                fragment.safePreloadAds(spaceNameConfig = "onboardfull1.2", spaceNameAds = "onboardfull_native_ID6", adChoice = AdsConstant.TOP_LEFT)
//            })
//
//    }
//
//    override fun showAdsReload() {
//        if(isClickAds && !isShowReloadAds && AdsConstant.listConfigAds["onboardfull1.1"]?.isOn == true){
//            isShowReloadAds = true
//            //do nothing
//            fragment.show3NativeFullScreenUsePriority(
//                spaceNameConfig = "onboardfull1.2",
//                spaceName1 = "onboardfull_native_ID4",
//                spaceName2 = "onboardfull_native_ID5",
//                spaceName3 = "onboardfull_native_ID6",
//                layoutToAttachAds = binding.adViewGroup,
//                layoutContainAds = binding.layoutAds,
//                viewAdsInflateFromXml = LayoutInflater.from(fragment.context).inflate(R.layout.layout_native_full_screen, null),
//                onAdsClick = {})
//        }
//    }
//
//}