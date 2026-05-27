package com.exerovv.deadpixel.feature.orders.domain.usecase

import com.exerovv.deadpixel.core.network.ApiResult
import com.exerovv.deadpixel.feature.orders.domain.model.OrderStatusHistory
import com.exerovv.deadpixel.feature.orders.domain.repository.OrdersRepository
import javax.inject.Inject

class GetOrderHistoryUseCase @Inject constructor(
    private val repository: OrdersRepository
) {
    suspend operator fun invoke(orderId: Int): ApiResult<List<OrderStatusHistory>> =
        repository.getOrderHistory(orderId)
}
