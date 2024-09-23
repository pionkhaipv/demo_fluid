package pion.datlt.libads.admob.ads

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import pion.datlt.libads.callback.AdCallback
import pion.datlt.libads.callback.PreloadCallback
import pion.datlt.libads.model.AdsChild
import pion.datlt.libads.utils.StateLoadAd
import java.util.*

/**
 * Class thực hiện các hành động load và show quảng cáo Admob.
 * Tất cả các quảng cáo được load, hay preload sẽ được lưu trữ ở đây
 */
abstract class AdmobAds {

    protected var timeLoader = 0L //thoi gian load success


    abstract fun loadAndShow(
        activity : Activity,
        adsChild : AdsChild,
        destinationToShowAds : Int?,
        adCallback: AdCallback?,
        lifecycle: Lifecycle?,
        timeout: Long?,
        layoutToAttachAds: ViewGroup?,
        viewAdsInflateFromXml: View?,
        adChoice: Int?,
        positionCollapsibleBanner: String?,
        isOneTimeCollapsible : Boolean?
    )

    abstract fun preload(
        activity: Activity,
        adsChild: AdsChild,
        positionCollapsibleBanner: String?,
        adChoice: Int?,
        isOneTimeCollapsible: Boolean?
    )

    abstract fun show(
        activity: Activity,
        adsChild: AdsChild,
        destinationToShowAds : Int?,
        adCallback: AdCallback?,
        lifecycle: Lifecycle?,
        //native
        layoutToAttachAds: ViewGroup?,
        viewAdsInflateFromXml: View?,
    )

    abstract fun setPreloadCallback(preloadCallback: PreloadCallback?)

    abstract fun getStateLoadAd(): StateLoadAd

    fun wasLoadTimeLessThanNHoursAgo(numHours: Long = 1): Boolean {
        val dateDifference: Long = Date().time - timeLoader
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

}