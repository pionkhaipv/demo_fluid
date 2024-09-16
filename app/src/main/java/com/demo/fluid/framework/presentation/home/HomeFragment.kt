package com.demo.fluid.framework.presentation.home

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.view.View
import com.demo.fluid.activity.home.HomeModel
import com.demo.fluid.databinding.FragmentHomeBinding
import com.demo.fluid.gl.SettingsStorage
import com.demo.fluid.service.NewWallpaperService
import com.demo.fluid.utils.Common
import com.magicfluids.Config
import com.magicfluids.NativeInterface
import dagger.hilt.android.AndroidEntryPoint
import com.demo.fluid.framework.presentation.common.BaseFragment
import com.demo.fluid.framework.presentation.home.adapter.WallpaperAdapter

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(
    FragmentHomeBinding::inflate,
    HomeViewModel::class.java
), WallpaperAdapter.Listener {

    val adapter = WallpaperAdapter()
    override fun init(view: View) {
        initView()
        viewModel.initData()
        adapter.setListener(this)
        adapter.submitList(viewModel.listWallpaper)
        NativeInterface.init()
        val abc = NativeInterface()
//        val mid = abc.id
//        Log.d("sagawgagwawgawg", "init: $mid")
        abc.onCreate(300, 200, false)

    }

    override fun subscribeObserver(view: View) {
    }

    override fun onItemClick(item: HomeModel) {
        WallpaperManager.getInstance(requireContext()).clear()
        Common.INSTANCE.nameWallpaper = item.title
        val componentName: ComponentName = ComponentName(
            requireContext().packageName,
            NewWallpaperService::class.java.getName()
        )
        loadConfigPreset(item.title)

        val intent = Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER")
        intent.putExtra("android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT", componentName)
        startActivity(intent)

    }

    private fun loadConfigPreset(nameWallpaper: String) {
        val config2: Config = Config.Current

        SettingsStorage.loadConfigFromInternalPreset(
            nameWallpaper,
            requireActivity().assets,
            config2
        )
    }

}
