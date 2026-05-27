package com.exerovv.deadpixel.feature.orders.data.remote

import com.exerovv.deadpixel.feature.orders.data.remote.dto.OrderDto
import retrofit2.http.GET

interface OrdersApi {
    @GET("api/orders")
    suspend fun getOrders(): List<OrderDto>
}
