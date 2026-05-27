package com.exerovv.deadpixel.feature.orders.domain.repository

import com.exerovv.deadpixel.core.network.ApiResult
import com.exerovv.deadpixel.feature.orders.domain.model.Order

interface OrdersRepository {
    suspend fun getOrders(): ApiResult<List<Order>>
}
