package com.exerovv.deadpixel.feature.diagnostics.domain.usecase

import com.exerovv.deadpixel.feature.diagnostics.domain.repository.DiagnosticsRepository
import javax.inject.Inject

class FailDiagnosticsUseCase @Inject constructor(
    private val repository: DiagnosticsRepository
) {
    suspend operator fun invoke(diagnosticsId: Int) = repository.fail(diagnosticsId)
}
