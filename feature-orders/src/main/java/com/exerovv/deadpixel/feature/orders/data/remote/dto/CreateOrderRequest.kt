package com.exerovv.deadpixel.feature.orders.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderRequest(
    val equipmentId: Int,
    val description: String,
    val estimatedCost: Double? = null,
    val deadline: String? = null
)
