package com.exerovv.deadpixel.feature.workplans.presentation

import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlanItemStatus
import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlanStatus

sealed interface WorkPlanCommand {
    data object Retry : WorkPlanCommand
    data object CreatePlan : WorkPlanCommand
    data class UpdatePlanStatus(val status: WorkPlanStatus) : WorkPlanCommand
    data class UpdateItemStatus(val itemId: Int, val status: WorkPlanItemStatus) : WorkPlanCommand
    data class AddItem(val description: String) : WorkPlanCommand
    data class DeleteItem(val itemId: Int) : WorkPlanCommand
}
