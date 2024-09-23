package com.demo.fluid.framework.presentation.setting.dialog

import android.os.Bundle
import com.demo.fluid.R
import com.demo.fluid.databinding.DialogDeveloperBinding
import com.demo.fluid.framework.presentation.common.BaseDialogFragment

class DeveloperDialog : BaseDialogFragment<DialogDeveloperBinding>(R.layout.dialog_developer) {
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        setDialogCanCancel()
    }
}