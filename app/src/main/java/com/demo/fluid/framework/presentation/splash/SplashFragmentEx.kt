package com.demo.fluid.framework.presentation.splash

import android.os.Bundle
import androidx.activity.addCallback
import com.demo.fluid.R
import pion.tech.fluid_wallpaper.framework.database.entities.DummyEntity
import pion.tech.fluid_wallpaper.util.BundleKey

fun SplashFragment.backEvent() {
    activity?.onBackPressedDispatcher?.addCallback(this, true) {
        onBackPressed()
    }
//    binding.btnBack.setPreventDoubleClickScaleView {
//        onBackPressed()
//    }
}

fun SplashFragment.onBackPressed() {
//    findNavController().popBackStack()
}

fun SplashFragment.startAnimation() {
    binding.loadingView.startAnim(2000L) {
        val dummyEntity = DummyEntity(id = 0, value = "Hello Home")

        val bundle = Bundle()
        bundle.putParcelable(BundleKey.KEY_DUMMY_ENTITY, dummyEntity)

        safeNav(R.id.splashFragment, R.id.homeFragment, bundle)
    }
}