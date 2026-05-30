package com.exerovv.deadpixel.feature.orders.domain.repository

import com.exerovv.deadpixel.feature.orders.domain.model.Order
import com.exerovv.deadpixel.feature.orders.domain.model.OrderAssignment
import com.exerovv.deadpixel.feature.orders.domain.model.OrderStatus
import com.exerovv.deadpixel.feature.orders.domain.model.OrderStatusHistory

interface OrdersRepository {
    suspend fun getOrders(): List<Order>
    suspend fun getOverdueOrders(): List<Order>
    suspend fun getOrderById(orderId: Int): Order
    suspend fun getOrderAssignment(orderId: Int): OrderAssignment?
    suspend fun getOrderHistory(orderId: Int): List<OrderStatusHistory>
    suspend fun getOrdersByMaster(masterId: Int): List<Order>
    suspend fun getOrdersByStatus(status: String): List<Order>
    suspend fun createOrder(equipmentId: Int, description: String, deadline: String?, estimatedCost: Double?): Order
    suspend fun updateOrderStatus(orderId: Int, status: OrderStatus, note: String?): Order
    suspend fun assignMaster(orderId: Int, masterId: Int)
}
