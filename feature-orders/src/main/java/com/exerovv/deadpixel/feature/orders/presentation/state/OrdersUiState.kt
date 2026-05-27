package com.exerovv.deadpixel.feature.orders.presentation.state

import com.exerovv.deadpixel.feature.orders.domain.model.Order

data class OrdersUiState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
