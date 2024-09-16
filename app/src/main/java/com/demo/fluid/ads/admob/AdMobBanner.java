package com.demo.fluid.ads.admob;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class AdMobBanner {

    private Activity activity;
    private adMobSmallAdCallback listener;
    private RelativeLayout admob_banner;
    String TAG = "BannerAdClass";
    LinearLayout adContainer;
    FrameLayout qureka;
    String type;

    public interface adMobSmallAdCallback {
        void onAdLoaded();

        void onAdError(String error);
    }

    public void showAd(Activity context, RelativeLayout admob_banner, LinearLayout madContainer, FrameLayout mqureka, String mtype, adMobSmallAdCallback adMobSmallAdCallback) {
        this.activity = context;
        this.listener = adMobSmallAdCallback;
        this.admob_banner = admob_banner;
        this.adContainer = madContainer;
        this.qureka = mqureka;
        this.type = mtype;
        if (!isOnline()) {
            listener.onAdError("No Internet Connection");
            return;
        }
    }

    public boolean isOnline() {
        NetworkInfo netInfo = ((ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (netInfo == null || !netInfo.isConnectedOrConnecting()) {
            return false;
        }
        return true;
    }

}
