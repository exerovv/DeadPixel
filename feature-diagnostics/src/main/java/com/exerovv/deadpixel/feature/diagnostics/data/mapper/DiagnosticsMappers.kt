package com.exerovv.deadpixel.feature.diagnostics.data.mapper

import com.exerovv.deadpixel.feature.diagnostics.data.remote.dto.DiagnosticsDto
import com.exerovv.deadpixel.feature.diagnostics.domain.model.Diagnostics

fun DiagnosticsDto.toDomain() = Diagnostics(
    id = id,
    orderId = orderId,
    performedBy = performedBy,
    status = status,
    result = result,
    detectedIssues = detectedIssues,
    startedAt = startedAt,
    completedAt = completedAt
)
