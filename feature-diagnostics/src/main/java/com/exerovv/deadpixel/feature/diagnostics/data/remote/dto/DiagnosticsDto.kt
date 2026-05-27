package com.exerovv.deadpixel.feature.diagnostics.data.remote.dto

import com.exerovv.deadpixel.feature.diagnostics.domain.model.DiagnosticStatus
import kotlinx.serialization.Serializable

@Serializable
data class DiagnosticsDto(
    val id: Int,
    val orderId: Int,
    val performedBy: Int,
    val status: DiagnosticStatus,
    val result: String? = null,
    val detectedIssues: String? = null,
    val startedAt: String,
    val completedAt: String? = null
)
