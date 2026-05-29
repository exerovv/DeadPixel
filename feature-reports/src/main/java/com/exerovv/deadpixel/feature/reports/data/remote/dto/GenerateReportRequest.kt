package com.exerovv.deadpixel.feature.reports.data.remote.dto

import com.exerovv.deadpixel.feature.reports.domain.model.ReportType
import kotlinx.serialization.Serializable

@Serializable
data class GenerateReportRequest(
    val type: ReportType,
    val generatedBy: Int,
    val periodStart: String,
    val periodEnd: String
)
