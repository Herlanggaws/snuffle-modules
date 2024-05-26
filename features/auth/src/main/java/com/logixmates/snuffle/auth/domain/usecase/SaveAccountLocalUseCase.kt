package com.logixmates.snuffle.auth.domain.usecase

import android.content.SharedPreferences
import androidx.core.content.edit
import com.logixmates.snuffle.auth.domain.model.LoginDomain
import com.logixmates.snuffle.core.domain.UseCase
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Factory

@Factory
class SaveAccountLocalUseCase(
    private val sharedPreferences: SharedPreferences,
    private val json: Json
) : UseCase<LoginDomain, Unit>() {
    override suspend operator fun invoke(request: LoginDomain) {
        val json = json.encodeToString(request)
        sharedPreferences.edit { putString(ACCOUNT_LOCAL_KEY, json) }
    }

    companion object {
        const val ACCOUNT_LOCAL_KEY = "snuffle_account_data"
    }
}
