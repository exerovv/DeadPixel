package com.exerovv.deadpixel.feature.diagnostics.presentation

import com.exerovv.deadpixel.feature.diagnostics.domain.model.Diagnostics

sealed interface DiagnosticsUiState {
    data object Loading : DiagnosticsUiState
    data class Success(val diagnostics: Diagnostics) : DiagnosticsUiState
    data object NotFound : DiagnosticsUiState
    data class Error(val message: String) : DiagnosticsUiState
}
