package com.exerovv.deadpixel.feature.reports.data.remote

import com.exerovv.deadpixel.feature.reports.data.remote.dto.GenerateReportRequest
import com.exerovv.deadpixel.feature.reports.data.remote.dto.ReportDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReportsApi {

    @GET("api/reports")
    suspend fun getReports(): List<ReportDto>

    @GET("api/reports/{id}")
    suspend fun getReportById(@Path("id") reportId: Int): ReportDto

    @GET("api/reports/type/{type}")
    suspend fun getReportsByType(@Path("type") type: String): List<ReportDto>

    @POST("api/reports")
    suspend fun generateReport(@Body request: GenerateReportRequest): ReportDto
}
