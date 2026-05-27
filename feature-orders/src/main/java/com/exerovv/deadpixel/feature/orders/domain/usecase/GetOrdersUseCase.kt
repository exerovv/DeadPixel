package com.exerovv.deadpixel.feature.orders.domain.usecase

import com.exerovv.deadpixel.core.network.ApiResult
import com.exerovv.deadpixel.feature.orders.domain.model.Order
import com.exerovv.deadpixel.feature.orders.domain.repository.OrdersRepository
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val repository: OrdersRepository
) {
    suspend operator fun invoke(): ApiResult<List<Order>> = repository.getOrders()
}
