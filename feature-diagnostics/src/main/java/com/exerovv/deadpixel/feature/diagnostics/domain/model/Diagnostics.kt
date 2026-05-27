package com.exerovv.deadpixel.feature.diagnostics.domain.model

data class Diagnostics(
    val id: Int,
    val orderId: Int,
    val performedBy: Int,
    val status: DiagnosticStatus,
    val result: String?,
    val detectedIssues: String?,
    val startedAt: String,
    val completedAt: String?
)
