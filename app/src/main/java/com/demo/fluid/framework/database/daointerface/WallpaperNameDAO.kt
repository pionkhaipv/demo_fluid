package com.demo.fluid.framework.database.daointerface

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.demo.fluid.framework.database.entities.WallpaperName
import com.demo.fluid.framework.database.entities.WallpaperWithTextView
import kotlinx.coroutines.flow.Flow

@Dao
interface WallpaperNameDAO {

    @Insert
    fun insert(vararg scale: WallpaperName): List<Long>

    @Update
    fun update(vararg string: WallpaperName)

    @Delete
    fun delete(scale: WallpaperName)

    @Query("SELECT * FROM ${WallpaperName.TABLE_NAME}")
    fun getAllTextViewData(): Flow<List<WallpaperName>>

    @Query("SELECT * FROM ${WallpaperName.TABLE_NAME} WHERE id = :id")
    fun getWallpaperNameById(id: Long): WallpaperName?

    @Transaction
    @Query("SELECT * FROM ${WallpaperName.TABLE_NAME}")
    fun getWallpaperWithTextView(): Flow<List<WallpaperWithTextView>>

    @Transaction
    @Query("SELECT * FROM ${WallpaperName.TABLE_NAME} WHERE id = :id")
    fun getWallpaperWithTextViewById(id: Int): WallpaperWithTextView?

}