package com.exerovv.deadpixel.feature.orders.presentation.mywork

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exerovv.deadpixel.core.network.TokenManager
import com.exerovv.deadpixel.core.network.UserRole
import com.exerovv.deadpixel.feature.orders.domain.usecase.GetOrdersByMasterUseCase
import com.exerovv.deadpixel.feature.orders.domain.usecase.GetOrdersByStatusUseCase
import com.exerovv.deadpixel.feature.orders.presentation.state.OrdersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyWorkViewModel @Inject constructor(
    tokenManager: TokenManager,
    private val getOrdersByMaster: GetOrdersByMasterUseCase,
    private val getOrdersByStatus: GetOrdersByStatusUseCase
) : ViewModel() {

    private val role = tokenManager.getUserRole()
    private val userId = tokenManager.getUserId()

    private val _state = MutableStateFlow(OrdersUiState())
    val state: StateFlow<OrdersUiState> = _state.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            runCatching {
                when (role) {
                    UserRole.MASTER -> getOrdersByMaster(userId ?: 0)
                    UserRole.MANAGER -> getOrdersByStatus("RECEIVED")
                    null -> emptyList()
                }
            }
                .onSuccess { orders -> _state.update { it.copy(orders = orders, isLoading = false) } }
                .onFailure { e -> _state.update { it.copy(error = e.message ?: "Ошибка загрузки", isLoading = false) } }
        }
    }
}
