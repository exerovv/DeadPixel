package com.exerovv.deadpixel.feature.diagnostics.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exerovv.deadpixel.feature.diagnostics.domain.usecase.CompleteDiagnosticsUseCase
import com.exerovv.deadpixel.feature.diagnostics.domain.usecase.FailDiagnosticsUseCase
import com.exerovv.deadpixel.feature.diagnostics.domain.usecase.GetDiagnosticsByOrderUseCase
import com.exerovv.deadpixel.feature.diagnostics.domain.usecase.SimulateDiagnosticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiagnosticsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDiagnosticsByOrder: GetDiagnosticsByOrderUseCase,
    private val simulate: SimulateDiagnosticsUseCase,
    private val complete: CompleteDiagnosticsUseCase,
    private val fail: FailDiagnosticsUseCase
) : ViewModel() {

    private val orderId: Int = checkNotNull(savedStateHandle["orderId"])

    private val _state = MutableStateFlow<DiagnosticsUiState>(DiagnosticsUiState.Loading)
    val state: StateFlow<DiagnosticsUiState> = _state.asStateFlow()

    private val _isActionLoading = MutableStateFlow(false)
    val isActionLoading: StateFlow<Boolean> = _isActionLoading.asStateFlow()

    private val _actionError = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val actionError: SharedFlow<String> = _actionError.asSharedFlow()

    init { load() }

    fun processCommand(command: DiagnosticsCommand) {
        when (command) {
            DiagnosticsCommand.Retry -> load()
            DiagnosticsCommand.Simulate -> onSimulate()
            is DiagnosticsCommand.Complete -> onComplete(command.result, command.detectedIssues)
            DiagnosticsCommand.Fail -> onFail()
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.value = DiagnosticsUiState.Loading
            runCatching { getDiagnosticsByOrder(orderId) }
                .onSuccess { data ->
                    _state.value = if (data != null) DiagnosticsUiState.Success(data)
                                   else DiagnosticsUiState.NotFound
                }
                .onFailure { e -> _state.value = DiagnosticsUiState.Error(e.message ?: "Ошибка загрузки") }
        }
    }

    private fun onSimulate() {
        val current = (_state.value as? DiagnosticsUiState.Success) ?: return
        viewModelScope.launch {
            _isActionLoading.value = true
            runCatching { simulate(current.diagnostics.id) }
                .onSuccess { _state.value = DiagnosticsUiState.Success(it) }
                .onFailure { e -> _actionError.tryEmit(e.message ?: "Ошибка") }
            _isActionLoading.value = false
        }
    }

    private fun onComplete(result: String, detectedIssues: String?) {
        val current = (_state.value as? DiagnosticsUiState.Success) ?: return
        viewModelScope.launch {
            _isActionLoading.value = true
            runCatching { complete(current.diagnostics.id, result, detectedIssues) }
                .onSuccess { _isActionLoading.value = false; load() }
                .onFailure { e -> _isActionLoading.value = false; _actionError.tryEmit(e.message ?: "Ошибка") }
        }
    }

    private fun onFail() {
        val current = (_state.value as? DiagnosticsUiState.Success) ?: return
        viewModelScope.launch {
            _isActionLoading.value = true
            runCatching { fail(current.diagnostics.id) }
                .onSuccess { _isActionLoading.value = false; load() }
                .onFailure { e -> _isActionLoading.value = false; _actionError.tryEmit(e.message ?: "Ошибка") }
        }
    }
}
