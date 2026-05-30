package com.exerovv.deadpixel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exerovv.deadpixel.core.network.TokenManager
import com.exerovv.deadpixel.core.network.UserRole
import com.exerovv.deadpixel.feature.auth.domain.usecase.LogoutUseCase
import com.exerovv.deadpixel.feature.orders.domain.model.Order
import com.exerovv.deadpixel.feature.orders.domain.usecase.GetOverdueOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val getOverdueOrders: GetOverdueOrdersUseCase,
    tokenManager: TokenManager
) : ViewModel() {

    val userId: Int? = tokenManager.getUserId()
    val userRole: UserRole? = tokenManager.getUserRole()
    val isManager = userRole == UserRole.MANAGER

    private val _overdueOrders = MutableStateFlow<List<Order>>(emptyList())
    val overdueOrders: StateFlow<List<Order>> = _overdueOrders.asStateFlow()

    private val _isRefreshingOverdue = MutableStateFlow(false)
    val isRefreshingOverdue: StateFlow<Boolean> = _isRefreshingOverdue.asStateFlow()

    init {
        if (isManager) {
            viewModelScope.launch {
                runCatching { getOverdueOrders() }
                    .onSuccess { _overdueOrders.value = it }
            }
        }
    }

    fun refreshOverdueOrders() {
        if (!isManager || _isRefreshingOverdue.value) return
        viewModelScope.launch {
            _isRefreshingOverdue.value = true
            runCatching { getOverdueOrders() }
                .onSuccess { _overdueOrders.value = it }
            _isRefreshingOverdue.value = false
        }
    }

    fun logout() {
        viewModelScope.launch { logoutUseCase() }
    }
}
