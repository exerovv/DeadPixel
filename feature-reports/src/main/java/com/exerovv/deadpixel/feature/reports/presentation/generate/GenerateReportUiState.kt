package com.exerovv.deadpixel.feature.reports.presentation.generate

sealed interface GenerateReportUiState {
    data object Idle : GenerateReportUiState
    data object Loading : GenerateReportUiState
    data class Success(val reportId: Int) : GenerateReportUiState
    data class Error(val message: String) : GenerateReportUiState
}
