package com.demo.fluid.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.demo.fluid.R
import com.demo.fluid.databinding.DialogChangingLanguageBinding
import com.demo.fluid.databinding.DialogDemoBinding

fun Context.showDemoDialog(
    lifecycle: Lifecycle,
    onClose: () -> Unit
){
    val dialog = Dialog(this)
    val view: View = LayoutInflater.from(this).inflate(R.layout.dialog_demo, null)
    dialog.setContentView(view)
    dialog.setCancelable(false)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    val binding = DialogDemoBinding.bind(view)

    lifecycle.addObserver(LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_PAUSE -> {
                dialog.dismiss()
            }
            else -> {

            }
        }
    })

    binding.apply {
        binding.btnClose.setOnClickListener {
            dialog.dismiss()
            onClose.invoke()
        }
    }


    if (!dialog.isShowing) {
        dialog.show()
    }
}

fun Context.showDialogChangingLanguage(
    lifecycle: Lifecycle
): Dialog {
    val dialog = Dialog(this)
    val view: View = LayoutInflater.from(this).inflate(R.layout.dialog_changing_language, null)
    dialog.setContentView(view)
    dialog.setCancelable(false)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.window?.setGravity(Gravity.BOTTOM)
    dialog.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    val binding = DialogChangingLanguageBinding.bind(view)
    lifecycle.addObserver(LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_STOP -> {
                dialog.dismiss()
            }

            else -> {

            }
        }
    })


    if (!dialog.isShowing) {
        dialog.show()
    }
    return dialog
}
