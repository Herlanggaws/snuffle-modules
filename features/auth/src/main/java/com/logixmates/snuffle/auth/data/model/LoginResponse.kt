package com.logixmates.snuffle.auth.data.model

import androidx.annotation.Keep
import com.logixmates.snuffle.core.data.model.Response
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class LoginResponse(
    @SerialName("message") val message: String?,
    @SerialName("status_code") val statusCode: Int?,
    @SerialName("success") val success: String?,
    @SerialName("token") val token: String?,
    @SerialName("user") var user: UserData?,
    @SerialName("data") var profileMain: UserData?
)
