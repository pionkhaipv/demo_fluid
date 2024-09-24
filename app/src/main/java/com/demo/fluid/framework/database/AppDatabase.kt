package com.demo.fluid.framework.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.demo.fluid.framework.database.daointerface.TextViewDataDAO
import com.demo.fluid.framework.database.daointerface.WallpaperNameDAO
import com.demo.fluid.framework.database.entities.TextViewData
import com.demo.fluid.framework.database.entities.WallpaperName

@Database(entities = [WallpaperName::class, TextViewData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun textViewDataDAO(): TextViewDataDAO
    abstract fun wallpaperDAO(): WallpaperNameDAO

    companion object {
        const val DATABASE_NAME = "app_db"
    }

}