package com.exerovv.deadpixel.feature.users.data.repository

import com.exerovv.deadpixel.feature.users.data.remote.UsersApi
import com.exerovv.deadpixel.feature.users.data.remote.toDomain
import com.exerovv.deadpixel.feature.users.domain.model.User
import com.exerovv.deadpixel.feature.users.domain.repository.UsersRepository
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val api: UsersApi
) : UsersRepository {

    override suspend fun getUsers(): List<User> = api.getUsers().map { it.toDomain() }

    override suspend fun setUserActive(userId: Int, value: Boolean) {
        api.setUserActive(userId, value)
    }
}
