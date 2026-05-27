package com.exerovv.deadpixel.feature.diagnostics.presentation

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exerovv.deadpixel.feature.diagnostics.R
import com.exerovv.deadpixel.feature.diagnostics.domain.model.DiagnosticStatus
import com.exerovv.deadpixel.feature.diagnostics.domain.model.Diagnostics

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosticsScreen(
    onNavigateBack: () -> Unit,
    viewModel: DiagnosticsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.diagnostics_title), style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.action_back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
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
                DiagnosticsUiState.Loading -> CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )

                DiagnosticsUiState.NotFound -> Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = stringResource(R.string.diagnostics_not_found),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                is DiagnosticsUiState.Error -> Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = s.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { viewModel.processCommand(DiagnosticsCommand.Retry) }) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.action_retry))
                    }
                }

                is DiagnosticsUiState.Success -> DiagnosticsContent(
                    diagnostics = s.diagnostics,
                    onCommand = { viewModel.processCommand(it) },
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.TopStart)
                )
            }
        }
    }
}

@Composable
private fun DiagnosticsContent(
    diagnostics: Diagnostics,
    onCommand: (DiagnosticsCommand) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCompleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatusCard(diagnostics)
        InfoCard(diagnostics)
        if (diagnostics.result != null || diagnostics.detectedIssues != null) {
            ResultCard(diagnostics)
        }
        ActionsCard(
            status = diagnostics.status,
            onSimulate = { onCommand(DiagnosticsCommand.Simulate) },
            onOpenComplete = { showCompleteDialog = true },
            onFail = { onCommand(DiagnosticsCommand.Fail) }
        )
    }

    if (showCompleteDialog) {
        CompleteDialog(
            onConfirm = { result, issues ->
                showCompleteDialog = false
                onCommand(DiagnosticsCommand.Complete(result, issues.ifBlank { null }))
            },
            onDismiss = { showCompleteDialog = false }
        )
    }
}

@Composable
private fun StatusCard(diagnostics: Diagnostics) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            DiagnosticStatusChip(status = diagnostics.status, large = true)
            Spacer(Modifier.height(12.dp))
            InfoRow(
                label = stringResource(R.string.field_performed_by),
                value = stringResource(R.string.field_performed_by_value, diagnostics.performedBy)
            )
        }
    }
}

@Composable
private fun InfoCard(diagnostics: Diagnostics) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SectionLabel(stringResource(R.string.diagnostics_section_info))
            InfoRow(stringResource(R.string.field_started_at), diagnostics.startedAt.take(16).replace("T", " "))
            diagnostics.completedAt?.let {
                InfoRow(stringResource(R.string.field_completed_at), it.take(16).replace("T", " "))
            }
        }
    }
}

@Composable
private fun ResultCard(diagnostics: Diagnostics) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SectionLabel(stringResource(R.string.diagnostics_section_result))
            diagnostics.result?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            diagnostics.detectedIssues?.let {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.field_detected_issues),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun ActionsCard(
    status: DiagnosticStatus,
    onSimulate: () -> Unit,
    onOpenComplete: () -> Unit,
    onFail: () -> Unit
) {
    if (status == DiagnosticStatus.COMPLETED || status == DiagnosticStatus.FAILED) return

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SectionLabel(stringResource(R.string.diagnostics_section_actions))
            when (status) {
                DiagnosticStatus.PENDING -> Button(
                    onClick = onSimulate,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.action_simulate))
                }
                DiagnosticStatus.IN_PROGRESS -> {
                    Button(
                        onClick = onOpenComplete,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.action_complete))
                    }
                    Button(
                        onClick = onFail,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(stringResource(R.string.action_fail))
                    }
                }
                else -> Unit
            }
        }
    }
}

@Composable
private fun CompleteDialog(
    onConfirm: (result: String, detectedIssues: String) -> Unit,
    onDismiss: () -> Unit
) {
    var result by remember { mutableStateOf("") }
    var detectedIssues by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_complete_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = result,
                    onValueChange = { result = it },
                    label = { Text(stringResource(R.string.dialog_hint_result)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
                OutlinedTextField(
                    value = detectedIssues,
                    onValueChange = { detectedIssues = it },
                    label = { Text(stringResource(R.string.dialog_hint_issues)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(result, detectedIssues) },
                enabled = result.isNotBlank()
            ) {
                Text(stringResource(R.string.dialog_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.dialog_cancel))
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    )
}

@Composable
private fun DiagnosticStatusChip(status: DiagnosticStatus, large: Boolean = false) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(diagnosticStatusColor(status))
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

private fun diagnosticStatusColor(status: DiagnosticStatus): Color = when (status) {
    DiagnosticStatus.PENDING -> Color(0xFFC0C0C2)
    DiagnosticStatus.IN_PROGRESS -> Color(0xFFF88909)
    DiagnosticStatus.COMPLETED -> Color(0xFF4CAF50)
    DiagnosticStatus.FAILED -> Color(0xFFCF6679)
}

@Composable
private fun DiagnosticStatus.label(): String = when (this) {
    DiagnosticStatus.PENDING -> stringResource(R.string.status_pending)
    DiagnosticStatus.IN_PROGRESS -> stringResource(R.string.status_in_progress)
    DiagnosticStatus.COMPLETED -> stringResource(R.string.status_completed)
    DiagnosticStatus.FAILED -> stringResource(R.string.status_failed)
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
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Bold
    )
}
