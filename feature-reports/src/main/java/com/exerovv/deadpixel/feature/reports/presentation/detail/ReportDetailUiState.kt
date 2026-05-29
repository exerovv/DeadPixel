package com.exerovv.deadpixel.feature.reports.presentation.detail

import com.exerovv.deadpixel.feature.reports.domain.model.Report

sealed interface ReportDetailUiState {
    data object Loading : ReportDetailUiState
    data class Success(val report: Report) : ReportDetailUiState
    data class Error(val message: String) : ReportDetailUiState
}
