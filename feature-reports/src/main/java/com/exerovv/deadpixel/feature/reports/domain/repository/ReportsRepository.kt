package com.exerovv.deadpixel.feature.reports.domain.repository

import com.exerovv.deadpixel.feature.reports.domain.model.Report
import com.exerovv.deadpixel.feature.reports.domain.model.ReportType

interface ReportsRepository {
    suspend fun getReports(): List<Report>
    suspend fun getReportById(reportId: Int): Report
    suspend fun getReportsByType(type: ReportType): List<Report>
    suspend fun generateReport(type: ReportType, generatedBy: Int, periodStart: String, periodEnd: String): Report
}
