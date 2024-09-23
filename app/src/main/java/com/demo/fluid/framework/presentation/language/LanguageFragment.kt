package com.demo.fluid.framework.presentation.language

import android.view.View
import com.demo.fluid.databinding.FragmentLanguageBinding
import com.demo.fluid.framework.presentation.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import com.demo.fluid.framework.presentation.language.adapter.LanguageAdapter
import com.demo.fluid.util.show
import pion.datlt.libads.utils.AdsConstant

@AndroidEntryPoint
class LanguageFragment : BaseFragment<FragmentLanguageBinding, LanguageViewModel>(
    FragmentLanguageBinding::inflate,
    LanguageViewModel::class.java
) {

    var inputLanguage = "en"
    var currentLanguage = "en"

    var isClickAds = false
    var isShowReloadInter = false

    val adapter = LanguageAdapter {
        onSelectLanguage(it)
        binding.btnOk.show()
        if (AdsConstant.listConfigAds["language1.1"]?.isOn == true) {
            showReloadNativeAds()
        }
    }

    override fun init(view: View) {
        backEvent()
        getCurrentLanguage()
        setUpRecycleViewLanguage()
        chooseLanguageEvent()
        preloadOnBoardAds()
        showNativeAds()
    }

    override fun onResume() {
        super.onResume()
        if (isClickAds) {
            showReloadNativeAds()
        }
    }

    override fun subscribeObserver(view: View) {
    }


}