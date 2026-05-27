package com.exerovv.deadpixel.feature.auth.domain.usecase

import com.exerovv.deadpixel.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String, role: String) =
        authRepository.register(name, email, password, role)
}
