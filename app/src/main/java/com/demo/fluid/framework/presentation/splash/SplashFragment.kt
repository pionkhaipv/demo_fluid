package com.demo.fluid.framework.presentation.splash

import android.view.View
import com.demo.fluid.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import com.demo.fluid.framework.presentation.common.BaseFragment


@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding, SplashViewModel>(
    FragmentSplashBinding::inflate,
    SplashViewModel::class.java
) {


    override fun init(view: View) {
        backEvent()
        startAnimation()
    }

    override fun subscribeObserver(view: View) {

    }

}
