package com.exerovv.deadpixel.feature.workplans.domain.usecase

import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlanItem
import com.exerovv.deadpixel.feature.workplans.domain.repository.WorkPlansRepository
import javax.inject.Inject

class AddWorkPlanItemUseCase @Inject constructor(private val repo: WorkPlansRepository) {
    suspend operator fun invoke(planId: Int, description: String): WorkPlanItem =
        repo.addWorkPlanItem(planId, description)
}
