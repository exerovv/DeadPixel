package com.exerovv.deadpixel.feature.orders.data.repository

import com.exerovv.deadpixel.core.network.ApiResult
import com.exerovv.deadpixel.core.network.safeApiCall
import com.exerovv.deadpixel.feature.orders.data.remote.OrdersApi
import com.exerovv.deadpixel.feature.orders.data.remote.dto.OrderDto
import com.exerovv.deadpixel.feature.orders.domain.model.Order
import com.exerovv.deadpixel.feature.orders.domain.repository.OrdersRepository
import javax.inject.Inject

class OrdersRepositoryImpl @Inject constructor(
    private val api: OrdersApi
) : OrdersRepository {

    override suspend fun getOrders(): ApiResult<List<Order>> = safeApiCall {
        api.getOrders().map { it.toDomain() }
    }

    private fun OrderDto.toDomain() = Order(
        id = id,
        workOrderNumber = workOrderNumber,
        description = description,
        status = status,
        estimatedCost = estimatedCost,
        deadline = deadline,
        createdAt = createdAt
    )
}
