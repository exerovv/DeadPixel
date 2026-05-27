package com.exerovv.deadpixel.feature.diagnostics.presentation

sealed interface DiagnosticsCommand {
    data object Retry : DiagnosticsCommand
    data object Simulate : DiagnosticsCommand
    data class Complete(val result: String, val detectedIssues: String?) : DiagnosticsCommand
    data object Fail : DiagnosticsCommand
}
