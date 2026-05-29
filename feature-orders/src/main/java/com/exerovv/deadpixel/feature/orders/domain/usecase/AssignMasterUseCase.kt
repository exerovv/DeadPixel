package com.exerovv.deadpixel.feature.orders.domain.usecase

import com.exerovv.deadpixel.feature.orders.domain.repository.OrdersRepository
import javax.inject.Inject

class AssignMasterUseCase @Inject constructor(private val repo: OrdersRepository) {
    suspend operator fun invoke(orderId: Int, masterId: Int) = repo.assignMaster(orderId, masterId)
}
