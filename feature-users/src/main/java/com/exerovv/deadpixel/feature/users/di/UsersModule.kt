package com.exerovv.deadpixel.feature.users.di

import com.exerovv.deadpixel.feature.users.data.remote.UsersApi
import com.exerovv.deadpixel.feature.users.data.repository.UsersRepositoryImpl
import com.exerovv.deadpixel.feature.users.domain.repository.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UsersModule {

    @Provides
    @Singleton
    fun provideUsersApi(retrofit: Retrofit): UsersApi =
        retrofit.create(UsersApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class UsersBindsModule {

    @Binds
    @Singleton
    abstract fun bindUsersRepository(impl: UsersRepositoryImpl): UsersRepository
}
