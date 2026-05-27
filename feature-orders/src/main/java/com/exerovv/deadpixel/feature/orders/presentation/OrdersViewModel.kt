package com.exerovv.deadpixel.feature.orders.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exerovv.deadpixel.core.network.ApiResult
import com.exerovv.deadpixel.feature.orders.domain.usecase.GetOrdersUseCase
import com.exerovv.deadpixel.feature.orders.presentation.state.OrdersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getOrders: GetOrdersUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(OrdersUiState())
    val state: StateFlow<OrdersUiState> = _state.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = getOrders()) {
                is ApiResult.Success -> _state.update { it.copy(orders = result.data, isLoading = false) }
                is ApiResult.Error -> _state.update { it.copy(error = result.message ?: "Ошибка загрузки", isLoading = false) }
            }
        }
    }
}
