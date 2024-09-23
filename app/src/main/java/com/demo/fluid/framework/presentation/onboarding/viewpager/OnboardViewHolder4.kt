//package pion.tech.camera_detector.framework.presentation.onboarding.viewpager
//
//import com.example.libiap.IAPConnector
//import pion.tech.camera_detector.R
//import pion.tech.camera_detector.databinding.PagerOnboardView4Binding
//import com.demo.fluid.framework.presentation.onboarding.OnboardingFragment
//import com.demo.fluid.framework.presentation.onboarding.adapter.OnboardingViewpagerAdapter
//import com.demo.fluid.framework.presentation.onboarding.goToHomeEvent
//import com.demo.fluid.framework.presentation.onboarding.onPreviousViewPagerEvent
//import com.demo.fluid.framework.presentation.onboarding.selectVersion
//import pion.tech.camera_detector.util.Constant
//import pion.tech.camera_detector.util.setPreventDoubleClickScaleView
//
//class OnboardViewHolder4(val binding: PagerOnboardView4Binding, val fragment: OnboardingFragment) :
//    OnboardingViewpagerAdapter.ViewHolder(binding.root) {
//
//    override fun bind() {
//        submitEvent()
//        previousEvent()
//        showAdsNative()
//        noAdVersionEvent()
//        includeAdsVersionEvent()
//        applyEvent()
//        initView()
//    }
//
//    private fun initView(){
//        binding.apply {
//            isNoAdsVersion = false
//            giaThat = IAPConnector.getProductById(Constant.iapId)?.formattedPrice
//            giaGach = IAPConnector.getProductById(Constant.iapGiaGach)?.formattedPrice
//        }
//    }
//
//    private fun submitEvent() {
//        binding.ivSubmit.setPreventDoubleClickScaleView {
//            fragment.goToHomeEvent()
//        }
//    }
//
//    private fun noAdVersionEvent() {
//        binding.btnNoAdVersion.setPreventDoubleClickScaleView {
//            binding.isNoAdsVersion = true
//            binding.btnApply.text = fragment.getString(R.string.buy_this_version)
//        }
//    }
//
//    private fun includeAdsVersionEvent() {
//        binding.btnIncludeAdsVersion.setPreventDoubleClickScaleView {
//            binding.isNoAdsVersion = false
//            binding.btnApply.text = fragment.getString(R.string.use_this_version)
//        }
//    }
//
//    private fun previousEvent() {
//        binding.ivPrevious.setPreventDoubleClickScaleView {
//            fragment.onPreviousViewPagerEvent()
//        }
//    }
//
//    private fun applyEvent() {
//        binding.btnApply.setPreventDoubleClickScaleView {
//            binding.isNoAdsVersion?.let {
//                fragment.selectVersion(it)
//            }
//        }
//    }
//    override fun showAdsReload() {
//
//    }
//
//    private fun showAdsNative() {
////        fragment.showLoadedNative(
////            spaceNameConfig = "onboard3",
////            spaceName = "onboard3_native_ID1",
////            layoutToAttachAds = binding.adViewGroup,
////            layoutContainAds = binding.layoutAds,
////            onAdsClick = {})
//    }
//}