package com.exerovv.deadpixel.feature.orders.presentation.detail

import com.exerovv.deadpixel.feature.orders.domain.model.Order
import com.exerovv.deadpixel.feature.orders.domain.model.OrderAssignment
import com.exerovv.deadpixel.feature.orders.domain.model.OrderStatusHistory

sealed interface OrderDetailUiState {
    data object Loading : OrderDetailUiState
    data class Success(
        val order: Order,
        val assignment: OrderAssignment?,
        val history: List<OrderStatusHistory>
    ) : OrderDetailUiState
    data class Error(val message: String) : OrderDetailUiState
}
