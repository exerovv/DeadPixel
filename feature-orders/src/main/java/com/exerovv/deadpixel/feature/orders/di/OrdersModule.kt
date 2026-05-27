package com.exerovv.deadpixel.feature.orders.di

import com.exerovv.deadpixel.feature.orders.data.remote.OrdersApi
import com.exerovv.deadpixel.feature.orders.data.repository.OrdersRepositoryImpl
import com.exerovv.deadpixel.feature.orders.domain.repository.OrdersRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OrdersModule {

    @Provides
    @Singleton
    fun provideOrdersApi(retrofit: Retrofit): OrdersApi =
        retrofit.create(OrdersApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class OrdersBindsModule {

    @Binds
    @Singleton
    abstract fun bindOrdersRepository(impl: OrdersRepositoryImpl): OrdersRepository
}
