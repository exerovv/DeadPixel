package com.exerovv.deadpixel.feature.reports.data.remote.dto

import com.exerovv.deadpixel.feature.reports.domain.model.ReportType
import kotlinx.serialization.Serializable

@Serializable
data class ReportDto(
    val id: Int,
    val generatedBy: Int,
    val type: ReportType,
    val periodStart: String,
    val periodEnd: String,
    val data: String,
    val generatedAt: String
)
