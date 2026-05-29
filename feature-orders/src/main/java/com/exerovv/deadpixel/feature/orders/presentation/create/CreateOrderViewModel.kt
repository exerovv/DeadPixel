package com.exerovv.deadpixel.feature.orders.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exerovv.deadpixel.feature.orders.domain.usecase.CreateOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateOrderViewModel @Inject constructor(
    private val createOrder: CreateOrderUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<CreateOrderUiState>(CreateOrderUiState.Idle)
    val state: StateFlow<CreateOrderUiState> = _state.asStateFlow()

    fun submit(equipmentId: Int, description: String, deadline: String?, estimatedCost: Double?) {
        viewModelScope.launch {
            _state.value = CreateOrderUiState.Loading
            runCatching { createOrder(equipmentId, description, deadline, estimatedCost) }
                .onSuccess { order -> _state.value = CreateOrderUiState.Success(order.id) }
                .onFailure { e -> _state.value = CreateOrderUiState.Error(e.message ?: "Ошибка создания") }
        }
    }
}
