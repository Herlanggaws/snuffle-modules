package com.logixmates.snuffle.core.domain.authtoken

import android.content.SharedPreferences
import androidx.core.content.edit
import com.logixmates.snuffle.core.domain.UseCase
import org.koin.core.annotation.Factory

@Factory
class SaveAuthTokenUseCase(
    private val sharedPreferences: SharedPreferences
) : UseCase<String, Unit>() {
    override suspend operator fun invoke(request: String) {
        sharedPreferences.edit { putString(AUTH_TOKEN_KEY, request) }
    }

    companion object {
        const val AUTH_TOKEN_KEY = "snuffle_auth_token"
    }
}
