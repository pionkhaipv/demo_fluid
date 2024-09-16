package com.demo.fluid.framework

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.demo.fluid.R
import dagger.hilt.android.AndroidEntryPoint
import com.demo.fluid.framework.presentation.common.LoadingDialog
import pion.tech.fluid_wallpaper.framework.presentation.common.lifecycleCallback.FragmentLifecycleCallbacksImpl

@AndroidEntryPoint
class HomeScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacksImpl(), true)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main_screen) //TODO: change name navhost
    }

    fun showLoading() {
        LoadingDialog.getInstance(this)?.show()
    }

    fun hiddenLoading() {
        LoadingDialog.getInstance(this)?.hidden()
    }
}