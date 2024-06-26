package com.logixmates.snuffle.auth.data.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class LoginRequest(
    @SerialName("user_name") val userName: String? = null,
    @SerialName("password") val password: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("email") val email: String? = null,
    @SerialName("provider") val provider: String? = null,
    @SerialName("provider_id") val providerId: String? = null,
    @SerialName("notify_token") val notifyToken: String? = null,
    @SerialName("timezone") val timezone: String? = null,
    @SerialName("device_info") val deviceInfo: String? = null,
    @SerialName("version") val version: String? = null,
)
