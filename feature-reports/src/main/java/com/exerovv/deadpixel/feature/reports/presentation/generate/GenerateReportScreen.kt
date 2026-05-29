package com.exerovv.deadpixel.feature.reports.presentation.generate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exerovv.deadpixel.feature.reports.R
import com.exerovv.deadpixel.feature.reports.domain.model.ReportType
import com.exerovv.deadpixel.feature.reports.presentation.label

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateReportScreen(
    onNavigateBack: () -> Unit,
    onGenerated: (Int) -> Unit,
    viewModel: GenerateReportViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var type by remember { mutableStateOf(ReportType.COMPLETED_ORDERS) }
    var typeExpanded by remember { mutableStateOf(false) }
    var periodStart by remember { mutableStateOf("") }
    var periodEnd by remember { mutableStateOf("") }

    LaunchedEffect(state) {
        when (val s = state) {
            is GenerateReportUiState.Success -> onGenerated(s.reportId)
            is GenerateReportUiState.Error -> {
                snackbarHostState.showSnackbar(s.message)
                viewModel.resetError()
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.generate_report_title), style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.action_back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = typeExpanded,
                    onExpandedChange = { typeExpanded = it }
                ) {
                    OutlinedTextField(
                        value = type.label(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.generate_report_field_type)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = typeExpanded,
                        onDismissRequest = { typeExpanded = false }
                    ) {
                        ReportType.entries.forEach { t ->
                            DropdownMenuItem(
                                text = { Text(t.label()) },
                                onClick = { type = t; typeExpanded = false },
                                trailingIcon = if (type == t) {
                                    { Icon(Icons.Default.Check, null, modifier = Modifier.size(16.dp)) }
                                } else null
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = periodStart,
                    onValueChange = { periodStart = it },
                    label = { Text(stringResource(R.string.generate_report_field_period_start)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(stringResource(R.string.generate_report_placeholder_start)) },
                    singleLine = true
                )
                OutlinedTextField(
                    value = periodEnd,
                    onValueChange = { periodEnd = it },
                    label = { Text(stringResource(R.string.generate_report_field_period_end)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(stringResource(R.string.generate_report_placeholder_end)) },
                    singleLine = true
                )

                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        viewModel.generate(type, "${periodStart}T00:00:00", "${periodEnd}T23:59:59")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state !is GenerateReportUiState.Loading
                            && periodStart.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))
                            && periodEnd.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))
                ) {
                    if (state is GenerateReportUiState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = Color.White)
                    } else {
                        Text(stringResource(R.string.generate_report_button))
                    }
                }
            }
        }
    }
}
