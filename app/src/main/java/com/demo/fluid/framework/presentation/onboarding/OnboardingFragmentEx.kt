package com.demo.fluid.framework.presentation.onboarding

import androidx.activity.addCallback
import com.example.libiap.IAPConnector
import pion.datlt.libads.utils.adsuntils.loadAndShowInterstitial

fun OnboardingFragment.initViewPager() {
//    binding.vpMain.adapter = adapter
}

fun OnboardingFragment.onNextViewPagerEvent() {
    binding.vpMain.currentItem += 1
}

fun OnboardingFragment.onPreviousViewPagerEvent() {
    binding.vpMain.currentItem -= 1
}

fun OnboardingFragment.goToHomeEvent(){
//    safeNav(R.id.onboardingFragment, R.id.action_onboardingFragment_to_homeFragment)
}

fun OnboardingFragment.onBackEvent() {
    activity?.onBackPressedDispatcher?.addCallback(this, true) {
        backEvent()
    }
}

fun OnboardingFragment.backEvent() {
    //Do noting
}

fun OnboardingFragment.selectVersion(isBuyIap: Boolean) {

//    if (isBuyIap) {
//        //mua iap
//        activity?.let { atvt ->
//            IAPConnector.buyIap(atvt, Constant.iapId)
//        }
//    } else {
//        //chuyen man show inter
//        loadAndShowInterstitial(
//            spaceNameConfig = "Onboard IAP",
//            spaceName = "onboardIAP_Inter",
//            destinationToShowAds = R.id.onboardingFragment,
//            isScreenType = false,
//            navOrBack = {
//                safeNavInter(
//                    R.id.onboardingFragment,
//                    R.id.action_onboardingFragment_to_homeFragment
//                )
//            },
//            onCloseAds = {})
//    }
}