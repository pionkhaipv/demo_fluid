package com.demo.fluid.framework.database.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.demo.fluid.framework.database.entities.TextViewData.Companion.TABLE_NAME
import kotlinx.parcelize.Parcelize

@Entity(tableName = TABLE_NAME)
@Parcelize
data class TextViewData(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var wallpaperId:Int = 0,
    val text: String,
    val color: Int,
    val typeFaceSource: Int,
    val positionX: Float,
    val positionY: Float,
    val textSize:Float
) : Parcelable{
    companion object {
        const val TABLE_NAME = "TextViewData"
    }
}
