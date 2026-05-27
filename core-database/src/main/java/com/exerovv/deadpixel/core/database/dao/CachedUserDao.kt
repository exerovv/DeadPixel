package com.exerovv.deadpixel.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.exerovv.deadpixel.core.database.entity.CachedUserEntity

@Dao
interface CachedUserDao {

    @Query("SELECT * FROM cached_user LIMIT 1")
    suspend fun get(): CachedUserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: CachedUserEntity)

    @Query("DELETE FROM cached_user")
    suspend fun clear()
}
