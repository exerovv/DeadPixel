package com.exerovv.deadpixel.feature.orders.domain.usecase

import com.exerovv.deadpixel.feature.orders.domain.model.Order
import com.exerovv.deadpixel.feature.orders.domain.repository.OrdersRepository
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(private val repo: OrdersRepository) {
    suspend operator fun invoke(
        equipmentId: Int,
        description: String,
        deadline: String?,
        estimatedCost: Double?
    ): Order = repo.createOrder(equipmentId, description, deadline, estimatedCost)
}
