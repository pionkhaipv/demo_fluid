package com.demo.fluid.activity.main;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;
import com.demo.fluid.utils.Common;

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //getWindow().getDecorView().setSystemUiVisibility(5894);
        Locale locale = new Locale(Common.INSTANCE.getLang(this));
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(new Locale("en"));
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    @Override
    public void onResume() {
        super.onResume();
        Locale locale = new Locale(Common.INSTANCE.getLang(this));
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(new Locale("en"));
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    @Override
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        //getWindow().getDecorView().setSystemUiVisibility(5894);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        //getWindow().getDecorView().setSystemUiVisibility(5894);
        return super.dispatchTouchEvent(motionEvent);
    }
}
