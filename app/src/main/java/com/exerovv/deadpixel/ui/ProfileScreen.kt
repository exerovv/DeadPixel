package com.exerovv.deadpixel.ui

import android.content.Intent
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.exerovv.deadpixel.R
import com.exerovv.deadpixel.core.network.UserRole
import com.exerovv.deadpixel.feature.orders.domain.model.Order

@Composable
fun ProfileScreen(
    userId: Int?,
    userRole: UserRole?,
    overdueOrders: List<Order>,
    isRefreshingOverdue: Boolean,
    onRefreshOverdueOrders: (() -> Unit)?,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var contactOrder by remember { mutableStateOf<Order?>(null) }

    val initials = when (userRole) {
        UserRole.MASTER -> "М"
        UserRole.MANAGER -> "МН"
        else -> "?"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (onRefreshOverdueOrders != null) {
            // Менеджер: прокручиваемая область сверху, кнопка выхода снизу
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(48.dp))
                ProfileAvatar(initials)
                Spacer(Modifier.height(12.dp))
                if (userRole != null) RoleChip(role = userRole)
                if (userId != null) {
                    Spacer(Modifier.height(6.dp))
                    ProfileIdText(userId)
                }
                Spacer(Modifier.height(28.dp))
                OverdueOrdersSection(
                    orders = overdueOrders,
                    isRefreshing = isRefreshingOverdue,
                    onRefresh = onRefreshOverdueOrders,
                    onOrderClick = { contactOrder = it }
                )
                Spacer(Modifier.height(24.dp))
            }
            LogoutButton(onLogout)
        } else {
            // Мастер: равные спейсеры сверху и снизу — контент в центре экрана
            Spacer(Modifier.weight(1f))
            ProfileAvatar(initials)
            Spacer(Modifier.height(12.dp))
            if (userRole != null) RoleChip(role = userRole)
            if (userId != null) {
                Spacer(Modifier.height(6.dp))
                ProfileIdText(userId)
            }
            Spacer(Modifier.height(32.dp))
            LogoutButton(onLogout)
            Spacer(Modifier.weight(1f))
        }
    }

    contactOrder?.let { order ->
        ContactClientDialog(
            order = order,
            onDismiss = { contactOrder = null }
        )
    }
}

@Composable
private fun OverdueOrdersSection(
    orders: List<Order>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onOrderClick: (Order) -> Unit
) {
    var expanded by remember { mutableStateOf(true) }
    val textColor = if (orders.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant
                    else MaterialTheme.colorScheme.onErrorContainer

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (orders.isEmpty()) MaterialTheme.colorScheme.surfaceVariant
                             else MaterialTheme.colorScheme.errorContainer
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    if (orders.isNotEmpty()) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = stringResource(R.string.profile_overdue_title),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(0.dp)) {
                    if (isRefreshing) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(32.dp)
                                .padding(7.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        IconButton(
                            onClick = onRefresh,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = stringResource(R.string.profile_overdue_refresh_desc),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    IconButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (expanded)
                                stringResource(R.string.profile_overdue_collapse)
                            else
                                stringResource(R.string.profile_overdue_expand),
                            tint = textColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(Modifier.height(8.dp))
                    if (orders.isEmpty()) {
                        Text(
                            text = stringResource(R.string.profile_overdue_empty),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        orders.forEach { order ->
                            TextButton(
                                onClick = { onOrderClick(order) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "#${order.workOrderNumber} — ${order.description.take(40)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactClientDialog(order: Order, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val defaultMessage = stringResource(R.string.profile_overdue_contact_default, order.workOrderNumber)
    var message by remember { mutableStateOf(defaultMessage) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.profile_overdue_contact_title)) },
        text = {
            Column {
                Text(
                    text = "#${order.workOrderNumber}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text(stringResource(R.string.profile_overdue_contact_message_hint)) },
                    minLines = 3,
                    maxLines = 6,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, message)
                    }
                    context.startActivity(Intent.createChooser(intent, null))
                    onDismiss()
                },
                enabled = message.isNotBlank()
            ) {
                Text(stringResource(R.string.action_open_messenger))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(android.R.string.cancel))
            }
        }
    )
}

@Composable
private fun ProfileAvatar(initials: String) {
    Box(
        modifier = Modifier
            .size(88.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ProfileIdText(userId: Int) {
    Text(
        text = stringResource(R.string.profile_id, userId),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun LogoutButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .wrapContentWidth()
            .padding(bottom = 32.dp, top = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error
        )
    ) {
        Icon(
            Icons.AutoMirrored.Filled.Logout,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.size(8.dp))
        Text(stringResource(R.string.action_logout))
    }
}

@Composable
private fun RoleChip(role: UserRole) {
    val label = if (role == UserRole.MASTER) stringResource(R.string.role_master)
                else stringResource(R.string.role_manager)
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondary)
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}
