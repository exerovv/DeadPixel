package com.exerovv.deadpixel.feature.workplans.domain.model

data class WorkPlan(
    val id: Int,
    val orderId: Int,
    val status: WorkPlanStatus,
    val items: List<WorkPlanItem>
)
