package com.exerovv.deadpixel.feature.reports.domain.usecase

import com.exerovv.deadpixel.feature.reports.domain.repository.ReportsRepository
import javax.inject.Inject

class GetReportsUseCase @Inject constructor(private val repository: ReportsRepository) {
    suspend operator fun invoke() = repository.getReports()
}
