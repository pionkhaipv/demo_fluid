package com.demo.fluid.framework.presentation.transparentWallpaper.dialog

import android.os.Bundle
import com.demo.fluid.R
import com.demo.fluid.databinding.DialogUnlockTransparentBinding
import com.demo.fluid.framework.presentation.common.BaseDialogFragment
import com.demo.fluid.util.setPreventDoubleClickScaleView

class UnlockTransparentDialog :
    BaseDialogFragment<DialogUnlockTransparentBinding>(R.layout.dialog_unlock_transparent) {
    interface Listener {
        fun onWatchAdsClick()
        fun onBuyVipVersion()
    }

    private var listener: Listener? = null
    fun setListener(listener: Listener) {
        this.listener = listener
    }

    override fun addEvent(savedInstanceState: Bundle?) {
        super.addEvent(savedInstanceState)
        binding.ivClose.setPreventDoubleClickScaleView {
            dismiss()
        }
        binding.btnWatchAds.setPreventDoubleClickScaleView {
            listener?.onWatchAdsClick()
            dismiss()
        }
        binding.btnBuyVip.setPreventDoubleClickScaleView {
            listener?.onBuyVipVersion()
            dismiss()
        }
    }
}