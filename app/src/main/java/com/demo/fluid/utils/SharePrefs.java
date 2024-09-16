package com.demo.fluid.utils;

import android.content.Context;
import android.content.SharedPreferences;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;


public final class SharePrefs {
    public static final String COOKIES = "Cookies";
    public static final String CSRF = "csrf";
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final String IS_INSTAGRAM_LOGIN = "Is_Instagram_Login";
    
    public static String PREFERENCE = "VIDEODOWNLOADER";
    
    public static String SESSIONID = "session_id";
    
    public static String USERID = "user_id";
    
    public static SharePrefs shareInstance = new SharePrefs();
    private SharedPreferences sharedPreferences;

    public void init(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFERENCE, 0);
    }

    public void putString(String str, String str2) {
        SharedPreferences sharedPreferences2 = this.sharedPreferences;
        sharedPreferences2.edit().putString(str, str2).apply();
    }

    public String getString(String str) {
        SharedPreferences sharedPreferences2 = this.sharedPreferences;
        String string = sharedPreferences2.getString(str, "");
        CharSequence charSequence = string;
        int length = charSequence.length() - 1;
        int i = 0;
        boolean z = false;
        while (i <= length) {
            boolean z2 = Intrinsics.compare((int) charSequence.charAt(!z ? i : length), 32) <= 0;
            if (!z) {
                if (!z2) {
                    z = true;
                } else {
                    i++;
                }
            } else if (!z2) {
                break;
            } else {
                length--;
            }
        }
        return charSequence.subSequence(i, length + 1).toString();
    }

    public int getInt(String str, int i) {
        SharedPreferences sharedPreferences2 = this.sharedPreferences;
        return sharedPreferences2.getInt(str, i);
    }

    public void putInt(String str, int i) {
        SharedPreferences sharedPreferences2 = this.sharedPreferences;
        sharedPreferences2.edit().putInt(str, i).apply();
    }

    public void putBoolean(String str, boolean z) {
        SharedPreferences sharedPreferences2 = this.sharedPreferences;
        sharedPreferences2.edit().putBoolean(str, z).apply();
    }

    public boolean getBoolean(String str) {
        SharedPreferences sharedPreferences2 = this.sharedPreferences;
        return sharedPreferences2.getBoolean(str, false);
    }

    public int getInt(String str) {
        SharedPreferences sharedPreferences2 = this.sharedPreferences;
        return sharedPreferences2.getInt(str, 0);
    }

    public static final class Companion {
        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public String getPREFERENCE() {
            return SharePrefs.PREFERENCE;
        }

        public void setPREFERENCE(String str) {
            SharePrefs.PREFERENCE = str;
        }

        public String getSESSIONID() {
            return SharePrefs.SESSIONID;
        }

        public void setSESSIONID(String str) {
            SharePrefs.SESSIONID = str;
        }

        public String getUSERID() {
            return SharePrefs.USERID;
        }

        public void setUSERID(String str) {
            SharePrefs.USERID = str;
        }

        public SharePrefs getShareInstance() {
            return SharePrefs.shareInstance;
        }

        public void setShareInstance(SharePrefs sharePrefs) {
            SharePrefs.shareInstance = sharePrefs;
        }
    }
}
