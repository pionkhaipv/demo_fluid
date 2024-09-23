package com.demo.fluid.framework.presentation.setting

import android.view.View
import com.demo.fluid.databinding.FragmentSettingBinding
import com.demo.fluid.framework.presentation.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding, SettingViewModel>(
    FragmentSettingBinding::inflate,
    SettingViewModel::class.java
) {

    override fun init(view: View) {
        onBackEvent()
        openLanguageScreenEven()
        showVersionName()
        onDeveloperEvent()
        onIAPEvent()
        onAdsEvent()
        onFeedbackEvent()
        onPermissionEvent()
        onPolicyEvent()
        gdprEvent()
        resetGDPR()
        resetIapEvent()
    }

    override fun subscribeObserver(view: View) {
    }

}