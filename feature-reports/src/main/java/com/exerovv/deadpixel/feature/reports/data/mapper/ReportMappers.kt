package com.exerovv.deadpixel.feature.reports.data.mapper

import com.exerovv.deadpixel.feature.reports.data.remote.dto.ReportDto
import com.exerovv.deadpixel.feature.reports.domain.model.Report

fun ReportDto.toDomain() = Report(
    id = id,
    generatedBy = generatedBy,
    type = type,
    periodStart = periodStart,
    periodEnd = periodEnd,
    data = data,
    generatedAt = generatedAt
)
