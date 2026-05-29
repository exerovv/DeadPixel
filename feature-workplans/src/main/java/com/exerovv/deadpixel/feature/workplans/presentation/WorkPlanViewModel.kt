package com.exerovv.deadpixel.feature.workplans.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exerovv.deadpixel.core.network.TokenManager
import com.exerovv.deadpixel.core.network.UserRole
import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlanItemStatus
import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlanStatus
import com.exerovv.deadpixel.feature.workplans.domain.usecase.AddWorkPlanItemUseCase
import com.exerovv.deadpixel.feature.workplans.domain.usecase.CreateWorkPlanUseCase
import com.exerovv.deadpixel.feature.workplans.domain.usecase.DeleteWorkPlanItemUseCase
import com.exerovv.deadpixel.feature.workplans.domain.usecase.GetWorkPlanByOrderUseCase
import com.exerovv.deadpixel.feature.workplans.domain.usecase.UpdateWorkPlanItemStatusUseCase
import com.exerovv.deadpixel.feature.workplans.domain.usecase.UpdateWorkPlanStatusUseCase
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
class WorkPlanViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getWorkPlanByOrder: GetWorkPlanByOrderUseCase,
    private val createWorkPlan: CreateWorkPlanUseCase,
    private val updatePlanStatus: UpdateWorkPlanStatusUseCase,
    private val addItem: AddWorkPlanItemUseCase,
    private val updateItemStatus: UpdateWorkPlanItemStatusUseCase,
    private val deleteItem: DeleteWorkPlanItemUseCase,
    tokenManager: TokenManager
) : ViewModel() {

    private val orderId: Int = checkNotNull(savedStateHandle["orderId"])
    val isManager: Boolean = tokenManager.getUserRole() == UserRole.MANAGER
    val isMaster: Boolean = tokenManager.getUserRole() == UserRole.MASTER

    private val _state = MutableStateFlow<WorkPlanUiState>(WorkPlanUiState.Loading)
    val state: StateFlow<WorkPlanUiState> = _state.asStateFlow()

    private val _isActionLoading = MutableStateFlow(false)
    val isActionLoading: StateFlow<Boolean> = _isActionLoading.asStateFlow()

    private val _actionError = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val actionError: SharedFlow<String> = _actionError.asSharedFlow()

    init { load() }

    fun processCommand(command: WorkPlanCommand) {
        when (command) {
            WorkPlanCommand.Retry -> load()
            WorkPlanCommand.CreatePlan -> onCreatePlan()
            is WorkPlanCommand.UpdatePlanStatus -> onUpdatePlanStatus(command.status)
            is WorkPlanCommand.UpdateItemStatus -> onUpdateItemStatus(command.itemId, command.status)
            is WorkPlanCommand.AddItem -> onAddItem(command.description)
            is WorkPlanCommand.DeleteItem -> onDeleteItem(command.itemId)
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.value = WorkPlanUiState.Loading
            runCatching { getWorkPlanByOrder(orderId) }
                .onSuccess { plan ->
                    _state.value = if (plan != null) WorkPlanUiState.Success(plan) else WorkPlanUiState.NotFound
                }
                .onFailure { e -> _state.value = WorkPlanUiState.Error(e.message ?: "Ошибка загрузки") }
        }
    }

    private fun onCreatePlan() {
        viewModelScope.launch {
            _state.value = WorkPlanUiState.Loading
            runCatching { createWorkPlan(orderId) }
                .onSuccess { plan -> _state.value = WorkPlanUiState.Success(plan) }
                .onFailure { e -> _state.value = WorkPlanUiState.Error(e.message ?: "Ошибка создания") }
        }
    }

    private fun onUpdatePlanStatus(status: WorkPlanStatus) {
        val current = (_state.value as? WorkPlanUiState.Success) ?: return
        viewModelScope.launch {
            _isActionLoading.value = true
            runCatching { updatePlanStatus(current.plan.id, status) }
                .onSuccess { plan -> _state.value = WorkPlanUiState.Success(plan) }
                .onFailure { e -> _actionError.tryEmit(e.message ?: "Ошибка") }
            _isActionLoading.value = false
        }
    }

    private fun onAddItem(description: String) {
        val current = (_state.value as? WorkPlanUiState.Success) ?: return
        viewModelScope.launch {
            _isActionLoading.value = true
            runCatching { addItem(current.plan.id, description) }
                .onSuccess { _isActionLoading.value = false; load() }
                .onFailure { e -> _isActionLoading.value = false; _actionError.tryEmit(e.message ?: "Ошибка") }
        }
    }

    private fun onUpdateItemStatus(itemId: Int, status: WorkPlanItemStatus) {
        val current = (_state.value as? WorkPlanUiState.Success) ?: return
        viewModelScope.launch {
            _isActionLoading.value = true
            runCatching { updateItemStatus(current.plan.id, itemId, status) }
                .onSuccess { _isActionLoading.value = false; load() }
                .onFailure { e -> _isActionLoading.value = false; _actionError.tryEmit(e.message ?: "Ошибка") }
        }
    }

    private fun onDeleteItem(itemId: Int) {
        val current = (_state.value as? WorkPlanUiState.Success) ?: return
        viewModelScope.launch {
            _isActionLoading.value = true
            runCatching { deleteItem(current.plan.id, itemId) }
                .onSuccess { _isActionLoading.value = false; load() }
                .onFailure { e -> _isActionLoading.value = false; _actionError.tryEmit(e.message ?: "Ошибка") }
        }
    }
}
