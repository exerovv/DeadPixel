package com.exerovv.deadpixel.feature.orders.domain.repository

import com.exerovv.deadpixel.core.network.ApiResult
import com.exerovv.deadpixel.feature.orders.domain.model.Order
import com.exerovv.deadpixel.feature.orders.domain.model.OrderAssignment
import com.exerovv.deadpixel.feature.orders.domain.model.OrderStatusHistory

interface OrdersRepository {
    suspend fun getOrders(): ApiResult<List<Order>>
    suspend fun getOrderById(orderId: Int): ApiResult<Order>
    suspend fun getOrderAssignment(orderId: Int): ApiResult<OrderAssignment?>
    suspend fun getOrderHistory(orderId: Int): ApiResult<List<OrderStatusHistory>>
}
