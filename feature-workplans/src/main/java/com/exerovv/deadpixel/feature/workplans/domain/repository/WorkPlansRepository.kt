package com.exerovv.deadpixel.feature.workplans.domain.repository

import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlan
import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlanItem
import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlanItemStatus
import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlanStatus

interface WorkPlansRepository {
    suspend fun getWorkPlanByOrder(orderId: Int): WorkPlan?
    suspend fun createWorkPlan(orderId: Int): WorkPlan
    suspend fun updateWorkPlanStatus(id: Int, status: WorkPlanStatus): WorkPlan
    suspend fun addWorkPlanItem(id: Int, description: String): WorkPlanItem
    suspend fun updateWorkPlanItemStatus(id: Int, itemId: Int, status: WorkPlanItemStatus): WorkPlanItem
    suspend fun deleteWorkPlanItem(id: Int, itemId: Int)
}
