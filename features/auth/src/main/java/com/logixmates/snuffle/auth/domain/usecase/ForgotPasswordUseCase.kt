package com.logixmates.snuffle.auth.domain.usecase

import com.logixmates.snuffle.auth.data.repo.AuthRepo
import com.logixmates.snuffle.core.data.model.Response
import com.logixmates.snuffle.core.domain.UseCase
import org.koin.core.annotation.Factory

@Factory
class ForgotPasswordUseCase(
    private val authRepo: AuthRepo
) : UseCase<String, Response>() {

    override suspend operator fun invoke(request: String): Response {
        return authRepo.requestForgotPassword(request)
    }
}
