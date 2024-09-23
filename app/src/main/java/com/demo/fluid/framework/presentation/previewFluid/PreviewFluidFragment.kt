package com.demo.fluid.framework.presentation.previewFluid

import android.app.Application
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.view.View
import com.demo.fluid.databinding.FragmentPreviewFluidBinding
import com.demo.fluid.framework.presentation.common.BaseFragment
import com.demo.fluid.framework.presentation.wallpaper_service.NewWallpaperService
import com.demo.fluid.util.Common
import com.demo.fluid.util.gl.GLES20Renderer
import com.demo.fluid.util.gl.OrientationSensor
import com.demo.fluid.util.gl.SettingsStorage
import com.magicfluids.Config
import com.magicfluids.NativeInterface
import dagger.hilt.android.AndroidEntryPoint
import com.demo.fluid.util.displayToast
import com.demo.fluid.util.setPreventDoubleClickScaleView

@AndroidEntryPoint
class PreviewFluidFragment : BaseFragment<FragmentPreviewFluidBinding, PreviewFluidViewModel>(
    FragmentPreviewFluidBinding::inflate,
    PreviewFluidViewModel::class.java
) {

    private var config: Config? = null

    var nameWallpaper: String = "AbstractAdventure"
    private var nativeInterface: NativeInterface? = null
    private var orientationSensor: OrientationSensor? = null
    private var renderer: GLES20Renderer? = null

    override fun init(view: View) {
        nameWallpaper = arguments?.getString("KeyName") ?:"AbstractAdventure"
        config = Config.Current
        NativeInterface.init()
        loadConfigPreset()
        initSettingController()

        binding.btnApply.setPreventDoubleClickScaleView {
            try {
                WallpaperManager.getInstance(requireContext()).clear()
                applySettingsToLwp()
                Common.INSTANCE.nameWallpaper = nameWallpaper
                Common.INSTANCE.setNameWallpaper(requireContext(), nameWallpaper)
                val componentName: ComponentName =
                    ComponentName(requireContext().packageName, NewWallpaperService::class.java.name)
                val intent = Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER")
                intent.putExtra(
                    "android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT",
                    componentName
                )
                startActivity(intent)
            } catch (unused: Exception) {
                displayToast("Device not support")
            }
        }

    }
    fun applySettingsToLwp() {
        Config.LWPCurrent.copyValuesFrom(this.config)
    }
    override fun subscribeObserver(view: View) {

    }

    private fun loadConfigPreset() {
        SettingsStorage.loadConfigFromInternalPreset(
            this.nameWallpaper,
            requireContext().assets,
            config
        )
    }

    override fun onResume() {
        super.onResume()
        binding.surfaceView.onResume()
        nativeInterface?.onResume()
    }

    private fun initSettingController() {
        binding.surfaceView.preserveEGLContextOnPause = false
        nativeInterface = NativeInterface()
        nativeInterface!!.setAssetManager(requireContext().assets)
        val application: Application = requireActivity().application
        this.orientationSensor = OrientationSensor(requireContext(), application)
        binding.surfaceView.setEGLContextClientVersion(2)
        val nativeInterface3 = this.nativeInterface
        val orientationSensor2 = this.orientationSensor
        this.renderer = GLES20Renderer(nativeInterface3, orientationSensor2)
        binding.surfaceView.setRenderer(this.renderer)
        val gLES20Renderer = this.renderer
        gLES20Renderer!!.setInitialScreenSize(300, 200)
        val nativeInterface4 = this.nativeInterface
        nativeInterface4!!.onCreate(300, 200, false)
        val nativeInterface5 = this.nativeInterface
        nativeInterface5!!.updateConfig(this.config)
    }

}
