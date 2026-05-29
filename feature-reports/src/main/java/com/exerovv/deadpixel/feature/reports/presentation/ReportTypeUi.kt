package com.exerovv.deadpixel.feature.reports.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.exerovv.deadpixel.feature.reports.R
import com.exerovv.deadpixel.feature.reports.domain.model.ReportType

@Composable
fun ReportType.label(): String = when (this) {
    ReportType.COMPLETED_ORDERS -> stringResource(R.string.report_type_completed_orders)
    ReportType.REVENUE -> stringResource(R.string.report_type_revenue)
    ReportType.MASTER_PERFORMANCE -> stringResource(R.string.report_type_master_performance)
    ReportType.DEADLINE_VIOLATIONS -> stringResource(R.string.report_type_deadline_violations)
}

@Composable
fun ReportType.chipLabel(): String = when (this) {
    ReportType.COMPLETED_ORDERS -> stringResource(R.string.report_type_chip_completed_orders)
    ReportType.REVENUE -> stringResource(R.string.report_type_chip_revenue)
    ReportType.MASTER_PERFORMANCE -> stringResource(R.string.report_type_chip_master_performance)
    ReportType.DEADLINE_VIOLATIONS -> stringResource(R.string.report_type_chip_deadline_violations)
}

fun reportTypeColor(type: ReportType, primary: Color, secondary: Color): Color = when (type) {
    ReportType.COMPLETED_ORDERS -> Color(0xFF4CAF50)
    ReportType.REVENUE -> primary
    ReportType.MASTER_PERFORMANCE -> secondary
    ReportType.DEADLINE_VIOLATIONS -> Color(0xFFCF6679)
}
