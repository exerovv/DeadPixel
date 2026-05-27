package com.exerovv.deadpixel.feature.diagnostics.domain.usecase

import com.exerovv.deadpixel.feature.diagnostics.domain.repository.DiagnosticsRepository
import javax.inject.Inject

class CompleteDiagnosticsUseCase @Inject constructor(
    private val repository: DiagnosticsRepository
) {
    suspend operator fun invoke(diagnosticsId: Int, result: String, detectedIssues: String?) =
        repository.complete(diagnosticsId, result, detectedIssues)
}
