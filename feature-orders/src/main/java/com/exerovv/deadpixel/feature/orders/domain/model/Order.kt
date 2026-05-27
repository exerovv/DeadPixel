package com.exerovv.deadpixel.feature.orders.domain.model

data class Order(
    val id: Int,
    val workOrderNumber: String,
    val description: String,
    val status: OrderStatus,
    val estimatedCost: Double?,
    val deadline: String?,
    val createdAt: String
)
