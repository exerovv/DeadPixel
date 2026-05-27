package com.exerovv.deadpixel.feature.orders.data.remote.dto

import com.exerovv.deadpixel.feature.orders.domain.model.OrderStatus
import kotlinx.serialization.Serializable

@Serializable
data class OrderStatusHistoryDto(
    val id: Int,
    val orderId: Int,
    val previousStatus: OrderStatus? = null,
    val newStatus: OrderStatus,
    val changedBy: Int,
    val note: String? = null,
    val changedAt: String
)
