package com.logixmates.snuffle.core.domain.authtoken

import android.content.SharedPreferences
import com.logixmates.snuffle.core.domain.UseCase
import org.koin.core.annotation.Factory

@Factory
class GetAuthTokenUseCase(
    private val sharedPreferences: SharedPreferences
) : UseCase<Unit, String>() {
    override suspend operator fun invoke(request: Unit): String {
        return sharedPreferences.getString(SaveAuthTokenUseCase.AUTH_TOKEN_KEY, "") ?: ""
    }
}
