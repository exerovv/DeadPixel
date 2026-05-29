package com.exerovv.deadpixel.feature.workplans.data.repository

import com.exerovv.deadpixel.feature.workplans.data.remote.WorkPlansApi
import com.exerovv.deadpixel.feature.workplans.data.remote.dto.AddWorkPlanItemRequest
import com.exerovv.deadpixel.feature.workplans.data.remote.dto.CreateWorkPlanRequest
import com.exerovv.deadpixel.feature.workplans.data.remote.toDomain
import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlan
import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlanItem
import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlanItemStatus
import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlanStatus
import com.exerovv.deadpixel.feature.workplans.domain.repository.WorkPlansRepository
import retrofit2.HttpException
import javax.inject.Inject

class WorkPlansRepositoryImpl @Inject constructor(
    private val api: WorkPlansApi
) : WorkPlansRepository {

    override suspend fun getWorkPlanByOrder(orderId: Int): WorkPlan? = try {
        api.getWorkPlanByOrder(orderId).toDomain()
    } catch (e: HttpException) {
        if (e.code() == 404) null else throw e
    }

    override suspend fun createWorkPlan(orderId: Int): WorkPlan =
        api.createWorkPlan(CreateWorkPlanRequest(orderId)).toDomain()

    override suspend fun updateWorkPlanStatus(id: Int, status: WorkPlanStatus): WorkPlan =
        api.updateWorkPlanStatus(id, status.name).toDomain()

    override suspend fun addWorkPlanItem(id: Int, description: String): WorkPlanItem =
        api.addWorkPlanItem(id, AddWorkPlanItemRequest(description)).toDomain()

    override suspend fun updateWorkPlanItemStatus(id: Int, itemId: Int, status: WorkPlanItemStatus): WorkPlanItem =
        api.updateWorkPlanItemStatus(id, itemId, status.name).toDomain()

    override suspend fun deleteWorkPlanItem(id: Int, itemId: Int) {
        api.deleteWorkPlanItem(id, itemId)
    }
}
