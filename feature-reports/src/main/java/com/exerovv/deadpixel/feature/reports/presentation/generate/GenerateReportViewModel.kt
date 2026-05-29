package com.exerovv.deadpixel.feature.reports.presentation.generate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exerovv.deadpixel.core.network.TokenManager
import com.exerovv.deadpixel.feature.reports.domain.model.ReportType
import com.exerovv.deadpixel.feature.reports.domain.usecase.GenerateReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenerateReportViewModel @Inject constructor(
    private val generateReport: GenerateReportUseCase,
    tokenManager: TokenManager
) : ViewModel() {

    private val _state = MutableStateFlow<GenerateReportUiState>(GenerateReportUiState.Idle)
    val state = _state.asStateFlow()

    val currentUserId: Int? = tokenManager.getUserId()

    fun generate(type: ReportType, periodStart: String, periodEnd: String) {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            _state.value = GenerateReportUiState.Loading
            runCatching { generateReport(type, userId, periodStart, periodEnd) }
                .onSuccess { _state.value = GenerateReportUiState.Success(it.id) }
                .onFailure { _state.value = GenerateReportUiState.Error(it.message ?: "Ошибка генерации") }
        }
    }

    fun resetError() {
        if (_state.value is GenerateReportUiState.Error) {
            _state.value = GenerateReportUiState.Idle
        }
    }
}
