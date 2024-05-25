package com.logixmates.snuffle.core.domain.firebase

import android.content.SharedPreferences
import androidx.core.content.edit
import com.logixmates.snuffle.core.domain.UseCase
import org.koin.core.annotation.Factory

@Factory
class SaveFcmTokenUseCase(
    private val sharedPreferences: SharedPreferences
) : UseCase<String, Unit>() {
    override suspend operator fun invoke(request: String) {
        sharedPreferences.edit { putString(FCM_TOKEN_KEY, request) }
    }

    companion object {
        const val FCM_TOKEN_KEY = "fcm_token"
    }
}
