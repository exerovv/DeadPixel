package com.exerovv.deadpixel.feature.orders.domain.model

data class OrderAssignment(
    val id: Int,
    val orderId: Int,
    val masterId: Int,
    val assignedAt: String,
    val isActive: Boolean
)
