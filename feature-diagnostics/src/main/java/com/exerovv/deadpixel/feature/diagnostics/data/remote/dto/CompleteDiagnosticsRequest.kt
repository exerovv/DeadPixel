package com.exerovv.deadpixel.feature.diagnostics.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CompleteDiagnosticsRequest(
    val result: String,
    val detectedIssues: String? = null
)
