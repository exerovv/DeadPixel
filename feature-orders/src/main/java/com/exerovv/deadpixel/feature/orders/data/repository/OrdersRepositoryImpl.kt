package com.exerovv.deadpixel.feature.orders.data.repository

import com.exerovv.deadpixel.feature.orders.data.mapper.toDomain
import com.exerovv.deadpixel.feature.orders.data.remote.OrdersApi
import com.exerovv.deadpixel.feature.orders.data.remote.dto.AssignMasterRequest
import com.exerovv.deadpixel.feature.orders.data.remote.dto.CreateOrderRequest
import com.exerovv.deadpixel.feature.orders.data.remote.dto.UpdateOrderStatusRequest
import com.exerovv.deadpixel.feature.orders.domain.model.Order
import com.exerovv.deadpixel.feature.orders.domain.model.OrderAssignment
import com.exerovv.deadpixel.feature.orders.domain.model.OrderStatus
import com.exerovv.deadpixel.feature.orders.domain.model.OrderStatusHistory
import com.exerovv.deadpixel.feature.orders.domain.repository.OrdersRepository
import retrofit2.HttpException
import javax.inject.Inject

class OrdersRepositoryImpl @Inject constructor(
    private val api: OrdersApi
) : OrdersRepository {

    override suspend fun getOrders(): List<Order> =
        api.getOrders().map { it.toDomain() }

    override suspend fun getOverdueOrders(): List<Order> =
        api.getOverdueOrders().map { it.toDomain() }

    override suspend fun getOrderById(orderId: Int): Order =
        api.getOrderById(orderId).toDomain()

    override suspend fun getOrderAssignment(orderId: Int): OrderAssignment? = try {
        api.getOrderAssignment(orderId).toDomain()
    } catch (e: HttpException) {
        if (e.code() == 404) null else throw e
    }

    override suspend fun getOrderHistory(orderId: Int): List<OrderStatusHistory> =
        api.getOrderHistory(orderId).map { it.toDomain() }

    override suspend fun getOrdersByMaster(masterId: Int): List<Order> =
        api.getOrdersByMaster(masterId).map { it.toDomain() }

    override suspend fun getOrdersByStatus(status: String): List<Order> =
        api.getOrdersByStatus(status).map { it.toDomain() }

    override suspend fun createOrder(
        equipmentId: Int,
        description: String,
        deadline: String?,
        estimatedCost: Double?
    ): Order = api.createOrder(CreateOrderRequest(equipmentId, description, estimatedCost, deadline)).toDomain()

    override suspend fun updateOrderStatus(orderId: Int, status: OrderStatus, note: String?): Order =
        api.updateOrderStatus(orderId, UpdateOrderStatusRequest(status.name, note)).toDomain()

    override suspend fun assignMaster(orderId: Int, masterId: Int) {
        api.assignMaster(orderId, AssignMasterRequest(masterId))
    }
}
