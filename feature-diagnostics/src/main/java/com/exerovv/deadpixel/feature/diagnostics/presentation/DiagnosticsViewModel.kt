package com.exerovv.deadpixel.feature.diagnostics.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exerovv.deadpixel.feature.diagnostics.domain.usecase.CompleteDiagnosticsUseCase
import com.exerovv.deadpixel.feature.diagnostics.domain.usecase.FailDiagnosticsUseCase
import com.exerovv.deadpixel.feature.diagnostics.domain.usecase.GetDiagnosticsByOrderUseCase
import com.exerovv.deadpixel.feature.diagnostics.domain.usecase.SimulateDiagnosticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
            runCatching { simulate(current.diagnostics.id) }
                .onSuccess { _state.value = DiagnosticsUiState.Success(it) }
                .onFailure { e -> _state.value = DiagnosticsUiState.Error(e.message ?: "Ошибка") }
        }
    }

    private fun onComplete(result: String, detectedIssues: String?) {
        val current = (_state.value as? DiagnosticsUiState.Success) ?: return
        viewModelScope.launch {
            runCatching { complete(current.diagnostics.id, result, detectedIssues) }
                .onSuccess { load() }
                .onFailure { e -> _state.value = DiagnosticsUiState.Error(e.message ?: "Ошибка") }
        }
    }

    private fun onFail() {
        val current = (_state.value as? DiagnosticsUiState.Success) ?: return
        viewModelScope.launch {
            runCatching { fail(current.diagnostics.id) }
                .onSuccess { load() }
                .onFailure { e -> _state.value = DiagnosticsUiState.Error(e.message ?: "Ошибка") }
        }
    }
}
