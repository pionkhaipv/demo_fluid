package com.demo.fluid.activity.main;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.demo.fluid.R;
import com.demo.fluid.databinding.ActivityThemeApplyBinding;

public final class ThemeApplyActivity extends AppCompatActivity {
    private ActivityThemeApplyBinding binding;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ViewDataBinding contentView = DataBindingUtil.setContentView(this, R.layout.activity_theme_apply);
        ActivityThemeApplyBinding activityThemeApplyBinding = (ActivityThemeApplyBinding) contentView;
        this.binding = activityThemeApplyBinding;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorlight));
        }



        //Reguler Banner Ads

        if (activityThemeApplyBinding == null) {
            activityThemeApplyBinding = null;
        }
        activityThemeApplyBinding.btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
