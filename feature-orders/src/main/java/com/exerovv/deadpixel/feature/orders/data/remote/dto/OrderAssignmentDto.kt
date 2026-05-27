package com.exerovv.deadpixel.feature.orders.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderAssignmentDto(
    val id: Int,
    val orderId: Int,
    val masterId: Int,
    val assignedAt: String,
    val isActive: Boolean
)
