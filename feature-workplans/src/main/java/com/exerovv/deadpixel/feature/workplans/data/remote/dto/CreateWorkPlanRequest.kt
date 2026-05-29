package com.exerovv.deadpixel.feature.workplans.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateWorkPlanRequest(val orderId: Int)
