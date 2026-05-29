package com.exerovv.deadpixel.feature.reports.domain.usecase

import com.exerovv.deadpixel.feature.reports.domain.repository.ReportsRepository
import javax.inject.Inject

class GetReportByIdUseCase @Inject constructor(private val repository: ReportsRepository) {
    suspend operator fun invoke(reportId: Int) = repository.getReportById(reportId)
}
