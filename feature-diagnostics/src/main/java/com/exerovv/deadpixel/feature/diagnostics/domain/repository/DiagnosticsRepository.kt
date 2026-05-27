package com.exerovv.deadpixel.feature.diagnostics.domain.repository

import com.exerovv.deadpixel.feature.diagnostics.domain.model.Diagnostics

interface DiagnosticsRepository {
    suspend fun getByOrder(orderId: Int): Diagnostics?
    suspend fun simulate(diagnosticsId: Int): Diagnostics
    suspend fun complete(diagnosticsId: Int, result: String, detectedIssues: String?)
    suspend fun fail(diagnosticsId: Int)
}
