package com.exerovv.deadpixel.feature.diagnostics.domain.usecase

import com.exerovv.deadpixel.feature.diagnostics.domain.repository.DiagnosticsRepository
import javax.inject.Inject

class GetDiagnosticsByOrderUseCase @Inject constructor(
    private val repository: DiagnosticsRepository
) {
    suspend operator fun invoke(orderId: Int) = repository.getByOrder(orderId)
}
