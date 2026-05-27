package com.exerovv.deadpixel.feature.auth.domain.usecase

import com.exerovv.deadpixel.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class IsLoggedInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Boolean = authRepository.isLoggedIn()
}
