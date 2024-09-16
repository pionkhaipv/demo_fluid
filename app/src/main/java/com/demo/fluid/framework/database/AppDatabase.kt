package pion.tech.fluid_wallpaper.framework.database

import androidx.room.Database
import androidx.room.RoomDatabase
import pion.tech.fluid_wallpaper.framework.database.daointerface.DummyDAO
import pion.tech.fluid_wallpaper.framework.database.entities.DummyEntity

@Database(entities = [DummyEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase(){

    abstract fun dummyDAO(): DummyDAO

    companion object {
        const val DATABASE_NAME = "app_db"
    }

}