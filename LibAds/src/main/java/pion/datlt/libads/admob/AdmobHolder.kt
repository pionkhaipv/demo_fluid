package pion.datlt.libads.admob

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import pion.datlt.libads.admob.ads.AdmobAds
import pion.datlt.libads.admob.ads.AdmobBanner300x250Ads
import pion.datlt.libads.admob.ads.AdmobBannerAdaptiveAds
import pion.datlt.libads.admob.ads.AdmobBannerCollapsibleAds
import pion.datlt.libads.admob.ads.AdmobBannerLargeAds
import pion.datlt.libads.admob.ads.AdmobInterstitialAds
import pion.datlt.libads.admob.ads.AdmobNativeAds
import pion.datlt.libads.admob.ads.AdmobNativeFullScreenAds
import pion.datlt.libads.admob.ads.AdmobOpenAppAds
import pion.datlt.libads.admob.ads.AdmobRewardInterstitialAds
import pion.datlt.libads.admob.ads.AdmobRewardVideoAds
import pion.datlt.libads.callback.AdCallback
import pion.datlt.libads.callback.PreloadCallback
import pion.datlt.libads.model.AdsChild
import pion.datlt.libads.utils.AdDef
import pion.datlt.libads.utils.CommonUtils
import pion.datlt.libads.utils.StateLoadAd
import java.util.*


class AdmobHolder {


    private var hashMap: HashMap<String, AdmobAds> = HashMap()

    fun loadAndShow(
        activity: Activity,
        adsChild: AdsChild,
        destinationToShowAds: Int?,
        adCallback: AdCallback?,
        lifecycle: Lifecycle?,
        timeout: Long?,
        layoutToAttachAds: ViewGroup?,
        viewAdsInflateFromXml: View?,
        adChoice: Int?,
        positionCollapsibleBanner: String?,
        isOneTimeCollapsible: Boolean?
    ) {
        var ads: AdmobAds? = null
        val key = adsChild.spaceName.lowercase()
        ads = getAdsByType(adsChild)
        if (ads == null) {
            CommonUtils.showToastDebug(
                activity,
                "not support ${adsChild.adsType} ${adsChild.spaceName} check file json"
            )
            adCallback?.onAdFailToLoad("not support adType ${adsChild.adsType} check file json")
        } else {
            ads.loadAndShow(
                activity = activity,
                adsChild = adsChild,
                destinationToShowAds = destinationToShowAds,
                adCallback = adCallback,
                lifecycle = lifecycle,
                timeout = timeout,
                layoutToAttachAds = layoutToAttachAds,
                viewAdsInflateFromXml = viewAdsInflateFromXml,
                adChoice = adChoice,
                positionCollapsibleBanner = positionCollapsibleBanner,
                isOneTimeCollapsible = isOneTimeCollapsible
            )
            hashMap[key] = ads
        }
    }

    fun showLoadedAds(
        activity: Activity,
        adsChild: AdsChild,
        includeHasBeenOpened: Boolean?,
        destinationToShowAds: Int?,
        adCallback: AdCallback?,
        lifecycle: Lifecycle?,
        timeout: Long?,
        layoutToAttachAds: ViewGroup?,
        viewAdsInflateFromXml: View?,
        adChoice: Int?,
        positionCollapsibleBanner: String?,
        isOneTimeCollapsible: Boolean?
    ) {
        val key = adsChild.spaceName.lowercase()
        val ads = hashMap[key]
        if (ads == null) {
            //quảng cáo chưa từng được load trước đó
            //thực hiện luồng load and show
            loadAndShow(
                activity = activity,
                adsChild = adsChild,
                destinationToShowAds = destinationToShowAds,
                adCallback = adCallback,
                lifecycle = lifecycle,
                timeout = timeout,
                layoutToAttachAds = layoutToAttachAds,
                viewAdsInflateFromXml = viewAdsInflateFromXml,
                adChoice = adChoice,
                positionCollapsibleBanner = positionCollapsibleBanner,
                isOneTimeCollapsible = isOneTimeCollapsible
            )
        } else {
            if (
                ads.getStateLoadAd() == StateLoadAd.SUCCESS || // quảng cáo đã được load thành công
                (includeHasBeenOpened == true && ads.getStateLoadAd() == StateLoadAd.HAS_BEEN_OPENED)// hoặc quảng cáo đã load và mở(đối với native, banner)
            ) {
                //show luôn
                ads.show(
                    activity = activity,
                    adsChild = adsChild,
                    destinationToShowAds = destinationToShowAds,
                    adCallback = adCallback,
                    lifecycle = lifecycle,
                    layoutToAttachAds = layoutToAttachAds,
                    viewAdsInflateFromXml = viewAdsInflateFromXml
                )
            } else if (ads.getStateLoadAd() == StateLoadAd.LOADING) {
                //qc vẫn đang load, load xong cái là show lên luôn
                ads.setPreloadCallback(
                    preloadCallback = object : PreloadCallback {
                        override fun onLoadDone() {
                            ads.show(
                                activity = activity,
                                adsChild = adsChild,
                                destinationToShowAds = destinationToShowAds,
                                adCallback = adCallback,
                                lifecycle = lifecycle,
                                layoutToAttachAds = layoutToAttachAds,
                                viewAdsInflateFromXml = viewAdsInflateFromXml
                            )
                        }

                        override fun onLoadFail(error: String) {
                            adCallback?.onAdFailToLoad(error)
                        }
                    }
                )
            } else {
                //qc chưa dược load, load and show như bình thường
                ads.loadAndShow(
                    activity = activity,
                    adsChild = adsChild,
                    destinationToShowAds = destinationToShowAds,
                    adCallback = adCallback,
                    lifecycle = lifecycle,
                    timeout = timeout,
                    layoutToAttachAds = layoutToAttachAds,
                    viewAdsInflateFromXml = viewAdsInflateFromXml,
                    adChoice = adChoice,
                    positionCollapsibleBanner = positionCollapsibleBanner,
                    isOneTimeCollapsible = isOneTimeCollapsible
                )
            }
            hashMap[key] = ads
        }
    }

    fun preload(
        activity: Activity,
        adsChild: AdsChild,
        includeHasBeenOpened: Boolean?,
        positionCollapsibleBanner: String?,
        adChoice: Int?,
        isOneTimeCollapsible: Boolean?,
        preloadCallback: PreloadCallback?
    ) {
        var ads: AdmobAds? = null
        val key = adsChild.spaceName.lowercase(Locale.getDefault())
        ads = getAdsByType(adsChild)
        if (ads == null) {
            CommonUtils.showToastDebug(
                activity,
                "not support adType ${adsChild.adsType} check file json 3"
            )
            preloadCallback?.onLoadFail("not support adType ${adsChild.adsType} check file json")
        } else {
            if (ads.getStateLoadAd() == StateLoadAd.SUCCESS){
                preloadCallback?.onLoadDone()
            }else if (includeHasBeenOpened == true && ads.getStateLoadAd() == StateLoadAd.HAS_BEEN_OPENED){
                preloadCallback?.onLoadDone()
            }else if (ads.getStateLoadAd() == StateLoadAd.LOADING){
                ads.setPreloadCallback(preloadCallback)
            }else{
                ads.preload(
                    activity = activity,
                    adsChild = adsChild,
                    positionCollapsibleBanner = positionCollapsibleBanner,
                    adChoice = adChoice,
                    isOneTimeCollapsible = isOneTimeCollapsible
                )
                ads.setPreloadCallback(preloadCallback)
            }
            hashMap[key] = ads
        }
    }

    fun getStatusPreload(adChild: AdsChild): StateLoadAd {
        hashMap[adChild.spaceName.lowercase()]?.let {
            return it.getStateLoadAd()
        }
        return StateLoadAd.NULL
    }

    fun getAdsByType(adsChild: AdsChild): AdmobAds? {
        AdDef.NETWORK
        return when (adsChild.adsType.lowercase(Locale.getDefault())) {
            AdDef.ADS_TYPE_ADMOB.NATIVE -> {
                AdmobNativeAds()
            }

            AdDef.ADS_TYPE_ADMOB.NATIVE_FULL_SCREEN -> {
                AdmobNativeFullScreenAds()
            }

            AdDef.ADS_TYPE_ADMOB.INTERSTITIAL -> {
                AdmobInterstitialAds()
            }

            AdDef.ADS_TYPE_ADMOB.BANNER -> {
                AdmobBanner300x250Ads()
            }

            AdDef.ADS_TYPE_ADMOB.BANNER_ADAPTIVE -> {
                AdmobBannerAdaptiveAds()
            }

            AdDef.ADS_TYPE_ADMOB.BANNER_LARGE -> {
                AdmobBannerLargeAds()
            }

            AdDef.ADS_TYPE_ADMOB.REWARD_VIDEO -> {
                AdmobRewardVideoAds()
            }

            AdDef.ADS_TYPE_ADMOB.REWARD_INTERSTITIAL -> {
                AdmobRewardInterstitialAds()
            }

            AdDef.ADS_TYPE_ADMOB.OPEN_APP -> {
                AdmobOpenAppAds()
            }

            AdDef.ADS_TYPE_ADMOB.BANNER_COLLAPSIBLE -> {
                AdmobBannerCollapsibleAds()
            }

            else -> {
                null
            }
        }
    }

    fun closeCollapsibleBanner(
        adsChild: AdsChild,
        lifecycleOwner: LifecycleOwner,
        onDone: () -> Unit
    ) {
        val key = adsChild.spaceName.lowercase(Locale.getDefault())
        hashMap[key]?.let {
            (it as AdmobBannerCollapsibleAds).close(lifecycleOwner, onDone)
        } ?: kotlin.run {
            onDone.invoke()
        }
    }

    fun setPreloadCallback(adsChild: AdsChild , preloadCallback : PreloadCallback){
        val key = adsChild.spaceName.lowercase(Locale.getDefault())
        hashMap[key]?.let {
            it.setPreloadCallback(preloadCallback)
        }
    }


}