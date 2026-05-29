package com.exerovv.deadpixel.feature.workplans.di

import com.exerovv.deadpixel.feature.workplans.data.remote.WorkPlansApi
import com.exerovv.deadpixel.feature.workplans.data.repository.WorkPlansRepositoryImpl
import com.exerovv.deadpixel.feature.workplans.domain.repository.WorkPlansRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkPlansModule {

    @Provides
    @Singleton
    fun provideWorkPlansApi(retrofit: Retrofit): WorkPlansApi =
        retrofit.create(WorkPlansApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class WorkPlansBindsModule {

    @Binds
    @Singleton
    abstract fun bindWorkPlansRepository(impl: WorkPlansRepositoryImpl): WorkPlansRepository
}
