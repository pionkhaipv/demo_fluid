package com.demo.fluid.framework.database.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.demo.fluid.framework.database.entities.WallpaperName.Companion.TABLE_NAME
import kotlinx.parcelize.Parcelize

@Entity(tableName = TABLE_NAME)
@Parcelize
data class WallpaperName(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val nameWallpaper:String,
) : Parcelable{
    companion object {
        const val TABLE_NAME = "WallpaperName"
    }
}
