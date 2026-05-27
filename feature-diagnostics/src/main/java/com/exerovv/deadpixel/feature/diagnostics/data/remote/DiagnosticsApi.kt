package com.exerovv.deadpixel.feature.diagnostics.data.remote

import com.exerovv.deadpixel.feature.diagnostics.data.remote.dto.CompleteDiagnosticsRequest
import com.exerovv.deadpixel.feature.diagnostics.data.remote.dto.DiagnosticsDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DiagnosticsApi {

    @GET("api/diagnostics/order/{orderId}")
    suspend fun getByOrder(@Path("orderId") orderId: Int): DiagnosticsDto

    @GET("api/diagnostics/{id}")
    suspend fun getById(@Path("id") id: Int): DiagnosticsDto

    @POST("api/diagnostics/{id}/simulate")
    suspend fun simulate(@Path("id") id: Int): DiagnosticsDto

    @POST("api/diagnostics/{id}/complete")
    suspend fun complete(@Path("id") id: Int, @Body request: CompleteDiagnosticsRequest)

    @POST("api/diagnostics/{id}/fail")
    suspend fun fail(@Path("id") id: Int)
}
