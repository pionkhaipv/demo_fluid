package com.demo.fluid.framework.presentation.personal

import com.demo.fluid.framework.database.daointerface.WallpaperNameDAO
import com.demo.fluid.framework.database.entities.WallpaperWithTextView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import pion.tech.fluid_wallpaper.framework.presentation.common.BaseViewModel
import pion.tech.fluid_wallpaper.framework.presentation.common.launchIO
import javax.inject.Inject

@HiltViewModel
class PersonalViewModel @Inject constructor(
    private val wallpaperNameDAO: WallpaperNameDAO,
): BaseViewModel() {


    private val _wallpaperWithTextView = MutableStateFlow<List<WallpaperWithTextView>>(emptyList())
    val wallpaperWithTextViewStateFlow: StateFlow<List<WallpaperWithTextView>> = _wallpaperWithTextView

    fun getAllWallpaper() {
        launchIO {
            wallpaperNameDAO.getWallpaperWithTextView().flowOn(Dispatchers.IO).collect{
                _wallpaperWithTextView.value = it
            }
        }
    }

}