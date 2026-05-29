package com.exerovv.deadpixel.feature.users.data.remote

import com.exerovv.deadpixel.feature.users.data.remote.dto.UserDto
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UsersApi {
    @GET("api/users")
    suspend fun getUsers(): List<UserDto>

    @PUT("api/users/{id}/active")
    suspend fun setUserActive(@Path("id") userId: Int, @Query("value") value: Boolean)
}
