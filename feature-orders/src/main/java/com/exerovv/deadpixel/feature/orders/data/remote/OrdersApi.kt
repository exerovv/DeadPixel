package com.exerovv.deadpixel.feature.orders.data.remote

import com.exerovv.deadpixel.feature.orders.data.remote.dto.OrderAssignmentDto
import com.exerovv.deadpixel.feature.orders.data.remote.dto.OrderDto
import com.exerovv.deadpixel.feature.orders.data.remote.dto.OrderStatusHistoryDto
import retrofit2.http.GET
import retrofit2.http.Path

interface OrdersApi {
    @GET("api/orders")
    suspend fun getOrders(): List<OrderDto>

    @GET("api/orders/{id}")
    suspend fun getOrderById(@Path("id") orderId: Int): OrderDto

    @GET("api/orders/{id}/assignment")
    suspend fun getOrderAssignment(@Path("id") orderId: Int): OrderAssignmentDto

    @GET("api/orders/{id}/history")
    suspend fun getOrderHistory(@Path("id") orderId: Int): List<OrderStatusHistoryDto>

    @GET("api/orders/master/{masterId}")
    suspend fun getOrdersByMaster(@Path("masterId") masterId: Int): List<OrderDto>

    @GET("api/orders/status/{status}")
    suspend fun getOrdersByStatus(@Path("status") status: String): List<OrderDto>
}
