package com.demo.fluid.framework.presentation.splash

import android.os.Handler
import android.view.View
import com.demo.fluid.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import com.demo.fluid.framework.presentation.common.BaseFragment
import com.demo.fluid.util.haveNetworkConnection


@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding, SplashViewModel>(
    FragmentSplashBinding::inflate,
    SplashViewModel::class.java
) {


    override fun init(view: View) {
        if (context?.haveNetworkConnection() == true) {
            checkIap()
        } else {
            Handler().postDelayed({
                goToNextScreen()
            }, 2000L)
        }
        backEvent()
        startAnimation()
    }

    override fun subscribeObserver(view: View) {

    }

}
