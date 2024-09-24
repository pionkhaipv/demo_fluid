package com.demo.fluid.framework.presentation.edit_fluid

import com.demo.fluid.framework.database.daointerface.TextViewDataDAO
import com.demo.fluid.framework.database.daointerface.WallpaperNameDAO
import com.demo.fluid.framework.database.entities.WallpaperWithTextView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pion.tech.fluid_wallpaper.framework.presentation.common.BaseViewModel
import pion.tech.fluid_wallpaper.framework.presentation.common.launchIO
import javax.inject.Inject

@HiltViewModel
class EditFluidViewModel @Inject constructor(
    private val textViewDataDAO: TextViewDataDAO,
    private val wallpaperNameDAO: WallpaperNameDAO,
) : BaseViewModel() {

    fun createNewChatRoomAndInsertMessage(
        wallpaperWithTextView: WallpaperWithTextView,
        onSuccess: (wallpaperWithTextView: WallpaperWithTextView) -> Unit,
        onFailure: () -> Unit,
    ) {
        launchIO(exceptionHandler = CoroutineExceptionHandler { _, _ -> onFailure.invoke() }) {
            val newWallpaperId =
                wallpaperNameDAO.insert(wallpaperWithTextView.wallpaperName).firstOrNull()
            if (newWallpaperId != null) {
                val textViewDataList = wallpaperWithTextView.listTextViewData.map { textViewData ->
                    textViewData.copy(wallpaperId = newWallpaperId.toInt())
                }
                textViewDataDAO.insert(*textViewDataList.toTypedArray())
                val wallpaperInserted =
                    wallpaperNameDAO.getWallpaperWithTextViewById(newWallpaperId.toInt())
                if (wallpaperInserted == null) {
                    withContext(Dispatchers.Main){
                        onFailure.invoke()
                    }
                } else {
                    withContext(Dispatchers.Main){
                        onSuccess.invoke(wallpaperInserted)
                    }
                }

            } else {
                withContext(Dispatchers.Main){
                    onFailure.invoke()
                }
            }

        }
    }
}