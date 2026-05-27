package com.exerovv.deadpixel.feature.diagnostics.di

import com.exerovv.deadpixel.feature.diagnostics.data.remote.DiagnosticsApi
import com.exerovv.deadpixel.feature.diagnostics.data.repository.DiagnosticsRepositoryImpl
import com.exerovv.deadpixel.feature.diagnostics.domain.repository.DiagnosticsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiagnosticsModule {

    @Provides
    @Singleton
    fun provideDiagnosticsApi(retrofit: Retrofit): DiagnosticsApi =
        retrofit.create(DiagnosticsApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DiagnosticsBindsModule {

    @Binds
    @Singleton
    abstract fun bindDiagnosticsRepository(impl: DiagnosticsRepositoryImpl): DiagnosticsRepository
}
