package com.demo.fluid.framework.presentation.edit_fluid

import android.annotation.SuppressLint
import android.app.Activity
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.demo.fluid.R
import com.demo.fluid.framework.database.entities.TextViewData
import com.demo.fluid.framework.database.entities.WallpaperName
import com.demo.fluid.framework.database.entities.WallpaperWithTextView
import com.demo.fluid.framework.presentation.addTextFluid.AddTextFluidActivity
import com.demo.fluid.framework.presentation.addTextFluid.getTypeFaceMap
import com.demo.fluid.framework.presentation.onScreenFluid.OnScreenActivity
import com.demo.fluid.framework.presentation.previewFluid.PreviewFluidActivity
import com.demo.fluid.framework.presentation.wallpaper_service.NewWallpaperService
import com.demo.fluid.util.BundleKey
import com.demo.fluid.util.Common
import com.demo.fluid.util.Constant
import com.demo.fluid.util.displayToast
import com.demo.fluid.util.getBitmapFromView
import com.demo.fluid.util.gl.GLES20Renderer
import com.demo.fluid.util.gl.OrientationSensor
import com.demo.fluid.util.gl.SettingsStorage
import com.demo.fluid.util.saveBitmapToFile
import com.demo.fluid.util.setPreventDoubleClickScaleView
import com.magicfluids.Config
import pion.tech.fluid_wallpaper.util.parcelable
import pion.tech.fluid_wallpaper.util.safeDelay

fun EditFluidFragment.initView() {

    nameWallpaper = arguments?.getString(BundleKey.KEY_FLUID_NAME_EDIT) ?: "AbstractAdventure"
    wallpaperWithTextView = arguments?.parcelable(BundleKey.KEY_WALLPAPER_WITH_TEXTVIEW)

    if (nameWallpaper == null && wallpaperWithTextView == null) {
        displayToast(R.string.something_error)
        findNavController().navigateUp()
    }

    if (wallpaperWithTextView != null) {
        Constant.textViewList.clear()
        nameWallpaper = wallpaperWithTextView!!.wallpaperName.nameWallpaper
        for (item in wallpaperWithTextView!!.listTextViewData) {
            val textView = TextView(requireContext())
            textView.text = item.text
            textView.x = item.positionX
            textView.y = item.positionY
            textView.setTextColor(item.color)
            textView.typeface = ResourcesCompat.getFont(requireContext(), item.typeFaceSource)
            textView.textSize = item.textSize / 2
            Constant.textViewList.add(textView)
            setUpViewForListTextView()
        }
    }
    binding.cvMain.post {
        adjustSurfaceViewSizeWithActionBar(activity = requireActivity(), view = binding.cvMain)
    }

}

fun EditFluidFragment.setUpViewForListTextView() {
    binding.tvSave.isVisible = Constant.textViewList.isNotEmpty()
    binding.flContainer.removeAllViews()
    binding.flContainer.post {
        for (item in Constant.textViewList) {
            val textView = TextView(requireContext())
            textView.text = item.text
            textView.x = item.x
            textView.y = item.y
            textView.setTextColor(item.currentTextColor)
            textView.typeface = item.typeface
            textView.textSize = item.textSize/2
            val parent = textView.parent as? ViewGroup
            parent?.removeView(textView)
            binding.flContainer.addView(textView)
        }
    }
}

fun adjustSurfaceViewSizeWithActionBar(activity: Activity, view: View) {
    // Lấy thông tin về kích thước màn hình
    val displayMetrics = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(displayMetrics)

    // Chiều rộng màn hình (bao gồm cả action bar và toolbar)
    val screenWidth = displayMetrics.widthPixels

    // Tính tổng chiều cao của màn hình (bao gồm cả thanh trạng thái và thanh điều hướng)
    val screenHeight: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        // API 30 trở lên: sử dụng WindowInsets
        val windowInsets = activity.windowManager.currentWindowMetrics.windowInsets
        val insets = windowInsets.getInsets(WindowInsets.Type.systemBars())
        displayMetrics.heightPixels + insets.top + insets.bottom
    } else {
        // Trước API 30: sử dụng kích thước màn hình chính
        val size = Point()
        activity.windowManager.defaultDisplay.getRealSize(size)
        size.y
    }

    // Tính tỷ lệ của màn hình
    val aspectRatio = screenWidth.toFloat() / screenHeight.toFloat()

    // Lấy chiều cao hiện tại của cvMain
    val currentHeight = view.height

    // Tính chiều rộng mới để giữ tỷ lệ
    val newWidth = (currentHeight * aspectRatio).toInt()

    // Set layout params của SurfaceView để giữ tỷ lệ giống với màn hình (bao gồm action bar)
    val layoutParams = view.layoutParams
    layoutParams.width = newWidth // Chỉ điều chỉnh chiều rộng
    view.layoutParams = layoutParams
}

fun EditFluidFragment.saveEvent() {
    binding.tvSave.setPreventDoubleClickScaleView {
        val listTextViewData = mutableListOf<TextViewData>()
        for (item in Constant.textViewList) {
            val tempTextViewData = TextViewData(
                id = 0,
                wallpaperId = 0,
                text = item.text.toString(),
                color = item.currentTextColor,
                typeFaceSource = requireContext().getTypeFaceMap().entries.find { it.value == item.typeface }?.key ?: pion.tech.commonres.R.font.font_300,
                positionX = item.x,
                positionY = item.y,
                textSize = item.textSize
            )
            listTextViewData.add(tempTextViewData)
        }
        val tempWallpaperWithTextView = WallpaperWithTextView(
            wallpaperName = WallpaperName(
                id = 0,
                nameWallpaper = nameWallpaper ?: "AbstractAdventure"
            ), listTextViewData = listTextViewData
        )
        viewModel.createNewChatRoomAndInsertMessage(
            wallpaperWithTextView = tempWallpaperWithTextView,
            onSuccess = {
                displayToast(
                    getString(
                        R.string.save_success
                    )
                )
            },
            onFailure = { displayToast(R.string.something_error) })
    }
}

fun EditFluidFragment.onBackEvent() {
    binding.ivBack.setPreventDoubleClickScaleView {
        backEvent()
    }
    activity?.onBackPressedDispatcher?.addCallback(this, true) {
        backEvent()
    }
}

fun EditFluidFragment.backEvent() {
    findNavController().navigateUp()
}

fun EditFluidFragment.setUpSurfaceView() {
    config = Config.Current
    SettingsStorage.loadConfigFromInternalPreset(
        nameWallpaper,
        requireContext().assets,
        config
    )

    binding.surfaceView.preserveEGLContextOnPause = true
    nativeInterface.setAssetManager(requireContext().assets)
    val orientationSensor = OrientationSensor(requireContext(), requireActivity().application)
    binding.surfaceView.setEGLContextClientVersion(2)
    val renderer = GLES20Renderer(nativeInterface, orientationSensor)
    binding.surfaceView.setRenderer(renderer)
    renderer.setInitialScreenSize(300, 200)
    nativeInterface.onCreate(300, 200, false)
    nativeInterface.updateConfig(config)
}


@SuppressLint("ClickableViewAccessibility")
fun EditFluidFragment.startHandAnim() {
    binding.clTutorial.isVisible = true
    binding.ivHand.post {
        safeDelay(500) {
            val slideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_left)
            binding.ivHand.startAnimation(slideUp)
        }
    }

    binding.clTutorial.setOnTouchListener { _, _ ->
        binding.clTutorial.isVisible = false
        true
    }
}

fun EditFluidFragment.onScreenEvent() {
    binding.btnOnscreen.setPreventDoubleClickScaleView {
        val intent = Intent(requireContext(), OnScreenActivity::class.java)
        intent.putExtra(BundleKey.KEY_FLUID_NAME_ON_SCREEN, nameWallpaper)
        startActivity(intent)
    }
}

fun EditFluidFragment.addTextEvent() {
    binding.btnAddText.setPreventDoubleClickScaleView {
        val intent = Intent(requireContext(), AddTextFluidActivity::class.java)
        intent.putExtra(BundleKey.KEY_FLUID_NAME_ADD_TEXT, nameWallpaper)
        startActivity(intent)
    }
}

fun EditFluidFragment.setWallpaperEvent() {
    binding.btnSetWallpaper.setPreventDoubleClickScaleView {
        Constant.smallFrameWidth =binding.flContainer.width
        Constant.smallFrameHeight =binding.flContainer.height
        val intent = Intent(requireContext(),PreviewFluidActivity::class.java)
        intent.putExtra(BundleKey.KEY_FLUID_NAME_PREVIEW,nameWallpaper)
        startActivity(intent)
    }
}