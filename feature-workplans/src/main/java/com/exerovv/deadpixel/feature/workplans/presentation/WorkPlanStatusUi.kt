package com.exerovv.deadpixel.feature.workplans.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.exerovv.deadpixel.feature.workplans.R
import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlanStatus

@Composable
fun WorkPlanStatus.label(): String = when (this) {
    WorkPlanStatus.DRAFT -> stringResource(R.string.workplan_status_draft)
    WorkPlanStatus.ACTIVE -> stringResource(R.string.workplan_status_active)
    WorkPlanStatus.COMPLETED -> stringResource(R.string.workplan_status_completed)
    WorkPlanStatus.CANCELLED -> stringResource(R.string.workplan_status_cancelled)
}

@Composable
fun WorkPlanStatus.color(): Color = when (this) {
    WorkPlanStatus.DRAFT -> MaterialTheme.colorScheme.onSurfaceVariant
    WorkPlanStatus.ACTIVE -> MaterialTheme.colorScheme.primary
    WorkPlanStatus.COMPLETED -> Color(0xFF4CAF50)
    WorkPlanStatus.CANCELLED -> MaterialTheme.colorScheme.error
}
