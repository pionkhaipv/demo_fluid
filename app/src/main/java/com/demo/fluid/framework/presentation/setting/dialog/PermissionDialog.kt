package com.demo.fluid.framework.presentation.setting.dialog

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.demo.fluid.R
import com.demo.fluid.databinding.DialogPermissionBinding
import com.demo.fluid.framework.presentation.common.BaseDialogFragment
import com.demo.fluid.util.setPreventDoubleClickScaleView
import com.permissionx.guolindev.PermissionX

class PermissionDialog : BaseDialogFragment<DialogPermissionBinding>(R.layout.dialog_permission) {
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        setDialogCanCancel()
        setUpView()
    }

    private fun setUpView() {
        if (isPermissionGranted(Manifest.permission.CAMERA)) {
            binding.tvCameraTop.setTextColor(Color.parseColor("#FFD656"))
            binding.tvCameraBottom.setTextColor(Color.WHITE)
        } else {
            binding.tvCameraTop.setTextColor(Color.parseColor("#907D55"))
            binding.tvCameraBottom.setTextColor(Color.parseColor("#ADADAD"))
        }
    }

    override fun addEvent(savedInstanceState: Bundle?) {
        super.addEvent(savedInstanceState)
        binding.clPermissionCamera.setPreventDoubleClickScaleView {
            PermissionX.init(this)
                .permissions(
                    Manifest.permission.CAMERA,
                )
                .request { _, _, _ ->
                    setUpView()
                }
        }
    }

    private fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}

