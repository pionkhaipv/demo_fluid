package com.demo.fluid.framework.presentation.splash

import androidx.activity.addCallback
import androidx.lifecycle.Observer
import com.demo.fluid.BuildConfig
import com.demo.fluid.R
import com.demo.fluid.util.Constant
import com.example.libiap.IAPConnector
import pion.datlt.libads.AdsController
import pion.datlt.libads.utils.AdsConstant
import pion.datlt.libads.utils.adsuntils.safePreloadAds
import pion.datlt.libads.utils.adsuntils.showSplashInter
import pion.datlt.libads.utils.loadAndShowConsentFormIfRequire
import pion.datlt.libads.utils.requestConsentInfoUpdate
import pion.tech.fluid_wallpaper.util.PrefUtil
import pion.tech.fluid_wallpaper.util.collectFlowOnView

fun SplashFragment.backEvent() {
    activity?.onBackPressedDispatcher?.addCallback(this, true) {
        onBackPressed()
    }
//    binding.btnBack.setPreventDoubleClickScaleView {
//        onBackPressed()
//    }
}

fun SplashFragment.onBackPressed() {
//    findNavController().popBackStack()
}

fun SplashFragment.startAnimation() {
//    binding.loadingView.startAnim(2000L) {
//        val dummyEntity = DummyEntity(id = 0, value = "Hello Home")
//
//        val bundle = Bundle()
//        bundle.putParcelable(BundleKey.KEY_DUMMY_ENTITY, dummyEntity)
//
//        safeNav(R.id.splashFragment, R.id.homeFragment, bundle)
//    }
}

fun SplashFragment.checkIap() {
    goToNextScreen()
//    activity?.application?.let { IAPConnector.initIap(it, "iap_id.json", BuildConfig.DEBUG) }
//    IAPConnector.stateCheckIap.collectFlowOnView(viewLifecycleOwner) { stateCheckIap ->
//        if (stateCheckIap == IAPConnector.StateCheckIap.DONE) {
//            val productModel = IAPConnector.getAllProductModel().find { it.isPurchase }
//            if (productModel != null) {
//                Constant.isPremium = productModel.isPurchase
//                PrefUtil.isPremium = productModel.isPurchase
//                AdsConstant.isPremium = productModel.isPurchase
//            } else {
//                Constant.isPremium = false
//                PrefUtil.isPremium = false
//                AdsConstant.isPremium = false
//            }
//            checkGDPR()
//        } else if (stateCheckIap == IAPConnector.StateCheckIap.FAILED) {
//            Constant.isPremium = false
//            PrefUtil.isPremium = false
//            AdsConstant.isPremium = false
//            checkGDPR()
//        }
//    }
}

fun SplashFragment.checkGDPR() {
//    Constant.remoteConfigState.observe(
//        viewLifecycleOwner,
//        object : Observer<Constant.RemoteConfigState> {
//            override fun onChanged(remoteState: Constant.RemoteConfigState) {
//                if (remoteState == Constant.RemoteConfigState.DONE) {
//                    //check consent
//                    //yeu cau nguoi dung consent truoc
//                    AdsController.getInstance().requestConsentInfoUpdate(
//                        onFailed = { error ->
//                            //vao nhu luong binh thuong
//                            loadAds()
//                        },
//                        onSuccess = { isRequire, isConsentAvailable ->
//                            if (isRequire) {
//                                AdsController.getInstance()
//                                    .loadAndShowConsentFormIfRequire(
//                                        onConsentError = { errorConsent ->
//                                            loadAds()
//                                        },
//                                        onConsentDone = {
//                                            loadAds()
//                                        }
//                                    )
//                            } else {
//                                loadAds()
//                            }
//                        }
//                    )
//                    Constant.remoteConfigState.removeObserver(this)
//                }
//            }
//        })
}

fun SplashFragment.loadAds() {
//    preloadAdsLanguage()
//    showSplashInter(
//        spaceNameConfig = "splash",
//        spaceNameInter1 = "splash1-inter",
//        spaceNameInter2 = "splash2-inter",
//        spaceNameOpenAds = "splash2-openad",
//        timeOut = 15000L,
//        destinationToShowAds = R.id.splashFragment,
//        navOrBack = {
//            goToNextScreen()
//        })
}


fun SplashFragment.goToNextScreen() {
    if (Constant.isSkipLanguageAfterSplash || PrefUtil.isPremium || Constant.isPremium) {
        //nav sang man home
        safeNavInter(
            R.id.splashFragment,
            R.id.action_splashFragment_to_homeFragment
        )
    } else {
        //nav sang man language
        safeNavInter(
            R.id.splashFragment,
            R.id.action_splashFragment_to_languageFragment
        )
    }
}

fun SplashFragment.preloadAdsLanguage() {
    if (AdsConstant.listConfigAds["language"]?.isOn == true) {
        safePreloadAds(spaceNameConfig = "language", spaceNameAds = "language1_banner")
    } else {
        safePreloadAds(spaceNameConfig = "language1.1", spaceNameAds = "language1_native")
        if (AdsConstant.listConfigAds["language1.1"]?.isOn == true) {
            safePreloadAds(spaceNameConfig = "language1.2", spaceNameAds = "language4_native")
        }
    }
}