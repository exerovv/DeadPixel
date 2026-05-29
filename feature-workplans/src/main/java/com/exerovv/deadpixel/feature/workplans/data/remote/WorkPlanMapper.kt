package com.exerovv.deadpixel.feature.workplans.data.remote

import com.exerovv.deadpixel.feature.workplans.data.remote.dto.WorkPlanDto
import com.exerovv.deadpixel.feature.workplans.data.remote.dto.WorkPlanItemDto
import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlan
import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlanItem
import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlanItemStatus
import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlanStatus

fun WorkPlanDto.toDomain() = WorkPlan(
    id = id,
    orderId = orderId,
    status = WorkPlanStatus.valueOf(status),
    items = items.map { it.toDomain() }
)

fun WorkPlanItemDto.toDomain() = WorkPlanItem(
    id = id,
    workPlanId = workPlanId,
    description = description,
    status = WorkPlanItemStatus.valueOf(status)
)
