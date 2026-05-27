package com.exerovv.deadpixel.feature.orders.presentation.detail

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exerovv.deadpixel.feature.orders.R
import com.exerovv.deadpixel.feature.orders.domain.model.Order
import com.exerovv.deadpixel.feature.orders.domain.model.OrderAssignment
import com.exerovv.deadpixel.feature.orders.domain.model.OrderStatus
import com.exerovv.deadpixel.feature.orders.domain.model.OrderStatusHistory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: OrderDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val title = (state as? OrderDetailUiState.Success)
                        ?.order?.workOrderNumber?.let { "#$it" }
                        ?: stringResource(R.string.order_detail_fallback_title)
                    Text(text = title, style = MaterialTheme.typography.titleLarge)
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.action_back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (val s = state) {
                OrderDetailUiState.Loading -> CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )

                is OrderDetailUiState.Error -> Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = s.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { viewModel.processCommand(OrderDetailCommand.Retry) }) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.action_retry))
                    }
                }

                is OrderDetailUiState.Success -> OrderDetailContent(
                    order = s.order,
                    assignment = s.assignment,
                    history = s.history,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun OrderDetailContent(
    order: Order,
    assignment: OrderAssignment?,
    history: List<OrderStatusHistory>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            horizontal = 16.dp,
            vertical = 12.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { StatusSection(order) }
        item { InfoSection(order) }
        if (assignment != null) {
            item { AssignmentSection(assignment) }
        }
        if (history.isNotEmpty()) {
            item {
                SectionLabel(stringResource(R.string.section_history))
            }
            items(history) { entry ->
                HistoryItem(entry)
            }
        }
    }
}

@Composable
private fun StatusSection(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            StatusChip(status = order.status, large = true)
            Spacer(Modifier.height(12.dp))
            Text(
                text = order.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun InfoSection(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SectionLabel(stringResource(R.string.section_info))
            InfoRow(stringResource(R.string.field_created_at), order.createdAt.take(10))
            order.deadline?.let { InfoRow(stringResource(R.string.field_deadline), it.take(10)) }
            order.estimatedCost?.let { InfoRow(stringResource(R.string.field_cost), "${it.toInt()} ₽") }
        }
    }
}

@Composable
private fun AssignmentSection(assignment: OrderAssignment) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SectionLabel(stringResource(R.string.section_master))
            InfoRow(stringResource(R.string.field_master_id), assignment.masterId.toString())
            InfoRow(stringResource(R.string.field_assigned_at), assignment.assignedAt.take(10))
        }
    }
}

@Composable
private fun HistoryItem(entry: OrderStatusHistory) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(statusColor(entry.newStatus))
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                entry.previousStatus?.let {
                    Text(
                        text = it.label(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.width(4.dp))
                }
                Text(
                    text = entry.newStatus.label(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = statusColor(entry.newStatus)
                )
            }
            Text(
                text = entry.changedAt.take(16).replace("T", " "),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            entry.note?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun StatusChip(status: OrderStatus, large: Boolean = false) {
    val color = statusColor(status)
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = if (large) 14.dp else 10.dp, vertical = if (large) 6.dp else 4.dp)
    ) {
        Text(
            text = status.label(),
            style = if (large) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun statusColor(status: OrderStatus): Color = when (status) {
    OrderStatus.RECEIVED -> Color(0xFF7F52FF)
    OrderStatus.DIAGNOSED -> Color(0xFF7F52FF)
    OrderStatus.IN_PROGRESS -> Color(0xFFF88909)
    OrderStatus.WAITING_PARTS -> Color(0xFFFFC107)
    OrderStatus.READY -> Color(0xFF4CAF50)
    OrderStatus.COMPLETED -> Color(0xFFC0C0C2)
    OrderStatus.CANCELLED -> Color(0xFFCF6679)
}

@Composable
private fun OrderStatus.label(): String = when (this) {
    OrderStatus.RECEIVED -> stringResource(R.string.status_received)
    OrderStatus.DIAGNOSED -> stringResource(R.string.status_diagnosed)
    OrderStatus.IN_PROGRESS -> stringResource(R.string.status_in_progress)
    OrderStatus.WAITING_PARTS -> stringResource(R.string.status_waiting_parts)
    OrderStatus.READY -> stringResource(R.string.status_ready)
    OrderStatus.COMPLETED -> stringResource(R.string.status_completed)
    OrderStatus.CANCELLED -> stringResource(R.string.status_cancelled)
}
