package pion.datlt.libads.admob.ads

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnPaidEventListener
import pion.datlt.libads.AdsController
import pion.datlt.libads.callback.AdCallback
import pion.datlt.libads.callback.PreloadCallback
import pion.datlt.libads.model.AdsChild
import pion.datlt.libads.utils.AdDef
import pion.datlt.libads.utils.AdsConstant
import pion.datlt.libads.utils.CommonUtils
import pion.datlt.libads.utils.StateLoadAd
import java.util.*

class AdmobBannerAdaptiveAds : AdmobAds() {

    private var adView: AdView? = null
    private var mAdCallback: AdCallback? = null
    private var error = ""
    private var stateLoadAd: StateLoadAd = StateLoadAd.NONE
    private var mPreloadCallback: PreloadCallback? = null

    private var mDestinationToShowAds : Int? = null

    var adSourceId = ""
    var adSourceName = ""
    var adUnitId = ""

    override fun loadAndShow(
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
        mAdCallback = adCallback
        mDestinationToShowAds = destinationToShowAds

        if (stateLoadAd == StateLoadAd.LOADING){
            //khong load cai moi nua
            //doi cai cu load xong roi show
        }else{
            //load cai moi
            load(
                activity = activity,
                adsChild = adsChild,
                isPreload = false,
                loadCallback = object : PreloadCallback{
                    override fun onLoadDone() {
                        show(
                            activity = activity,
                            adsChild = adsChild,
                            destinationToShowAds = destinationToShowAds,
                            layoutToAttachAds = layoutToAttachAds,
                            viewAdsInflateFromXml = viewAdsInflateFromXml,
                            lifecycle = lifecycle,
                            adCallback = adCallback
                        )
                    }

                    override fun onLoadFail(error: String) {
                        super.onLoadFail(error)
                        adCallback?.onAdFailToLoad(error)
                    }
                }
            )
        }
    }

    override fun preload(
        activity: Activity,
        adsChild: AdsChild,
        positionCollapsibleBanner: String?,
        adChoice: Int?,
        isOneTimeCollapsible: Boolean?
    ) {
        load(
            activity = activity,
            adsChild = adsChild,
            isPreload = true
        )
    }

    private fun load(
        activity: Activity,
        adsChild: AdsChild,
        isPreload : Boolean,
        loadCallback : PreloadCallback? = null
    ){
        stateLoadAd = StateLoadAd.LOADING
        val idAds = if (AdsConstant.isDebug) AdsConstant.ID_ADMOB_BANNER_ADAPTIVE_TEST else adsChild.adsId


        adView = AdView(activity.applicationContext)
        adView?.setBackgroundColor(Color.WHITE)
        adView?.adUnitId = idAds
        val adSize = getAdsize(activity)
        adView?.setAdSize(adSize)

        adView?.adListener = object : AdListener() {

            override fun onAdOpened() {
                super.onAdOpened()
            }

            override fun onAdClicked() {
                super.onAdClicked()
                    mAdCallback?.onAdClick()
            }

            override fun onAdClosed() {
                super.onAdClosed()
                mAdCallback?.onAdClose()
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                super.onAdFailedToLoad(adError)
                error = adError.message
                stateLoadAd = StateLoadAd.LOAD_FAILED
                mAdCallback?.onAdFailToLoad(error)
                loadCallback?.onLoadFail(error)
                if (isPreload) {
                    mPreloadCallback?.onLoadFail(error)
                }
            }

            override fun onAdLoaded() {
                super.onAdLoaded()

                adView?.let{
                    it.responseInfo?.adapterResponses?.forEach {responseInfo ->
                        if (responseInfo.adSourceId.isNotEmpty()){
                            adSourceId = responseInfo.adSourceId
                        }
                        if (responseInfo.adSourceName.isNotEmpty()){
                            adSourceName = responseInfo.adSourceName
                        }
                    }
                    adUnitId = it.adUnitId
                }

                stateLoadAd = StateLoadAd.SUCCESS
                loadCallback?.onLoadDone()
                timeLoader = Date().time
                if (isPreload){
                    mPreloadCallback?.onLoadDone()
                }
            }
        }

        adView?.onPaidEventListener = OnPaidEventListener { adValue ->
            val bundle = Bundle().apply {
                putString("ad_unit_id" , adUnitId)
                putInt("precision_type" , adValue.precisionType)
                putLong("revenue_micros" , adValue.valueMicros)
                putString("ad_source_id" , adSourceId)
                putString("ad_source_name" , adSourceName)
                putString("ad_type" , AdDef.ADS_TYPE_ADMOB.BANNER_ADAPTIVE)
                putString("currency_code" , adValue.currencyCode)
            }
            mAdCallback?.onPaidEvent(bundle)
        }

        adView?.loadAd(
            AdRequest.Builder().build()
        )
    }

    override fun show(
        activity: Activity,
        adsChild: AdsChild,
        destinationToShowAds: Int?,
        adCallback: AdCallback?,
        lifecycle: Lifecycle?,
        layoutToAttachAds: ViewGroup?,
        viewAdsInflateFromXml: View?
    ) {
        mAdCallback = adCallback
        mDestinationToShowAds = destinationToShowAds

        val adSize = getAdsize(activity)
        layoutToAttachAds?.let { viewG ->
            val lp = viewG.layoutParams
            lp.width = adSize.getWidthInPixels(viewG.context)
            lp.height = adSize.getHeightInPixels(viewG.context)
            viewG.layoutParams = lp
        }

        if (adView != null && layoutToAttachAds != null){
            if (mDestinationToShowAds != null && mDestinationToShowAds != AdsController.currentDestinationId){
                adCallback?.onAdFailToLoad("show in wrong destination")
            }else{
                layoutToAttachAds.removeAllViews()
                if (adView!!.parent != null) {
                    (adView!!.parent as ViewGroup).removeView(adView)
                }
                layoutToAttachAds.addView(adView)
                stateLoadAd = StateLoadAd.HAS_BEEN_OPENED
                CommonUtils.showToastDebug(activity, "Admob banner adaptive id: ${adsChild.adsId}")
                mAdCallback?.onAdShow()
            }
        }else{
            adCallback?.onAdFailToLoad("layout null")
        }
    }

    override fun setPreloadCallback(preloadCallback: PreloadCallback?) {
        mPreloadCallback = preloadCallback
    }

    override fun getStateLoadAd(): StateLoadAd {
        return stateLoadAd
    }


    private fun getAdsize(activity: Activity): AdSize {
        val display = activity.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels.toFloat()
        val density = outMetrics.density
        val adWidth = (widthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
            activity,
            adWidth
        )
    }
}