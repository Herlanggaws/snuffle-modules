package com.logixmates.snuffle.auth.domain.usecase

import com.logixmates.snuffle.auth.data.model.LoginRequest
import com.logixmates.snuffle.auth.data.repo.AuthRepo
import com.logixmates.snuffle.auth.domain.model.LoginDomain
import com.logixmates.snuffle.core.domain.UseCase
import org.koin.core.annotation.Factory

@Factory
class LoginUseCase(
    private val authRepo: AuthRepo
) : UseCase<LoginRequest, LoginDomain>() {

    override suspend operator fun invoke(request: LoginRequest): LoginDomain {
        return LoginDomain(authRepo.doLogin(request))
    }
}
