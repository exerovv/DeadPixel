package com.exerovv.deadpixel.feature.auth.di

import com.exerovv.deadpixel.core.network.TokenRefresher
import com.exerovv.deadpixel.feature.auth.data.remote.AuthApiService
import com.exerovv.deadpixel.feature.auth.data.remote.TokenRefresherImpl
import com.exerovv.deadpixel.feature.auth.data.repository.AuthRepositoryImpl
import com.exerovv.deadpixel.feature.auth.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTokenRefresher(impl: TokenRefresherImpl): TokenRefresher

    companion object {

        @Provides
        @Singleton
        fun provideAuthApiService(@Named("auth") retrofit: Retrofit): AuthApiService =
            retrofit.create(AuthApiService::class.java)
    }
}
