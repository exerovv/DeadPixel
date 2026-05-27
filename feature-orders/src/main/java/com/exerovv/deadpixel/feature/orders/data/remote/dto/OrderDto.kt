package com.exerovv.deadpixel.feature.orders.data.remote.dto

import com.exerovv.deadpixel.feature.orders.domain.model.OrderStatus
import kotlinx.serialization.Serializable

@Serializable
data class OrderDto(
    val id: Int,
    val workOrderNumber: String,
    val equipmentId: Int,
    val createdBy: Int,
    val status: OrderStatus,
    val description: String,
    val estimatedCost: Double? = null,
    val deadline: String? = null,
    val createdAt: String,
    val updatedAt: String,
    val completedAt: String? = null
)
