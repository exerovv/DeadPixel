package com.exerovv.deadpixel.feature.workplans.domain.usecase

import com.exerovv.deadpixel.feature.workplans.domain.repository.WorkPlansRepository
import javax.inject.Inject

class DeleteWorkPlanItemUseCase @Inject constructor(private val repo: WorkPlansRepository) {
    suspend operator fun invoke(planId: Int, itemId: Int) = repo.deleteWorkPlanItem(planId, itemId)
}
