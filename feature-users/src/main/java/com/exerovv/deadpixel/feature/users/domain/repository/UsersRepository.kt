package com.exerovv.deadpixel.feature.users.domain.repository

import com.exerovv.deadpixel.feature.users.domain.model.User

interface UsersRepository {
    suspend fun getUsers(): List<User>
    suspend fun setUserActive(userId: Int, value: Boolean)
}
