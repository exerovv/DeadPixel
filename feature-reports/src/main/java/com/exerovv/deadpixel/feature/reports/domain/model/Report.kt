package com.exerovv.deadpixel.feature.reports.domain.model

data class Report(
    val id: Int,
    val generatedBy: Int,
    val type: ReportType,
    val periodStart: String,
    val periodEnd: String,
    val data: String,
    val generatedAt: String
)
