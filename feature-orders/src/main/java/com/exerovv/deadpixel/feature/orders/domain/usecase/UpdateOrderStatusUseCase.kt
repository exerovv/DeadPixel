package com.exerovv.deadpixel.feature.orders.domain.usecase

import com.exerovv.deadpixel.feature.orders.domain.model.Order
import com.exerovv.deadpixel.feature.orders.domain.model.OrderStatus
import com.exerovv.deadpixel.feature.orders.domain.repository.OrdersRepository
import javax.inject.Inject

class UpdateOrderStatusUseCase @Inject constructor(private val repo: OrdersRepository) {
    suspend operator fun invoke(orderId: Int, status: OrderStatus, note: String?): Order =
        repo.updateOrderStatus(orderId, status, note)
}
