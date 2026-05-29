package com.exerovv.deadpixel.feature.orders.presentation.create

sealed interface CreateOrderUiState {
    data object Idle : CreateOrderUiState
    data object Loading : CreateOrderUiState
    data class Success(val orderId: Int) : CreateOrderUiState
    data class Error(val message: String) : CreateOrderUiState
}
