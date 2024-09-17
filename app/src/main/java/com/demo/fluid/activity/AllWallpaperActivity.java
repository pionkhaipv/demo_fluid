package com.demo.fluid.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import com.demo.fluid.activity.home.HomeModel;
import com.demo.fluid.activity.home.HomeViewModel;
import com.demo.fluid.activity.main.MainActivity;
import com.demo.fluid.util.Common;
import com.demo.fluid.R;

public class AllWallpaperActivity extends AppCompatActivity {
    HomeViewModel homeViewModel;
    ArrayList<HomeModel> listDataWallpaper;
    WallpaperAdapter wallpaperAdapter;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_all_wallpaper);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorlight));
        }


        //Reguler Banner Ads


        HomeViewModel homeViewModel2 = (HomeViewModel) new ViewModelProvider(this).get(HomeViewModel.class);
        this.homeViewModel = homeViewModel2;
        homeViewModel2.setData();
        WallpaperAdapter wallpaperAdapter2 = new WallpaperAdapter(this, new WallpaperAdapter.OnClickListener() {
            @Override
            public void onClickListener(String str, String str2) {
                Common common = Common.INSTANCE;
                common.setCountSaveSuccess(common.getCountSaveSuccess() + 1);
                Intent intent2 = new Intent(AllWallpaperActivity.this, MainActivity.class);
                intent2.putExtra("nameWallpaper", str);
            }
        });
        this.wallpaperAdapter = wallpaperAdapter2;
        ((RecyclerView) findViewById(R.id.rcvWallpaper)).setAdapter(wallpaperAdapter2);
        if (!(this.homeViewModel == null || HomeViewModel.listDataWallpaper == null || this.wallpaperAdapter == null)) {
            ArrayList<HomeModel> arrayList = HomeViewModel.listDataWallpaper;
            this.listDataWallpaper = arrayList;
            this.wallpaperAdapter.setData(arrayList);
        }
        findViewById(R.id.icBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllWallpaperActivity.this.onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
