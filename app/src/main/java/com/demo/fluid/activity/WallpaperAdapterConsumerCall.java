package com.demo.fluid.activity;

import io.reactivex.rxjava3.functions.Consumer;
import kotlin.Unit;
import com.demo.fluid.activity.home.HomeModel;

public final class WallpaperAdapterConsumerCall implements Consumer {
    public final WallpaperAdapter walladp1;
    public final HomeModel homemod1;

    public WallpaperAdapterConsumerCall(WallpaperAdapter wallpaperAdapter, HomeModel homeModel) {
        this.walladp1 = wallpaperAdapter;
        this.homemod1 = homeModel;
    }

    @Override
    public void accept(Object obj) throws Throwable {
        this.walladp1.WallpaperAdapterConsumerCall1(this.homemod1, (Unit) obj);
    }
}
