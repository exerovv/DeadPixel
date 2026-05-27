package com.exerovv.deadpixel.feature.orders.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exerovv.deadpixel.core.network.ApiResult
import com.exerovv.deadpixel.feature.orders.domain.usecase.GetOrderAssignmentUseCase
import com.exerovv.deadpixel.feature.orders.domain.usecase.GetOrderByIdUseCase
import com.exerovv.deadpixel.feature.orders.domain.usecase.GetOrderHistoryUseCase
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
    private val getOrderHistory: GetOrderHistoryUseCase
) : ViewModel() {

    private val orderId: Int = checkNotNull(savedStateHandle["orderId"])

    private val _state = MutableStateFlow<OrderDetailUiState>(OrderDetailUiState.Loading)
    val state: StateFlow<OrderDetailUiState> = _state.asStateFlow()

    init { load() }

    fun processCommand(command: OrderDetailCommand) {
        when (command) {
            OrderDetailCommand.Retry -> load()
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.value = OrderDetailUiState.Loading

            val orderResult = getOrderById(orderId)
            if (orderResult is ApiResult.Error) {
                _state.value = OrderDetailUiState.Error(orderResult.message ?: "Ошибка загрузки")
                return@launch
            }
            val order = (orderResult as ApiResult.Success).data

            val assignmentDeferred = async { getOrderAssignment(orderId) }
            val historyDeferred = async { getOrderHistory(orderId) }

            val assignment = (assignmentDeferred.await() as? ApiResult.Success)?.data
            val history = (historyDeferred.await() as? ApiResult.Success)?.data ?: emptyList()

            _state.value = OrderDetailUiState.Success(order, assignment, history)
        }
    }
}
