package com.demo.fluid.framework.presentation.setting.dialog

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.util.DisplayMetrics
import android.view.WindowManager
import com.demo.fluid.BuildConfig
import com.demo.fluid.R
import com.demo.fluid.databinding.DialogFeedbackBinding
import com.demo.fluid.framework.presentation.common.BaseDialogFragment
import com.demo.fluid.util.displayToast
import com.demo.fluid.util.setPreventDoubleClickScaleView
import java.util.*

class FeedbackDialog : BaseDialogFragment<DialogFeedbackBinding>(R.layout.dialog_feedback) {

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        setDialogCanCancel()
    }
    override fun addEvent(savedInstanceState: Bundle?) {
        super.addEvent(savedInstanceState)
        binding.tvSubmit.setPreventDoubleClickScaleView {
            var textContent = ""
            if (binding.radioUnableToSaveImage.isChecked) {
                textContent = binding.radioUnableToSaveImage.text.toString()
            }
            if (binding.radioSharp.isChecked) {
                textContent = binding.radioSharp.text.toString()
            }
            if (binding.radioOthers.isChecked) {
                textContent = binding.radioOthers.text.toString()
            }
            if (textContent.isBlank()) {
                return@setPreventDoubleClickScaleView
            }
            textContent =
                textContent + "\n\n" + "DEVICE INFORMATION (Device information is useful for application improvement and development)" + "\n\n" + getDeviceInformation()
            sendEmail(
                "piontech.feedback@gmail.com",
                "Fluid Wallpaper ${BuildConfig.VERSION_NAME} Rate Feedback",
                textContent
            )
        }
    }

    private fun getDeviceInformation(): String {
        val manufacturer = Build.MANUFACTURER // Nhà sản xuất (Manufacturer)
        val model = Build.MODEL // Model của thiết bị
        val osVersion = Build.VERSION.RELEASE // Phiên bản hệ điều hành (OS version)
        val displayMetrics = DisplayMetrics()
        val windowManager =
            requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        val screenSize = "${screenWidth}x${screenHeight}" // Độ phân giải màn hình
        val timeZone = TimeZone.getDefault().id // Múi giờ của thiết bị

        // Lấy thông tin về không gian trống của thiết bị
        val stat = StatFs(Environment.getDataDirectory().path)
        val bytesAvailable = stat.availableBytes // Số byte có sẵn trong phân vùng dữ liệu
        val megabytesAvailable = bytesAvailable / (1024 * 1024) // Chuyển đổi thành đơn vị MB

        // Tạo chuỗi thông tin thiết bị
        val deviceInformation = StringBuilder()
        deviceInformation.append("Manufacturer: $manufacturer\n")
        deviceInformation.append("Model: $model\n")
        deviceInformation.append("OS Version: $osVersion\n")
        deviceInformation.append("Screen Resolution: $screenSize\n")
        deviceInformation.append("Free space: $megabytesAvailable MB\n")
        deviceInformation.append("TimeZone: $timeZone\n")

        return deviceInformation.toString()
    }

    private fun sendEmail(email: String, title: String, content: String) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:") // Sử dụng URI "mailto:" để chỉ định gửi email
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email)) // Thiết lập địa chỉ email
                putExtra(Intent.EXTRA_SUBJECT, title) // Thiết lập tiêu đề email
                putExtra(Intent.EXTRA_TEXT, content) // Thiết lập nội dung email
            }

            startActivity(intent)
        } catch (e: Exception) {
            displayToast(getString(R.string.something_error))
        }

    }
}