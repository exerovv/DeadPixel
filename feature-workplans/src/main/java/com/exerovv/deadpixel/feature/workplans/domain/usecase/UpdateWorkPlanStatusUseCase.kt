package com.exerovv.deadpixel.feature.workplans.domain.usecase

import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlan
import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlanStatus
import com.exerovv.deadpixel.feature.workplans.domain.repository.WorkPlansRepository
import javax.inject.Inject

class UpdateWorkPlanStatusUseCase @Inject constructor(private val repo: WorkPlansRepository) {
    suspend operator fun invoke(id: Int, status: WorkPlanStatus): WorkPlan =
        repo.updateWorkPlanStatus(id, status)
}
