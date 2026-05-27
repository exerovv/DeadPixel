package com.exerovv.deadpixel.feature.orders.domain.usecase

import com.exerovv.deadpixel.feature.orders.domain.repository.OrdersRepository
import javax.inject.Inject

class GetOrdersByMasterUseCase @Inject constructor(
    private val repository: OrdersRepository
) {
    suspend operator fun invoke(masterId: Int) = repository.getOrdersByMaster(masterId)
}
