package pion.datlt.libads.utils.adsuntils

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import pion.datlt.libads.AdsController
import pion.datlt.libads.callback.PreloadCallback
import pion.datlt.libads.model.ConfigAds
import pion.datlt.libads.model.ConfigNative
import pion.datlt.libads.utils.AdDef
import pion.datlt.libads.utils.AdsConstant
import pion.datlt.libads.utils.StateLoadAd
import java.util.*

fun checkConditionShowAds(context: Context?, spaceNameConfig: String): Boolean {
    context ?: return false
    val config: ConfigAds? = AdsConstant.listConfigAds[spaceNameConfig]
    val isOn = config?.isOn ?: false
    Log.d(
        "CHECKCONDITION",
        "checkConditionShowAds: " +
                "${AdsConstant.isInternetConnected} " +
                "${!AdsConstant.isPremium} " +
                "$isOn " +
                "${isOverTimeDelay(spaceNameConfig = spaceNameConfig)}"
    )

    if (AdsConstant.disableAllConfig) return false

    return (AdsConstant.isInternetConnected
            && !AdsConstant.isPremium
            && isOn
            && isOverTimeDelay(spaceNameConfig = spaceNameConfig))
}

private fun isOverTimeDelay(spaceNameConfig: String): Boolean {
    val config: ConfigAds? = AdsConstant.listConfigAds[spaceNameConfig]
    config ?: return false
    val timeDelay = config.timeDelayShowInter
    timeDelay ?: return true
    return Date().time - AdsController.lastTimeShowAdsInter > timeDelay * 1000
}

fun setLastTimeShowInter(spaceNameConfig: String) {
    AdsController.lastTimeShowAdsInter = System.currentTimeMillis()
}

fun Fragment.safePreloadAds(
    spaceNameConfig: String,
    spaceNameAds: String,
    includeHasBeenOpened: Boolean? = null,
    positionCollapsibleBanner: String? = null,
    adChoice: Int? = null,
    isOneTimeCollapsible: Boolean? = null,
    preloadCallback: PreloadCallback? = null
) {
    val config = AdsConstant.listConfigAds[spaceNameConfig]
    val isOn = config?.isOn ?: false

    if (AdsConstant.isPremium) {
        preloadCallback?.onLoadFail(AdsConstant.ERROR_PREMIUM)
        return
    }

//    if (!AdsController.checkInit()) {
//        preloadCallback?.onLoadFail(AdsConstant.ERROR_INIT)
//        return
//    }

    val isTypeEnable = checkAdsByType(spaceNameAds)

    if (AdsController.getInstance().checkAdsState(spaceNameAds) == StateLoadAd.SUCCESS) {
        preloadCallback?.onLoadDone()
    } else if (AdsController.getInstance().checkAdsState(spaceNameAds) == StateLoadAd.LOADING) {
        //set new call back
        AdsController.getInstance().setPreloadCallback(spaceNameAds, preloadCallback)
    } else if (includeHasBeenOpened == true && AdsController.getInstance()
            .checkAdsState(spaceNameAds) == StateLoadAd.HAS_BEEN_OPENED
    ) {
        preloadCallback?.onLoadDone()
    } else if (!isTypeEnable) {
        preloadCallback?.onLoadFail("remote type off")
    } else {
        if (isOn) {
            //tinh toan ad choice
            val newAdChoice: Int? = adChoice
                ?: (config?.getConfigNative(
                    context = context,
                    default = ConfigNative(
                        ratio = "360:94",
                        adChoice = AdsConstant.TOP_LEFT
                    )
                )?.adChoice)



            AdsController.getInstance()
                .preload(
                    spaceName = spaceNameAds,
                    includeHasBeenOpened = includeHasBeenOpened,
                    positionCollapsibleBanner = positionCollapsibleBanner,
                    adChoice = newAdChoice,
                    isOneTimeCollapsible = isOneTimeCollapsible,
                    preloadCallback = preloadCallback
                )
        } else {
            preloadCallback?.onLoadFail("remote off")
        }
    }

}

fun checkAdsByType(spaceNameAds: String): Boolean {
    val adsDetail = AdsController.getInstance().getAdsDetail(spaceNameAds)
    return when (adsDetail?.adsType) {
        //admob
        AdDef.ADS_TYPE_ADMOB.OPEN_APP -> {
            return AdsConstant.isOpenAppOn
        }

        AdDef.ADS_TYPE_ADMOB.INTERSTITIAL -> {
            return AdsConstant.isInterstitialOn
        }

        AdDef.ADS_TYPE_ADMOB.NATIVE -> {
            return AdsConstant.isNativeOn
        }

        AdDef.ADS_TYPE_ADMOB.NATIVE_FULL_SCREEN -> {
            return AdsConstant.isNativeFullScreenOn
        }

        AdDef.ADS_TYPE_ADMOB.BANNER -> {
            return AdsConstant.isBannerOn
        }

        AdDef.ADS_TYPE_ADMOB.BANNER_ADAPTIVE -> {
            return AdsConstant.isBannerAdaptiveOn
        }

        AdDef.ADS_TYPE_ADMOB.BANNER_LARGE -> {
            return AdsConstant.isBannerLargeOn
        }

        AdDef.ADS_TYPE_ADMOB.BANNER_INLINE -> {
            return AdsConstant.isBannerInlineOn
        }

        AdDef.ADS_TYPE_ADMOB.BANNER_COLLAPSIBLE -> {
            return AdsConstant.isBannerCollapsibleOn
        }

        AdDef.ADS_TYPE_ADMOB.REWARD_VIDEO -> {
            return AdsConstant.isRewardVideoOn
        }

        AdDef.ADS_TYPE_ADMOB.REWARD_INTERSTITIAL -> {
            return AdsConstant.isRewardInterOn
        }

        else -> {
            false
        }
    }
}