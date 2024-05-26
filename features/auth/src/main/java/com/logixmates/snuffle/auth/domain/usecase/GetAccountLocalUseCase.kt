package com.logixmates.snuffle.auth.domain.usecase

import android.content.SharedPreferences
import com.logixmates.snuffle.auth.domain.model.LoginDomain
import com.logixmates.snuffle.core.domain.UseCase
import kotlinx.serialization.json.Json

class GetAccountLocalUseCase(
    private val sharedPreferences: SharedPreferences,
    private val json: Json
) : UseCase<Unit, LoginDomain>() {
    override suspend operator fun invoke(request: Unit): LoginDomain {
        val jsonRaw =
            sharedPreferences.getString(SaveAccountLocalUseCase.ACCOUNT_LOCAL_KEY, "") ?: ""
        require(jsonRaw.isNotBlank())
        return json.decodeFromString(jsonRaw)
    }
}
