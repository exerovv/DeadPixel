package com.exerovv.deadpixel.feature.workplans.domain.model

data class WorkPlanItem(
    val id: Int,
    val workPlanId: Int,
    val description: String,
    val status: WorkPlanItemStatus
)
