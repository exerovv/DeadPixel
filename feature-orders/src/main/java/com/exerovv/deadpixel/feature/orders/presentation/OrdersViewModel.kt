package com.exerovv.deadpixel.feature.orders.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exerovv.deadpixel.core.network.TokenManager
import com.exerovv.deadpixel.core.network.UserRole
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
    private val getOrders: GetOrdersUseCase,
    tokenManager: TokenManager
) : ViewModel() {

    val isManager: Boolean = tokenManager.getUserRole() == UserRole.MANAGER

    private val _state = MutableStateFlow(OrdersUiState())
    val state: StateFlow<OrdersUiState> = _state.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            runCatching { getOrders() }
                .onSuccess { orders -> _state.update { it.copy(orders = orders, isLoading = false) } }
                .onFailure { e -> _state.update { it.copy(error = e.message ?: "Ошибка загрузки", isLoading = false) } }
        }
    }
}
