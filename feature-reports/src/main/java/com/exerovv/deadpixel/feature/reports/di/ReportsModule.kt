package com.exerovv.deadpixel.feature.reports.di

import com.exerovv.deadpixel.feature.reports.data.remote.ReportsApi
import com.exerovv.deadpixel.feature.reports.data.repository.ReportsRepositoryImpl
import com.exerovv.deadpixel.feature.reports.domain.repository.ReportsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReportsModule {

    @Provides
    @Singleton
    fun provideReportsApi(retrofit: Retrofit): ReportsApi =
        retrofit.create(ReportsApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ReportsBindsModule {

    @Binds
    @Singleton
    abstract fun bindReportsRepository(impl: ReportsRepositoryImpl): ReportsRepository
}
