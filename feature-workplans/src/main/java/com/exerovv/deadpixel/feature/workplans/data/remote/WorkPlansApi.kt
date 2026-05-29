package com.exerovv.deadpixel.feature.workplans.data.remote

import com.exerovv.deadpixel.feature.workplans.data.remote.dto.AddWorkPlanItemRequest
import com.exerovv.deadpixel.feature.workplans.data.remote.dto.CreateWorkPlanRequest
import com.exerovv.deadpixel.feature.workplans.data.remote.dto.WorkPlanDto
import com.exerovv.deadpixel.feature.workplans.data.remote.dto.WorkPlanItemDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WorkPlansApi {
    @GET("api/workplans/order/{orderId}")
    suspend fun getWorkPlanByOrder(@Path("orderId") orderId: Int): WorkPlanDto

    @POST("api/workplans")
    suspend fun createWorkPlan(@Body request: CreateWorkPlanRequest): WorkPlanDto

    @PATCH("api/workplans/{id}/status")
    suspend fun updateWorkPlanStatus(@Path("id") id: Int, @Query("status") status: String): WorkPlanDto

    @POST("api/workplans/{id}/items")
    suspend fun addWorkPlanItem(@Path("id") id: Int, @Body request: AddWorkPlanItemRequest): WorkPlanItemDto

    @PATCH("api/workplans/{id}/items/{itemId}/status")
    suspend fun updateWorkPlanItemStatus(
        @Path("id") id: Int,
        @Path("itemId") itemId: Int,
        @Query("status") status: String
    ): WorkPlanItemDto

    @DELETE("api/workplans/{id}/items/{itemId}")
    suspend fun deleteWorkPlanItem(@Path("id") id: Int, @Path("itemId") itemId: Int)
}
