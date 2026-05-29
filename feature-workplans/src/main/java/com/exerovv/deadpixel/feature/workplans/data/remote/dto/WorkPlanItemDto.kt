package com.exerovv.deadpixel.feature.workplans.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class WorkPlanItemDto(
    val id: Int,
    val workPlanId: Int,
    val description: String,
    val status: String
)
