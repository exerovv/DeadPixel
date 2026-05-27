package com.exerovv.deadpixel.feature.orders.presentation.detail

sealed interface OrderDetailCommand {
    data object Retry : OrderDetailCommand
}
