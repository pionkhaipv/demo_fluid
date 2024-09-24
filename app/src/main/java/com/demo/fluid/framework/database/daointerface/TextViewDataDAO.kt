package com.demo.fluid.framework.database.daointerface

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.demo.fluid.framework.database.entities.TextViewData
import com.demo.fluid.framework.database.entities.WallpaperName
import kotlinx.coroutines.flow.Flow

@Dao
interface TextViewDataDAO {

    @Insert
    fun insert(vararg scale: TextViewData): List<Long>

    @Update
    fun update(vararg string: TextViewData)

    @Delete
    fun delete(scale: TextViewData)

    @Query("SELECT * FROM ${TextViewData.TABLE_NAME}")
    fun getAllTextViewData(): Flow<List<TextViewData>>

}