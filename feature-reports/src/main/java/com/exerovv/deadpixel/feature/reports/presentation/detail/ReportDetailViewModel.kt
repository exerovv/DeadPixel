package com.exerovv.deadpixel.feature.reports.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exerovv.deadpixel.feature.reports.domain.usecase.GetReportByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getReportById: GetReportByIdUseCase
) : ViewModel() {

    private val reportId: Int = checkNotNull(savedStateHandle["reportId"])

    private val _state = MutableStateFlow<ReportDetailUiState>(ReportDetailUiState.Loading)
    val state = _state.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.value = ReportDetailUiState.Loading
            runCatching { getReportById(reportId) }
                .onSuccess { _state.value = ReportDetailUiState.Success(it) }
                .onFailure { _state.value = ReportDetailUiState.Error(it.message ?: "Ошибка загрузки") }
        }
    }
}
