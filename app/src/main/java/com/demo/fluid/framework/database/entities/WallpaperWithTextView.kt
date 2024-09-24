package com.demo.fluid.framework.database.entities

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class WallpaperWithTextView(
    @Embedded val wallpaperName: WallpaperName,
    @Relation(
        parentColumn = "id",
        entityColumn = "wallpaperId",
    )
    val listTextViewData: List<TextViewData>,
) : Parcelable
