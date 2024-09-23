package com.demo.fluid.framework.presentation.setting.dialog

import android.os.Bundle
import com.demo.fluid.R
import com.demo.fluid.databinding.DialogIapBinding
import com.demo.fluid.framework.presentation.common.BaseDialogFragment

class IAPDialog : BaseDialogFragment<DialogIapBinding>(R.layout.dialog_iap) {
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        setDialogCanCancel()
    }
}