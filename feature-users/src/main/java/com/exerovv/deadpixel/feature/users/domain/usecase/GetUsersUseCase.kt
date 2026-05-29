package com.exerovv.deadpixel.feature.users.domain.usecase

import com.exerovv.deadpixel.feature.users.domain.model.User
import com.exerovv.deadpixel.feature.users.domain.repository.UsersRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(private val repo: UsersRepository) {
    suspend operator fun invoke(): List<User> = repo.getUsers()
}
