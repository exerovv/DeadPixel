package com.exerovv.deadpixel.feature.workplans.domain.usecase

import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlanItem
import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlanItemStatus
import com.exerovv.deadpixel.feature.workplans.domain.repository.WorkPlansRepository
import javax.inject.Inject

class UpdateWorkPlanItemStatusUseCase @Inject constructor(private val repo: WorkPlansRepository) {
    suspend operator fun invoke(planId: Int, itemId: Int, status: WorkPlanItemStatus): WorkPlanItem =
        repo.updateWorkPlanItemStatus(planId, itemId, status)
}
