package com.exerovv.deadpixel.feature.orders.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exerovv.deadpixel.feature.orders.R
import com.exerovv.deadpixel.feature.orders.domain.model.Order
import com.exerovv.deadpixel.feature.orders.domain.model.OrderStatus
import com.exerovv.deadpixel.feature.orders.presentation.state.OrdersUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    modifier: Modifier = Modifier,
    onOrderClick: (Int) -> Unit = {},
    onCreateOrder: () -> Unit = {},
    viewModel: OrdersViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    Box(modifier = modifier.fillMaxSize()) {
        OrdersList(state = state, onRefresh = viewModel::load, onOrderClick = onOrderClick)
        if (viewModel.isManager) {
            FloatingActionButton(
                onClick = onCreateOrder,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(R.string.create_order_fab_desc)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OrdersList(
    state: OrdersUiState,
    onRefresh: () -> Unit,
    onOrderClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    PullToRefreshBox(
        isRefreshing = state.isLoading && state.orders.isNotEmpty(),
        onRefresh = onRefresh,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            state.isLoading && state.orders.isEmpty() -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }

            state.error != null && state.orders.isEmpty() -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = state.error!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = onRefresh) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.action_retry))
                    }
                }
            }

            state.orders.isEmpty() -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.orders_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.orders) { order ->
                    OrderCard(order = order, onClick = { onOrderClick(order.id) })
                }
            }
        }
    }
}

@Composable
private fun OrderCard(order: Order, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#${order.workOrderNumber}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                StatusChip(status = order.status)
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = order.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (order.deadline != null || order.estimatedCost != null) {
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    order.deadline?.let { deadline ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.CalendarToday,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = deadline.take(10),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    order.estimatedCost?.let { cost ->
                        Text(
                            text = "${cost.toInt()} ₽",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun StatusChip(status: OrderStatus, large: Boolean = false) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(statusColor(status))
            .padding(horizontal = if (large) 14.dp else 10.dp, vertical = if (large) 6.dp else 4.dp)
    ) {
        Text(
            text = status.label(),
            style = if (large) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.labelSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

internal fun statusColor(status: OrderStatus): Color = when (status) {
    OrderStatus.RECEIVED -> Color(0xFF7F52FF)
    OrderStatus.DIAGNOSED -> Color(0xFF7F52FF)
    OrderStatus.IN_PROGRESS -> Color(0xFFF88909)
    OrderStatus.WAITING_PARTS -> Color(0xFFD4900A)
    OrderStatus.READY -> Color(0xFF4CAF50)
    OrderStatus.COMPLETED -> Color(0xFF757575)
    OrderStatus.CANCELLED -> Color(0xFFCF6679)
}

@Composable
internal fun OrderStatus.label(): String = when (this) {
    OrderStatus.RECEIVED -> stringResource(R.string.status_received)
    OrderStatus.DIAGNOSED -> stringResource(R.string.status_diagnosed)
    OrderStatus.IN_PROGRESS -> stringResource(R.string.status_in_progress)
    OrderStatus.WAITING_PARTS -> stringResource(R.string.status_waiting_parts)
    OrderStatus.READY -> stringResource(R.string.status_ready)
    OrderStatus.COMPLETED -> stringResource(R.string.status_completed)
    OrderStatus.CANCELLED -> stringResource(R.string.status_cancelled)
}
