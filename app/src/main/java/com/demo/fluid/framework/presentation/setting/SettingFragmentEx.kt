package com.demo.fluid.framework.presentation.setting

import android.content.Intent
import android.net.Uri
import androidx.navigation.fragment.findNavController
import com.demo.fluid.BuildConfig
import com.demo.fluid.R
import com.demo.fluid.util.setPreventDoubleClickScaleView
import com.demo.fluid.framework.presentation.setting.dialog.AdsDialog
import com.demo.fluid.framework.presentation.setting.dialog.DeveloperDialog
import com.demo.fluid.framework.presentation.setting.dialog.FeedbackDialog
import com.demo.fluid.framework.presentation.setting.dialog.IAPDialog
import com.demo.fluid.framework.presentation.setting.dialog.PermissionDialog

fun SettingFragment.onBackEvent() {
    binding.ivBack.setPreventDoubleClickScaleView {
        findNavController().navigateUp()
    }
}

fun SettingFragment.openLanguageScreenEven() {
    binding.clSettingLanguage.setPreventDoubleClickScaleView {
        safeNav(
            R.id.settingFragment,
            R.id.action_settingFragment_to_languageFragment
        )
    }
}

fun SettingFragment.showVersionName() {
    binding.llAppVersion.text =
        buildString {
            append(getString(R.string.application_version_v))
            append(" ")
            append(BuildConfig.VERSION_NAME)
        }
}

fun SettingFragment.onDeveloperEvent() {
    binding.clDev.setPreventDoubleClickScaleView {
        DeveloperDialog().show(childFragmentManager, "")
    }
}

fun SettingFragment.onIAPEvent() {
    binding.clPurchase.setPreventDoubleClickScaleView {
        IAPDialog().show(childFragmentManager, "")
    }
}

fun SettingFragment.onAdsEvent() {
    binding.clAd.setPreventDoubleClickScaleView {
        AdsDialog().show(childFragmentManager, "")
    }
}

fun SettingFragment.onFeedbackEvent() {
    binding.clFeedback.setPreventDoubleClickScaleView {
        FeedbackDialog().show(childFragmentManager, "")
    }
}

fun SettingFragment.onPermissionEvent() {
    binding.clPermission.setPreventDoubleClickScaleView {
        context ?: return@setPreventDoubleClickScaleView
        val dialog = PermissionDialog()
        dialog.show(childFragmentManager)
    }
}

fun SettingFragment.onPolicyEvent() {
    binding.clPolicy.setPreventDoubleClickScaleView {

        try {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://sites.google.com/view/cameradectector")
            )
            startActivity(browserIntent)
        } catch (e: Exception) {

        }

    }
}

fun SettingFragment.gdprEvent() {
//    binding.btnGdpr.setPreventDoubleClickScaleView {
//        try {
//            AdsController.getInstance().showPolicyForm(
//                onShow = {
//                    //do nothing
//                },
//                onError = {
//                    //do nothing
//                }
//            )
//        } catch (e: Throwable) {
//
//        }
//    }
//
//    if (AdsController.getInstance().isNeedToShowConsent() || BuildConfig.DEBUG){
//        binding.btnGdpr.show()
//    }else{
//        binding.btnGdpr.gone()
//    }


}

fun SettingFragment.resetGDPR() {
//    if (BuildConfig.DEBUG) {
//        binding.btnResetGdpr.setPreventDoubleClickScaleView {
//            try {
//                AdsController.getInstance().resetConsent()
//            } catch (e: Throwable) {
//
//            }
//        }
//    } else {
//        binding.btnResetGdpr.gone()
//    }
}

fun SettingFragment.resetIapEvent() {
//    if (BuildConfig.DEBUG) {
//        binding.btnResetIap.setPreventDoubleClickScaleView {
//            activity?.let { IAPConnector.resetIap(it) }
//        }
//    } else {
//        binding.btnResetIap.gone()
//    }
}

