package com.demo.fluid.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Common {
    public static final Common INSTANCE = new Common();
    private static int countSaveSuccess = 0;
    private static String nameWallpaper = "";
    private static String filePathTextView = "";

    private Common() {
    }

    public int getCountSaveSuccess() {
        return countSaveSuccess;
    }

    public void setCountSaveSuccess(int i) {
        countSaveSuccess = i;
    }

    public String getNameWallpaper() {
        return nameWallpaper;
    }

    public String getFilePathTextView() {
        return filePathTextView;
    }

    public void setNameWallpaper(String str) {
        nameWallpaper = str;
    }

    public void setFilePathTextView(String str) {
        filePathTextView = str;
    }

    public String getNameWallpaper(Context context) {
        return context.getSharedPreferences(context.getPackageName(), 4).getString("KEY_nameWallpaper", "AbstractAdventure");
    }

    public void setNameWallpaper(Context context, String str) {
        SharedPreferences.Editor edit = context.getSharedPreferences(context.getPackageName(), 4).edit();
        edit.putString("KEY_nameWallpaper", str);
        edit.apply();
    }

    public String getLang(Context context) {
        return context.getSharedPreferences(context.getPackageName(), 4).getString("KEY_LANG", "en");
    }

    public void setLang(Context context, String str) {
        SharedPreferences.Editor edit = context.getSharedPreferences(context.getPackageName(), 4).edit();
        edit.putString("KEY_LANG", str);
        edit.apply();
    }

}
