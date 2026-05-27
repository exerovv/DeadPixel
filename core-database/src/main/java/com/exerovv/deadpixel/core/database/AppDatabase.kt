package com.exerovv.deadpixel.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.exerovv.deadpixel.core.database.dao.CachedUserDao
import com.exerovv.deadpixel.core.database.entity.CachedUserEntity

@Database(entities = [CachedUserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cachedUserDao(): CachedUserDao
}
