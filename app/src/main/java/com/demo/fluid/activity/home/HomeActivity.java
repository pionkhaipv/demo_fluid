package com.demo.fluid.activity.home;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.lifecycle.ViewModelProvider;
import java.util.Timer;
import java.util.TimerTask;

import com.demo.fluid.activity.AllWallpaperActivity;

import com.demo.fluid.adapter.AdapterHome;
import com.demo.fluid.activity.main.BaseActivity;
import com.demo.fluid.activity.main.MainActivity;
import com.demo.fluid.utils.CenterZoomLinearLayoutManager;
import com.demo.fluid.utils.Common;
import com.demo.fluid.R;
import com.demo.fluid.databinding.ActivityHomeBinding;
import com.magicfluids.NativeInterface;

public class HomeActivity extends BaseActivity {
    private AdapterHome adapterHome;
    public final Handler autoScrollHandler = new Handler();
    private Timer autoScrollTimer;
    private ActivityHomeBinding binding;
    private HomeViewModel homeViewModel;

    public CenterZoomLinearLayoutManager layoutManager1;

    public CenterZoomLinearLayoutManager layoutManager2;

    @Override
    public void onCreate(Bundle bundle) {
        AdapterHome adapterHome2;
        super.onCreate(bundle);
        ActivityHomeBinding inflate = ActivityHomeBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView(inflate.getRoot());
        NativeInterface.init();
        NativeInterface abc = new NativeInterface();
        int hello = abc.getID();
        Log.d("asgaggwaagwwag", "onCreate: "+hello);
        abc.onCreate(300, 200, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorlight));
        }


        //Reguler Banner Ads

        //Small Native Ads


        this.layoutManager1 = new CenterZoomLinearLayoutManager(getApplicationContext(), 0, false);
        this.layoutManager2 = new CenterZoomLinearLayoutManager(getApplicationContext(), 0, false);
        HomeViewModel homeViewModel2 = (HomeViewModel) new ViewModelProvider(this).get(HomeViewModel.class);
        this.homeViewModel = homeViewModel2;
        homeViewModel2.setData();
        this.adapterHome = new AdapterHome(this, new AdapterHome.OnClickListener() {
            @Override
            public void onClickListener(String str, String str2) {
                Common common = Common.INSTANCE;
                common.setCountSaveSuccess(common.getCountSaveSuccess() + 1);

                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.putExtra("nameWallpaper", str);
                startActivity(intent);
//                AdsCommon.InterstitialAd(HomeActivity.this, intent);
            }
        });
        this.binding.rcvTheme.setLayoutManager(this.layoutManager1);
        this.binding.rcvTheme.setAdapter(this.adapterHome);
        this.binding.rcvTrending.setLayoutManager(this.layoutManager2);
        this.binding.rcvTrending.setAdapter(this.adapterHome);
        this.binding.rcvTheme.scrollToPosition(this.adapterHome.getItemCount() - 1);
        if (!(this.homeViewModel == null || HomeViewModel.listDataWallpaper == null || (adapterHome2 = this.adapterHome) == null)) {
            adapterHome2.setData(HomeViewModel.listDataWallpaper);
        }
        this.layoutManager1.scrollToPositionWithOffset(1, 0);
        this.layoutManager2.scrollToPositionWithOffset(15, 0);
        this.binding.btnLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        this.binding.seeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AllWallpaperActivity.class);
                startActivity(intent);
            }
        });
        this.binding.seealltrending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AllWallpaperActivity.class);
                startActivity(intent);
            }
        });
        startAutoScroll();
    }


    private void startAutoScroll() {
        if (this.layoutManager1 == null) {
            this.layoutManager1 = new CenterZoomLinearLayoutManager(getApplicationContext(), 0, false);
        }
        if (this.layoutManager2 == null) {
            this.layoutManager2 = new CenterZoomLinearLayoutManager(getApplicationContext(), 0, false);
        }
        Timer timer = new Timer();
        this.autoScrollTimer = timer;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                HomeActivity.this.autoScrollHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        int childCount = HomeActivity.this.layoutManager1.getChildCount();
                        int itemCount = HomeActivity.this.layoutManager1.getItemCount();
                        int findFirstVisibleItemPosition = HomeActivity.this.layoutManager1.findFirstVisibleItemPosition();
                        if (childCount + findFirstVisibleItemPosition < itemCount) {
                            HomeActivity.this.layoutManager1.scrollToPositionWithOffset(findFirstVisibleItemPosition + 1, 0);
                        } else {
                            HomeActivity.this.layoutManager1.scrollToPositionWithOffset(0, 0);
                        }
                        int childCount2 = HomeActivity.this.layoutManager2.getChildCount();
                        int itemCount2 = HomeActivity.this.layoutManager2.getItemCount();
                        int findFirstVisibleItemPosition2 = HomeActivity.this.layoutManager2.findFirstVisibleItemPosition();
                        if (childCount2 + findFirstVisibleItemPosition2 < itemCount2) {
                            HomeActivity.this.layoutManager2.scrollToPositionWithOffset(findFirstVisibleItemPosition2 + 1, 0);
                        } else {
                            HomeActivity.this.layoutManager2.scrollToPositionWithOffset(0, 0);
                        }
                    }
                });
            }
        }, 3000, 4000);
    }

    private void stopAutoScroll() {
        Timer timer = this.autoScrollTimer;
        if (timer != null) {
            timer.cancel();
            this.autoScrollTimer.purge();
            this.autoScrollTimer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAutoScroll();
    }

    @Override
    public void onBackPressed() {
        ExitDialog();
    }

    private void ExitDialog() {

        final Dialog dialog = new Dialog(HomeActivity.this, R.style.DialogTheme);
        dialog.setContentView(R.layout.popup_exit_dialog);
        dialog.setCancelable(false);

        RelativeLayout no = (RelativeLayout) dialog.findViewById(R.id.no);
        RelativeLayout rate = (RelativeLayout) dialog.findViewById(R.id.rate);
        RelativeLayout yes = (RelativeLayout) dialog.findViewById(R.id.yes);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String rateapp = getPackageName();
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + rateapp));
                startActivity(intent1);
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
                System.exit(0);
                //Intent intent = new Intent(AppMainHomeActivity.this, AppThankYouActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //AdsCommon.InterstitialAd(AppMainHomeActivity.this, intent);
            }
        });

        dialog.show();
    }
}
