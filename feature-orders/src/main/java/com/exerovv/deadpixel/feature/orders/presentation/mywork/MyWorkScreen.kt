package com.exerovv.deadpixel.feature.orders.presentation.mywork

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.exerovv.deadpixel.feature.orders.presentation.OrdersList

@Composable
fun MyWorkScreen(
    modifier: Modifier = Modifier,
    onOrderClick: (Int) -> Unit = {},
    viewModel: MyWorkViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    OrdersList(state = state, onRefresh = viewModel::load, onOrderClick = onOrderClick, modifier = modifier)
}
