package com.exerovv.deadpixel.feature.orders.domain.model

data class OrderStatusHistory(
    val id: Int,
    val previousStatus: OrderStatus?,
    val newStatus: OrderStatus,
    val changedBy: Int,
    val note: String?,
    val changedAt: String
)
