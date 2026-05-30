package com.exerovv.deadpixel.feature.orders.domain.usecase

import com.exerovv.deadpixel.feature.orders.domain.model.Order
import com.exerovv.deadpixel.feature.orders.domain.repository.OrdersRepository
import javax.inject.Inject

class GetOverdueOrdersUseCase @Inject constructor(
    private val repository: OrdersRepository
) {
    suspend operator fun invoke(): List<Order> = repository.getOverdueOrders()
}
