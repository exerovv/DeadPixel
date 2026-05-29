package com.exerovv.deadpixel.feature.reports.domain.usecase

import com.exerovv.deadpixel.feature.reports.domain.model.ReportType
import com.exerovv.deadpixel.feature.reports.domain.repository.ReportsRepository
import javax.inject.Inject

class GenerateReportUseCase @Inject constructor(private val repository: ReportsRepository) {
    suspend operator fun invoke(type: ReportType, generatedBy: Int, periodStart: String, periodEnd: String) =
        repository.generateReport(type, generatedBy, periodStart, periodEnd)
}
