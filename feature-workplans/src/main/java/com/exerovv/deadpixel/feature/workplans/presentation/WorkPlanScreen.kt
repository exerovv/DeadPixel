package com.exerovv.deadpixel.feature.workplans.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exerovv.deadpixel.feature.workplans.R
import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlan
import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlanItem
import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlanItemStatus
import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlanStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkPlanScreen(
    onNavigateBack: () -> Unit,
    viewModel: WorkPlanViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val isActionLoading by viewModel.isActionLoading.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.actionError.collect { snackbarHostState.showSnackbar(it) }
    }

    var showPlanStatusDialog by remember { mutableStateOf(false) }
    var itemStatusDialogTarget by remember { mutableStateOf<WorkPlanItem?>(null) }
    var showAddItemDialog by remember { mutableStateOf(false) }
    var deleteConfirmItemId by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.workplan_title),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            if (state is WorkPlanUiState.Success && viewModel.isManager) {
                FloatingActionButton(
                    onClick = { showAddItemDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(R.string.workplan_fab_add_desc))
                }
            }
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
                WorkPlanUiState.Loading -> CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)

                WorkPlanUiState.NotFound -> NotFoundContent(
                    isManager = viewModel.isManager,
                    onCreatePlan = { viewModel.processCommand(WorkPlanCommand.CreatePlan) }
                )

                is WorkPlanUiState.Error -> ErrorContent(
                    message = s.message,
                    onRetry = { viewModel.processCommand(WorkPlanCommand.Retry) }
                )

                is WorkPlanUiState.Success -> {
                    WorkPlanContent(
                        plan = s.plan,
                        isManager = viewModel.isManager,
                        isMaster = viewModel.isMaster,
                        onPlanStatusClick = { showPlanStatusDialog = true },
                        onItemStatusClick = { item -> itemStatusDialogTarget = item },
                        onDeleteItem = { itemId -> deleteConfirmItemId = itemId },
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.TopStart)
                    )
                    if (isActionLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.32f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }

    val planStatusCurrent = (state as? WorkPlanUiState.Success)?.plan?.status
    if (showPlanStatusDialog && planStatusCurrent != null) {
        PlanStatusDialog(
            current = planStatusCurrent,
            onDismiss = { showPlanStatusDialog = false },
            onSelect = { status ->
                viewModel.processCommand(WorkPlanCommand.UpdatePlanStatus(status))
                showPlanStatusDialog = false
            }
        )
    }

    itemStatusDialogTarget?.let { item ->
        ItemStatusDialog(
            current = item.status,
            onDismiss = { itemStatusDialogTarget = null },
            onSelect = { status ->
                viewModel.processCommand(WorkPlanCommand.UpdateItemStatus(item.id, status))
                itemStatusDialogTarget = null
            }
        )
    }

    if (showAddItemDialog) {
        AddItemDialog(
            onDismiss = { showAddItemDialog = false },
            onAdd = { description ->
                viewModel.processCommand(WorkPlanCommand.AddItem(description))
                showAddItemDialog = false
            }
        )
    }

    deleteConfirmItemId?.let { itemId ->
        AlertDialog(
            onDismissRequest = { deleteConfirmItemId = null },
            title = { Text(stringResource(R.string.workplan_dialog_delete_title)) },
            text = { Text(stringResource(R.string.workplan_dialog_delete_message)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.processCommand(WorkPlanCommand.DeleteItem(itemId))
                    deleteConfirmItemId = null
                }) {
                    Text(stringResource(R.string.dialog_delete), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteConfirmItemId = null }) {
                    Text(stringResource(R.string.dialog_cancel))
                }
            }
        )
    }
}

@Composable
private fun WorkPlanContent(
    plan: WorkPlan,
    isManager: Boolean,
    isMaster: Boolean,
    onPlanStatusClick: () -> Unit,
    onItemStatusClick: (WorkPlanItem) -> Unit,
    onDeleteItem: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PlanStatusCard(status = plan.status, isMaster = isMaster, onClick = onPlanStatusClick)
        }
        item {
            Text(
                text = stringResource(R.string.workplan_section_items),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        if (plan.items.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.workplan_items_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(plan.items, key = { it.id }) { item ->
                WorkPlanItemRow(
                    item = item,
                    isMaster = isMaster,
                    isManager = isManager,
                    onStatusClick = { onItemStatusClick(item) },
                    onDelete = { onDeleteItem(item.id) }
                )
            }
        }
    }
}

@Composable
private fun PlanStatusCard(
    status: WorkPlanStatus,
    isMaster: Boolean,
    onClick: () -> Unit
) {
    val statusColor = status.color()
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.workplan_section_status),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            if (isMaster) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = statusColor.copy(alpha = 0.15f),
                    onClick = onClick
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = status.label(),
                            color = statusColor,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Icon(
                            Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = statusColor,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = statusColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = status.label(),
                        color = statusColor,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun WorkPlanItemRow(
    item: WorkPlanItem,
    isMaster: Boolean,
    isManager: Boolean,
    onStatusClick: () -> Unit,
    onDelete: () -> Unit
) {
    val statusColor = item.status.color()
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (isMaster) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = statusColor.copy(alpha = 0.15f),
                        onClick = onStatusClick
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = item.status.label(),
                                color = statusColor,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = statusColor,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = statusColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = item.status.label(),
                            color = statusColor,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            if (isManager) {
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun NotFoundContent(isManager: Boolean, onCreatePlan: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(24.dp)
    ) {
        Text(
            text = stringResource(R.string.workplan_not_found),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        if (isManager) {
            Spacer(Modifier.height(16.dp))
            Button(onClick = onCreatePlan) {
                Text(stringResource(R.string.workplan_create))
            }
        }
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(24.dp)
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.action_retry))
        }
    }
}

@Composable
private fun PlanStatusDialog(
    current: WorkPlanStatus,
    onDismiss: () -> Unit,
    onSelect: (WorkPlanStatus) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.workplan_dialog_plan_status_title)) },
        text = {
            Column {
                WorkPlanStatus.entries.forEach { status ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(status) }
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(selected = status == current, onClick = { onSelect(status) })
                        Spacer(Modifier.width(8.dp))
                        Text(status.label(), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.dialog_cancel))
            }
        }
    )
}

@Composable
private fun ItemStatusDialog(
    current: WorkPlanItemStatus,
    onDismiss: () -> Unit,
    onSelect: (WorkPlanItemStatus) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.workplan_dialog_item_status_title)) },
        text = {
            Column {
                WorkPlanItemStatus.entries.forEach { status ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(status) }
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(selected = status == current, onClick = { onSelect(status) })
                        Spacer(Modifier.width(8.dp))
                        Text(status.label(), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.dialog_cancel))
            }
        }
    )
}

@Composable
private fun AddItemDialog(onDismiss: () -> Unit, onAdd: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.workplan_dialog_add_title)) },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(stringResource(R.string.workplan_dialog_add_hint)) },
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = { onAdd(text.trim()) }, enabled = text.isNotBlank()) {
                Text(stringResource(R.string.dialog_add))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.dialog_cancel))
            }
        }
    )
}
