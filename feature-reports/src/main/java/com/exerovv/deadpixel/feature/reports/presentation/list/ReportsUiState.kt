package com.exerovv.deadpixel.feature.reports.presentation.list

import com.exerovv.deadpixel.feature.reports.domain.model.Report
import com.exerovv.deadpixel.feature.reports.domain.model.ReportType

data class ReportsUiState(
    val reports: List<Report> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedType: ReportType? = null
) {
    val filtered: List<Report>
        get() = if (selectedType == null) reports else reports.filter { it.type == selectedType }
}
