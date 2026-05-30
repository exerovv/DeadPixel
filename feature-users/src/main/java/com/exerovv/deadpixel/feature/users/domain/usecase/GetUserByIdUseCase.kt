package com.exerovv.deadpixel.feature.users.domain.usecase

import com.exerovv.deadpixel.feature.users.domain.model.User
import com.exerovv.deadpixel.feature.users.domain.repository.UsersRepository
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val repository: UsersRepository
) {
    suspend operator fun invoke(userId: Int): User = repository.getUserById(userId)
}
