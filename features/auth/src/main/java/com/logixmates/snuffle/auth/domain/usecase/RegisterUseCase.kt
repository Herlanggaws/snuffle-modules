package com.logixmates.snuffle.auth.domain.usecase

import com.logixmates.snuffle.auth.data.model.RegisterRequest
import com.logixmates.snuffle.auth.data.repo.AuthRepo
import com.logixmates.snuffle.auth.domain.model.LoginDomain
import com.logixmates.snuffle.core.BuildConfig
import com.logixmates.snuffle.core.domain.UseCase
import com.logixmates.snuffle.core.domain.authtoken.SaveAuthTokenUseCase
import com.logixmates.snuffle.core.domain.firebase.GetFcmTokenUseCase
import org.koin.core.annotation.Factory
import java.util.TimeZone

@Factory
class RegisterUseCase(
    private val authRepo: AuthRepo,
    private val getFcmTokenUseCase: GetFcmTokenUseCase,
    private val saveAuthTokenUseCase: SaveAuthTokenUseCase,
    private val saveAccountLocalUseCase: SaveAccountLocalUseCase
) : UseCase<RegisterRequest, LoginDomain>() {

    override suspend operator fun invoke(request: RegisterRequest): LoginDomain {
        return LoginDomain(
            authRepo.register(
                request.copy(
                    notifyToken = getFcmTokenUseCase(false),
                    dob = "",
                    gender = "",
                    registerWith = "email",
                    timezone = TimeZone.getDefault().id,
                    deviceInfo = android.os.Build.MODEL,
                    version = BuildConfig.VERSION_NAME
                )
            ).also {
                saveAuthTokenUseCase(it.token ?: "")
            }
        ).also {
            saveAccountLocalUseCase(it)
        }
    }
}
