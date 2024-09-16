package com.demo.fluid.activity.home;

public class HomeModel {
    String ads;
    String title;

    public HomeModel(String str, String str2) {
        this.title = str;
        this.ads = str2;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAds() {
        return this.ads;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public void setAds(String str) {
        this.ads = str;
    }
}
