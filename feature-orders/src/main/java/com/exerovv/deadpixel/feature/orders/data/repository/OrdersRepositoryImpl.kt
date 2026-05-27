package com.exerovv.deadpixel.feature.orders.data.repository

import com.exerovv.deadpixel.core.network.ApiResult
import com.exerovv.deadpixel.core.network.safeApiCall
import com.exerovv.deadpixel.feature.orders.data.mapper.toDomain
import com.exerovv.deadpixel.feature.orders.data.remote.OrdersApi
import com.exerovv.deadpixel.feature.orders.domain.model.Order
import com.exerovv.deadpixel.feature.orders.domain.model.OrderAssignment
import com.exerovv.deadpixel.feature.orders.domain.model.OrderStatusHistory
import com.exerovv.deadpixel.feature.orders.domain.repository.OrdersRepository
import javax.inject.Inject

class OrdersRepositoryImpl @Inject constructor(
    private val api: OrdersApi
) : OrdersRepository {

    override suspend fun getOrders(): ApiResult<List<Order>> = safeApiCall {
        api.getOrders().map { it.toDomain() }
    }

    override suspend fun getOrderById(orderId: Int): ApiResult<Order> = safeApiCall {
        api.getOrderById(orderId).toDomain()
    }

    override suspend fun getOrderAssignment(orderId: Int): ApiResult<OrderAssignment?> {
        return when (val result = safeApiCall { api.getOrderAssignment(orderId) }) {
            is ApiResult.Success -> ApiResult.Success(result.data.toDomain())
            is ApiResult.Error -> if (result.code == 404) ApiResult.Success(null) else result
        }
    }

    override suspend fun getOrderHistory(orderId: Int): ApiResult<List<OrderStatusHistory>> = safeApiCall {
        api.getOrderHistory(orderId).map { it.toDomain() }
    }
}
