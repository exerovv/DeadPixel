package com.exerovv.deadpixel.feature.reports.data.repository

import com.exerovv.deadpixel.feature.reports.data.mapper.toDomain
import com.exerovv.deadpixel.feature.reports.data.remote.ReportsApi
import com.exerovv.deadpixel.feature.reports.data.remote.dto.GenerateReportRequest
import com.exerovv.deadpixel.feature.reports.domain.model.Report
import com.exerovv.deadpixel.feature.reports.domain.model.ReportType
import com.exerovv.deadpixel.feature.reports.domain.repository.ReportsRepository
import javax.inject.Inject

class ReportsRepositoryImpl @Inject constructor(
    private val api: ReportsApi
) : ReportsRepository {

    override suspend fun getReports(): List<Report> =
        api.getReports().map { it.toDomain() }

    override suspend fun getReportById(reportId: Int): Report =
        api.getReportById(reportId).toDomain()

    override suspend fun getReportsByType(type: ReportType): List<Report> =
        api.getReportsByType(type.name).map { it.toDomain() }

    override suspend fun generateReport(
        type: ReportType,
        generatedBy: Int,
        periodStart: String,
        periodEnd: String
    ): Report = api.generateReport(GenerateReportRequest(type, generatedBy, periodStart, periodEnd)).toDomain()
}
