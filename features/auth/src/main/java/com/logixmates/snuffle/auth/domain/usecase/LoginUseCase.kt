package com.logixmates.snuffle.auth.domain.usecase

import com.logixmates.snuffle.auth.data.model.LoginRequest
import com.logixmates.snuffle.auth.data.repo.AuthRepo
import com.logixmates.snuffle.auth.domain.model.LoginDomain
import com.logixmates.snuffle.core.BuildConfig
import com.logixmates.snuffle.core.domain.UseCase
import com.logixmates.snuffle.core.domain.firebase.GetFcmTokenUseCase
import org.koin.core.annotation.Factory
import java.util.TimeZone

@Factory
class LoginUseCase(
    private val authRepo: AuthRepo,
    private val getFcmTokenUseCase: GetFcmTokenUseCase
) : UseCase<LoginRequest, LoginDomain>() {

    override suspend operator fun invoke(request: LoginRequest): LoginDomain {
        return LoginDomain(
            authRepo.doLogin(
                request.copy(
                    notifyToken = getFcmTokenUseCase(false),
                    timezone = TimeZone.getDefault().id,
                    deviceInfo = android.os.Build.MODEL,
                    version = BuildConfig.VERSION_NAME
                )
            )
        )
    }
}
