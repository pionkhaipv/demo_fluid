package com.demo.fluid.framework.presentation.language

import android.os.Handler
import android.util.Log
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.demo.fluid.R
import com.demo.fluid.customview.GridSpacingItemDecoration
import com.demo.fluid.framework.MainActivity
import com.demo.fluid.framework.presentation.model.LanguageItem
import com.demo.fluid.framework.presentation.model.listLanguage
import com.demo.fluid.util.convertDpToPx
import com.demo.fluid.util.setPreventDoubleClickScaleView
import pion.datlt.libads.utils.AdsConstant
import pion.datlt.libads.utils.adsuntils.safePreloadAds
import pion.datlt.libads.utils.adsuntils.show3NativeUsePriority
import pion.datlt.libads.utils.adsuntils.showLoadedBannerAdaptive
import com.demo.fluid.util.Constant
import com.demo.fluid.util.showDialogChangingLanguage
import pion.tech.fluid_wallpaper.util.PrefUtil
import java.util.Locale

fun LanguageFragment.backEvent() {
    val check = findNavController().previousBackStackEntry?.destination?.id == R.id.settingFragment
    activity?.onBackPressedDispatcher?.addCallback(this, true) {
        if (check) findNavController().popBackStack()
    }
    if (check) {
        //vao tu setting
        binding.btnOk.setPreventDoubleClickScaleView {
            findNavController().popBackStack()
        }
    }
}


fun LanguageFragment.getCurrentLanguage() {
    activity ?: return
    currentLanguage = (activity as MainActivity?)?.currentLanguage() ?: "en"
    inputLanguage = currentLanguage
    if (!listLanguage.any { it.languageCode == currentLanguage }) {
        //list language khong chua current language
        currentLanguage = "en"
        inputLanguage = "en"
    }
    for (i in listLanguage.indices) {
        listLanguage[i] = listLanguage[i].copy(isChecked = false)
    }
}

fun LanguageFragment.setUpRecycleViewLanguage() {
    binding.rcvLanguage.layoutManager =
        GridLayoutManager(requireContext(), 3)
    binding.rcvLanguage.addItemDecoration(
        GridSpacingItemDecoration(
            3,
            requireContext().convertDpToPx(24),
            false
        )
    )
    binding.rcvLanguage.adapter = adapter
    binding.rcvLanguage.itemAnimator = null
    bindListToAdapter()
}

fun LanguageFragment.bindListToAdapter() {
    val newList = mutableListOf<LanguageItem>()
    newList.addAll(listLanguage)

    val currentDeviceLanguageCode = Locale.getDefault().language

    val itemToMove = newList.find { it.languageCode.lowercase() == currentDeviceLanguageCode.lowercase() }

    itemToMove?.let {
        newList.remove(it)
        newList.add(4, it)
    }
    adapter.submitList(newList)
}

fun LanguageFragment.onSelectLanguage(languageItem: LanguageItem) {
    currentLanguage = languageItem.languageCode
    for (i in listLanguage.indices) {
        listLanguage[i] =
            listLanguage[i].copy(isChecked = listLanguage[i].languageCode == currentLanguage)
    }
    bindListToAdapter()
}

fun LanguageFragment.chooseLanguageEvent() {
    binding.btnOk.setPreventDoubleClickScaleView {
        val check = navController.previousBackStackEntry?.destination?.id == R.id.settingFragment
        Log.d("CHECKLANGUAGE", "okEvent: 1")
        runCatching {
            if (currentLanguage != inputLanguage) {
                if (check) {
                    Log.d("CHECKLANGUAGE", "okEvent: 2")
                    Constant.isSkipLanguageAfterSplash = true
                    PrefUtil.isChangeLanguageFromSetting = true
                } else {
                    Log.d("CHECKLANGUAGE", "okEvent: 3")
                    PrefUtil.isChangeLanguageFromFirstOpen = true
                }
                Log.d("CHECKLANGUAGE", "okEvent: 4")
                Constant.isBlockNativeLanguage = true
                (activity as MainActivity).setLocale(currentLanguage)
            } else {
                //khong doi ngon ngu
                if (check) {
                    Log.d("CHECKLANGUAGE", "okEvent: 5")
                    Constant.isSkipLanguageAfterSplash = true
                    navController.popBackStack(R.id.splashFragment, false)
                } else {
                    Log.d("CHECKLANGUAGE", "okEvent: 6")
                    navController.navigate(R.id.onboardingFragment)
                }
            }
        }
    }
}


fun LanguageFragment.showDialogAndPreloadNative() {
    if (PrefUtil.isChangeLanguageFromFirstOpen) {
        //show dialog 2s
        val dialog = context?.showDialogChangingLanguage(lifecycle)
        //load truoc qc
        preloadOnBoardAds()
        //chuyen man
        Handler().postDelayed({
            dialog?.dismiss()
            //chuyen man
            (activity as MainActivity?)?.goToOnBoard()
        }, Constant.timeShowDialogChangeLanguage)
    } else {
        preloadOnBoardAds()
    }
}

fun LanguageFragment.preloadOnBoardAds() {
//    val check = findNavController().previousBackStackEntry?.destination?.id != R.id.splashFragment
//    if (check) return
//
//    //onboard 1
//    safePreloadAds(spaceNameConfig = "onboard1.1", spaceNameAds = "onboard1_native_ID1")
//    safePreloadAds(spaceNameConfig = "onboard1.1", spaceNameAds = "onboard1_native_ID2")
//    safePreloadAds(spaceNameConfig = "onboard1.1", spaceNameAds = "onboard1_native_ID3")
//
//    //onboard 2
//    safePreloadAds(spaceNameConfig = "Onboard2", spaceNameAds = "onboard2_native")
//
//    //onboard 3
//    safePreloadAds(spaceNameConfig = "onboardfull1.1", spaceNameAds = "onboardfull_native_ID1", adChoice = AdsConstant.TOP_LEFT)
//    safePreloadAds(spaceNameConfig = "onboardfull1.1", spaceNameAds = "onboardfull_native_ID2", adChoice = AdsConstant.TOP_LEFT)
//    safePreloadAds(spaceNameConfig = "onboardfull1.1", spaceNameAds = "onboardfull_native_ID3", adChoice = AdsConstant.TOP_LEFT)
//
//    //onboard 4
//    safePreloadAds(spaceNameConfig = "Onboard3", spaceNameAds = "onboard2_native")
}

fun LanguageFragment.showReloadNativeAds() {
    if (!isShowReloadInter && AdsConstant.listConfigAds["language1.1"]?.isOn == true) {
        isShowReloadInter = true
        show3NativeUsePriority(
            spaceNameConfig = "language1.2",
            spaceName1 = "language4_native",
            spaceName2 = "language5_native",
            spaceName3 = "language6_native",
            layoutToAttachAds = binding.adViewGroup,
            layoutContainAds = binding.layoutAds,
            onAdsClick = {
                isClickAds = true
            })
    }
}

fun LanguageFragment.showNativeAds() {
    if (!Constant.isBlockNativeLanguage) {
        if (AdsConstant.listConfigAds["language"]?.isOn == false) {
            show3NativeUsePriority(
                spaceNameConfig = "language1.1",
                spaceName1 = "language1_native",
                spaceName2 = "language2_native",
                spaceName3 = "language3_native",
                layoutToAttachAds = binding.adViewGroup,
                layoutContainAds = binding.layoutAds,
                onAdsClick = {
                    isClickAds = true
                })
        } else {
            showLoadedBannerAdaptive(
                spaceNameConfig = "language",
                spaceName = "language1_banner",
                includeHasBeenOpened = false,
                layoutToAttachAds = binding.adViewGroup,
                layoutContainAds = binding.layoutAds,
            )
        }
    } else {
        Constant.isBlockNativeLanguage = false
    }
}
