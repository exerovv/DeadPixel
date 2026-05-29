package com.exerovv.deadpixel.feature.orders.presentation.detail

import com.exerovv.deadpixel.feature.orders.domain.model.OrderStatus

sealed interface OrderDetailCommand {
    data object Retry : OrderDetailCommand
    data class UpdateStatus(val status: OrderStatus, val note: String?) : OrderDetailCommand
    data class AssignMaster(val masterId: Int) : OrderDetailCommand
}
