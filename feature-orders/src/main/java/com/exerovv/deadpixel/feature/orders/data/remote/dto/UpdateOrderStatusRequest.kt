package com.exerovv.deadpixel.feature.orders.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateOrderStatusRequest(
    val status: String,
    val note: String? = null
)
