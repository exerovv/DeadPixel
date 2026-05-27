package com.exerovv.deadpixel.feature.diagnostics.data.repository

import com.exerovv.deadpixel.feature.diagnostics.data.mapper.toDomain
import com.exerovv.deadpixel.feature.diagnostics.data.remote.DiagnosticsApi
import com.exerovv.deadpixel.feature.diagnostics.data.remote.dto.CompleteDiagnosticsRequest
import com.exerovv.deadpixel.feature.diagnostics.domain.model.Diagnostics
import com.exerovv.deadpixel.feature.diagnostics.domain.repository.DiagnosticsRepository
import retrofit2.HttpException
import javax.inject.Inject

class DiagnosticsRepositoryImpl @Inject constructor(
    private val api: DiagnosticsApi
) : DiagnosticsRepository {

    override suspend fun getByOrder(orderId: Int): Diagnostics? = try {
        api.getByOrder(orderId).toDomain()
    } catch (e: HttpException) {
        if (e.code() == 404) null else throw e
    }

    override suspend fun simulate(diagnosticsId: Int): Diagnostics =
        api.simulate(diagnosticsId).toDomain()

    override suspend fun complete(diagnosticsId: Int, result: String, detectedIssues: String?) {
        api.complete(diagnosticsId, CompleteDiagnosticsRequest(result, detectedIssues))
    }

    override suspend fun fail(diagnosticsId: Int) {
        api.fail(diagnosticsId)
    }
}
