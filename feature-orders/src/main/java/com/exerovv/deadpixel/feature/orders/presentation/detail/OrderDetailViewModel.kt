package com.exerovv.deadpixel.feature.orders.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exerovv.deadpixel.core.network.TokenManager
import com.exerovv.deadpixel.core.network.UserRole
import com.exerovv.deadpixel.feature.orders.domain.model.OrderStatus
import com.exerovv.deadpixel.feature.orders.domain.usecase.AssignMasterUseCase
import com.exerovv.deadpixel.feature.orders.domain.usecase.GetOrderAssignmentUseCase
import com.exerovv.deadpixel.feature.orders.domain.usecase.GetOrderByIdUseCase
import com.exerovv.deadpixel.feature.orders.domain.usecase.GetOrderHistoryUseCase
import com.exerovv.deadpixel.feature.orders.domain.usecase.UpdateOrderStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getOrderById: GetOrderByIdUseCase,
    private val getOrderAssignment: GetOrderAssignmentUseCase,
    private val getOrderHistory: GetOrderHistoryUseCase,
    private val updateOrderStatus: UpdateOrderStatusUseCase,
    private val assignMaster: AssignMasterUseCase,
    tokenManager: TokenManager
) : ViewModel() {

    private val orderId: Int = checkNotNull(savedStateHandle["orderId"])
    val isManager: Boolean = tokenManager.getUserRole() == UserRole.MANAGER
    val isMaster: Boolean = tokenManager.getUserRole() == UserRole.MASTER

    private val _state = MutableStateFlow<OrderDetailUiState>(OrderDetailUiState.Loading)
    val state: StateFlow<OrderDetailUiState> = _state.asStateFlow()

    init { load() }

    fun processCommand(command: OrderDetailCommand) {
        when (command) {
            OrderDetailCommand.Retry -> load()
            is OrderDetailCommand.UpdateStatus -> onUpdateStatus(command.status, command.note)
            is OrderDetailCommand.AssignMaster -> onAssignMaster(command.masterId)
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.value = OrderDetailUiState.Loading
            runCatching { getOrderById(orderId) }
                .onSuccess { order ->
                    val assignmentDeferred = async { runCatching { getOrderAssignment(orderId) }.getOrNull() }
                    val historyDeferred = async { runCatching { getOrderHistory(orderId) }.getOrElse { emptyList() } }
                    _state.value = OrderDetailUiState.Success(order, assignmentDeferred.await(), historyDeferred.await())
                }
                .onFailure { e -> _state.value = OrderDetailUiState.Error(e.message ?: "Ошибка загрузки") }
        }
    }

    private fun onUpdateStatus(status: OrderStatus, note: String?) {
        viewModelScope.launch {
            runCatching { updateOrderStatus(orderId, status, note) }
                .onSuccess { load() }
                .onFailure { e -> _state.value = OrderDetailUiState.Error(e.message ?: "Ошибка") }
        }
    }

    private fun onAssignMaster(masterId: Int) {
        viewModelScope.launch {
            runCatching { assignMaster(orderId, masterId) }
                .onSuccess { load() }
                .onFailure { e -> _state.value = OrderDetailUiState.Error(e.message ?: "Ошибка") }
        }
    }
}
