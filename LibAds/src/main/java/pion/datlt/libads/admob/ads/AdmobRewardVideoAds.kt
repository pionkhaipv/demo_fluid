package pion.datlt.libads.admob.ads

import android.app.Activity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnPaidEventListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import pion.datlt.libads.AdsController
import pion.datlt.libads.callback.AdCallback
import pion.datlt.libads.callback.PreloadCallback
import pion.datlt.libads.model.AdsChild
import pion.datlt.libads.utils.AdDef
import pion.datlt.libads.utils.AdsConstant
import pion.datlt.libads.utils.CommonUtils
import pion.datlt.libads.utils.StateLoadAd
import java.util.*

class AdmobRewardVideoAds : AdmobAds() {

    private var stateLoadAd: StateLoadAd = StateLoadAd.NONE
    private var rewardedAd : RewardedAd? = null
    private var isTimeOut: Boolean = false

    private var eventLifecycle: Lifecycle.Event = Lifecycle.Event.ON_RESUME
    private var error = ""

    private var mActivity: Activity? = null
    private var mAdsChild: AdsChild? = null

    private var mDestinationToShowAds : Int? = null

    private var mPreloadCallback : PreloadCallback? = null
    private var mAdCallback : AdCallback? = null

    //bundle
    private var adSourceId = ""
    private var adSourceName = ""
    private var adUnitId = ""

    private val handler = Handler(Looper.getMainLooper())

    private var mLifecycle : Lifecycle? = null

    private val countDownShowAds = object : CountDownTimer(4000L , 4000L) {
        override fun onTick(p0: Long) {

        }

        override fun onFinish() {
            rewardedAd = null
            stateLoadAd = StateLoadAd.SHOW_FAILED
            error = "timeout show ads"
            if (eventLifecycle == Lifecycle.Event.ON_RESUME) {
                mAdCallback?.onAdFailToLoad(error)
                mLifecycle?.removeObserver(lifecycleObserver)
            }
        }

    }

    private val timeoutCallback = Runnable {
        if (
            stateLoadAd != StateLoadAd.SUCCESS
            && stateLoadAd != StateLoadAd.LOAD_FAILED
            && stateLoadAd != StateLoadAd.SHOW_FAILED
        ){
            //none hoac loading
            isTimeOut = true
            if (eventLifecycle == Lifecycle.Event.ON_RESUME){
                mAdCallback?.onAdFailToLoad("TimeOut")
                mLifecycle?.removeObserver(lifecycleObserver)
            }
        }
    }

    private val lifecycleObserver = object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            eventLifecycle = event
            if (event == Lifecycle.Event.ON_RESUME){
                if (isTimeOut){
                    mLifecycle?.removeObserver(this)
                    mAdCallback?.onAdFailToLoad("TimeOut")
                }else if (stateLoadAd == StateLoadAd.SUCCESS){
                    if(mActivity != null && mAdsChild != null){
                        show(
                            activity = mActivity!!,
                            adsChild = mAdsChild!!,
                            destinationToShowAds = mDestinationToShowAds,
                            adCallback = mAdCallback,
                            lifecycle = mLifecycle,
                            layoutToAttachAds = null,
                            viewAdsInflateFromXml = null)
                    }else{
                        mLifecycle?.removeObserver(this)
                        mAdCallback?.onAdFailToLoad("activity or adsChild must not null")
                    }
                }else {
                    mLifecycle?.removeObserver(this)
                    mAdCallback?.onAdFailToLoad(error)
                }
            }
        }
    }

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
        if (stateLoadAd == StateLoadAd.LOADING){
            //không load cái mới nữa
            //chờ load xong rồi show
        }else if (stateLoadAd == StateLoadAd.SUCCESS){
            //show luôn
            show(
                activity = activity,
                adsChild = adsChild,
                destinationToShowAds = destinationToShowAds,
                adCallback = adCallback,
                lifecycle = lifecycle,
                layoutToAttachAds = layoutToAttachAds,
                viewAdsInflateFromXml = viewAdsInflateFromXml)
        }else{
            //load quảng cáo mới
            load(
                activity = activity,
                adsChild = adsChild,
                isPreload = false,
                loadCallback = object : PreloadCallback{
                    override fun onLoadDone() {
                        //show luon
                        show(
                            activity = activity,
                            adsChild = adsChild,
                            destinationToShowAds = destinationToShowAds,
                            adCallback = adCallback,
                            lifecycle = lifecycle,
                            layoutToAttachAds = layoutToAttachAds,
                            viewAdsInflateFromXml = viewAdsInflateFromXml)
                    }

                    override fun onLoadFail(error: String) {
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
        activity : Activity,
        adsChild: AdsChild,
        isPreload: Boolean,
        timeout: Long = AdsConstant.TIME_OUT_DEFAULT,
        loadCallback: PreloadCallback? = null
    ){
        stateLoadAd = StateLoadAd.LOADING
        val id = if (AdsConstant.isDebug) AdsConstant.ID_ADMOB_REWARD_TEST else adsChild.adsId

        if (!isPreload){
            handler.removeCallbacks(timeoutCallback)
            handler.postDelayed(timeoutCallback , timeout)
        }


        val rewardAdCallback = object : RewardedAdLoadCallback() {
            override fun onAdLoaded(ads: RewardedAd) {
                super.onAdLoaded(ads)
                rewardedAd = ads
                timeLoader = Date().time
                stateLoadAd = StateLoadAd.SUCCESS
                loadCallback?.onLoadDone()
                handler.removeCallbacks(timeoutCallback)
                if (isPreload){
                    mPreloadCallback?.onLoadDone()
                }
            }

            override fun onAdFailedToLoad(loadError: LoadAdError) {
                super.onAdFailedToLoad(loadError)
                error = loadError.message
                stateLoadAd = StateLoadAd.LOAD_FAILED
                loadCallback?.onLoadFail(loadError.message)
                handler.removeCallbacks(timeoutCallback)
                if (isPreload) {
                    mPreloadCallback?.onLoadFail(loadError.message)
                }
            }
        }
        val request = AdRequest.Builder().build()
        RewardedAd.load(
            activity, id, request, rewardAdCallback
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
        mActivity = activity
        mAdsChild = adsChild
        mDestinationToShowAds = destinationToShowAds
        mAdCallback = adCallback
        mLifecycle = lifecycle

        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback(){

            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                countDownShowAds.cancel()
                rewardedAd = null
                adCallback?.onAdClose()
                lifecycle?.removeObserver(lifecycleObserver)
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                //goi khi quang cao duoc show len
                countDownShowAds.cancel()
                rewardedAd = null
                stateLoadAd = StateLoadAd.HAS_BEEN_OPENED
                lifecycle?.removeObserver(lifecycleObserver)
                CommonUtils.showToastDebug(activity, "Admob Reward id: ${adsChild.adsId}")
                adCallback?.onAdShow()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                super.onAdFailedToShowFullScreenContent(adError)
                countDownShowAds.cancel()
                rewardedAd = null
                stateLoadAd = StateLoadAd.SHOW_FAILED
                error = adError.message
                if (eventLifecycle == Lifecycle.Event.ON_RESUME) {
                    adCallback?.onAdFailToLoad(adError.message)
                    lifecycle?.removeObserver(lifecycleObserver)
                }
            }

            override fun onAdClicked() {
                super.onAdClicked()
                adCallback?.onAdClick()
            }


            override fun onAdImpression() {
                super.onAdImpression()
            }

        }

        rewardedAd?.onPaidEventListener = OnPaidEventListener { adValue ->
            val bundle = Bundle().apply {
                putString("ad_unit_id" , adUnitId)
                putInt("precision_type" , adValue.precisionType)
                putLong("revenue_micros" , adValue.valueMicros)
                putString("ad_source_id" , adSourceId)
                putString("ad_source_name" , adSourceName)
                putString("ad_type" , AdDef.ADS_TYPE_ADMOB.REWARD_VIDEO)
                putString("currency_code" , adValue.currencyCode)
            }
            adCallback?.onPaidEvent(bundle)
        }

        lifecycle?.let {
            if (lifecycle.currentState != Lifecycle.State.RESUMED) {
                lifecycle.removeObserver(lifecycleObserver)
                lifecycle.addObserver(lifecycleObserver)
            }
        }

        if (eventLifecycle == Lifecycle.Event.ON_RESUME && !isTimeOut) {
            if (mDestinationToShowAds != null && mDestinationToShowAds != AdsController.currentDestinationId) {
                adCallback?.onAdFailToLoad("show in wrong destination")
            } else if (!wasLoadTimeLessThanNHoursAgo()){
                adCallback?.onAdFailToLoad("ads expired")
            } else {
                rewardedAd?.show(activity) {
                    adCallback?.onGotReward()
                }
                //bat dau dem nguoc
                countDownShowAds.start()
            }
        }
    }

    override fun setPreloadCallback(preloadCallback: PreloadCallback?) {
        mPreloadCallback = preloadCallback
    }

    override fun getStateLoadAd(): StateLoadAd {
        return stateLoadAd
    }
}