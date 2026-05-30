package com.exerovv.deadpixel.feature.workplans.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.exerovv.deadpixel.feature.workplans.R
import com.exerovv.deadpixel.feature.workplans.domain.model.WorkPlanItemStatus

@Composable
fun WorkPlanItemStatus.label(): String = when (this) {
    WorkPlanItemStatus.PENDING -> stringResource(R.string.workplan_item_status_pending)
    WorkPlanItemStatus.IN_PROGRESS -> stringResource(R.string.workplan_item_status_in_progress)
    WorkPlanItemStatus.COMPLETED -> stringResource(R.string.workplan_item_status_completed)
    WorkPlanItemStatus.SKIPPED -> stringResource(R.string.workplan_item_status_skipped)
}

@Composable
fun WorkPlanItemStatus.color(): Color = when (this) {
    WorkPlanItemStatus.PENDING -> MaterialTheme.colorScheme.onSurfaceVariant
    WorkPlanItemStatus.IN_PROGRESS -> MaterialTheme.colorScheme.primary
    WorkPlanItemStatus.COMPLETED -> Color(0xFF4CAF50)
    WorkPlanItemStatus.SKIPPED -> MaterialTheme.colorScheme.secondary
}
