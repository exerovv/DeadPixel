package com.exerovv.deadpixel.core.database

import android.content.Context
import androidx.room.Room
import com.exerovv.deadpixel.core.database.dao.CachedUserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "deadpixel.db").build()

    @Provides
    fun provideCachedUserDao(db: AppDatabase): CachedUserDao = db.cachedUserDao()
}
