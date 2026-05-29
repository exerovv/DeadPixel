package com.exerovv.deadpixel.feature.workplans.domain.usecase

import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlan
import com.exerovv.deadpixel.feature.workplans.domain.repository.WorkPlansRepository
import javax.inject.Inject

class CreateWorkPlanUseCase @Inject constructor(private val repo: WorkPlansRepository) {
    suspend operator fun invoke(orderId: Int): WorkPlan = repo.createWorkPlan(orderId)
}
