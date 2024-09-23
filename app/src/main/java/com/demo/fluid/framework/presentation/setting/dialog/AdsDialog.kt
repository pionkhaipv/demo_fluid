package com.demo.fluid.framework.presentation.setting.dialog

import android.os.Bundle
import com.demo.fluid.R
import com.demo.fluid.databinding.DialogAdsBinding
import com.demo.fluid.framework.presentation.common.BaseDialogFragment

class AdsDialog : BaseDialogFragment<DialogAdsBinding>(R.layout.dialog_ads) {
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        setDialogCanCancel()
    }
}