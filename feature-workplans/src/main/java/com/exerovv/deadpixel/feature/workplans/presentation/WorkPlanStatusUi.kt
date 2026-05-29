package com.exerovv.deadpixel.feature.workplans.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.exerovv.deadpixel.feature.workplans.R
import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlanStatus

@Composable
fun WorkPlanStatus.label(): String = when (this) {
    WorkPlanStatus.PENDING -> stringResource(R.string.workplan_status_pending)
    WorkPlanStatus.IN_PROGRESS -> stringResource(R.string.workplan_status_in_progress)
    WorkPlanStatus.COMPLETED -> stringResource(R.string.workplan_status_completed)
    WorkPlanStatus.CANCELLED -> stringResource(R.string.workplan_status_cancelled)
}

@Composable
fun WorkPlanStatus.color(): Color = when (this) {
    WorkPlanStatus.PENDING -> MaterialTheme.colorScheme.onSurfaceVariant
    WorkPlanStatus.IN_PROGRESS -> MaterialTheme.colorScheme.primary
    WorkPlanStatus.COMPLETED -> Color(0xFF4CAF50)
    WorkPlanStatus.CANCELLED -> MaterialTheme.colorScheme.error
}
