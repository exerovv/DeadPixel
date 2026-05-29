package com.exerovv.deadpixel.feature.users.domain.usecase

import com.exerovv.deadpixel.feature.users.domain.repository.UsersRepository
import javax.inject.Inject

class SetUserActiveUseCase @Inject constructor(private val repo: UsersRepository) {
    suspend operator fun invoke(userId: Int, value: Boolean) = repo.setUserActive(userId, value)
}
