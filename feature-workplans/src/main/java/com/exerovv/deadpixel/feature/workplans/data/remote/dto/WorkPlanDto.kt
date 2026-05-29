package com.exerovv.deadpixel.feature.workplans.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class WorkPlanDto(
    val id: Int,
    val orderId: Int,
    val status: String,
    val items: List<WorkPlanItemDto>
)
