package com.logixmates.snuffle.core.domain.firebase

import android.content.SharedPreferences
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.logixmates.snuffle.core.domain.UseCase
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Factory

@Factory
class GetFcmTokenUseCase(
    private val sharedPreferences: SharedPreferences,
    private val saveFcmTokenUseCase: SaveFcmTokenUseCase
) : UseCase<Boolean, String>() {

    override suspend operator fun invoke(isLocal: Boolean): String {
        return if (isLocal) {
            sharedPreferences.getString(SaveFcmTokenUseCase.FCM_TOKEN_KEY, "") ?: ""
        } else {
            Firebase.messaging.token.await().also {
                saveFcmTokenUseCase(it)
            }
        }
    }
}
