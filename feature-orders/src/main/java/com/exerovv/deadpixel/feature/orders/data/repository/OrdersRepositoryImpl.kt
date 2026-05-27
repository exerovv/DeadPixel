package com.exerovv.deadpixel.feature.orders.data.repository

import com.exerovv.deadpixel.core.network.ApiResult
import com.exerovv.deadpixel.core.network.safeApiCall
import com.exerovv.deadpixel.feature.orders.data.remote.OrdersApi
import com.exerovv.deadpixel.feature.orders.data.remote.dto.OrderAssignmentDto
import com.exerovv.deadpixel.feature.orders.data.remote.dto.OrderDto
import com.exerovv.deadpixel.feature.orders.data.remote.dto.OrderStatusHistoryDto
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

    private fun OrderDto.toDomain() = Order(
        id = id,
        workOrderNumber = workOrderNumber,
        description = description,
        status = status,
        estimatedCost = estimatedCost,
        deadline = deadline,
        createdAt = createdAt
    )

    private fun OrderAssignmentDto.toDomain() = OrderAssignment(
        id = id,
        orderId = orderId,
        masterId = masterId,
        assignedAt = assignedAt,
        isActive = isActive
    )

    private fun OrderStatusHistoryDto.toDomain() = OrderStatusHistory(
        id = id,
        previousStatus = previousStatus,
        newStatus = newStatus,
        changedBy = changedBy,
        note = note,
        changedAt = changedAt
    )
}
