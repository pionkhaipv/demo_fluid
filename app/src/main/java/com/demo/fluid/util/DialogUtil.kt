package pion.tech.fluid_wallpaper.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.demo.fluid.R
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