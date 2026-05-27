package com.exerovv.deadpixel.feature.diagnostics.domain.usecase

import com.exerovv.deadpixel.feature.diagnostics.domain.repository.DiagnosticsRepository
import javax.inject.Inject

class SimulateDiagnosticsUseCase @Inject constructor(
    private val repository: DiagnosticsRepository
) {
    suspend operator fun invoke(diagnosticsId: Int) = repository.simulate(diagnosticsId)
}
