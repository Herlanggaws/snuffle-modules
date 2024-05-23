package com.logixmates.snuffle.auth.data.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class LoginRequest(
    @SerialName("user_name") val userName: String,
    @SerialName("notify_token") val notifyToken: String,
    @SerialName("timezone") val timezone: String,
    @SerialName("device_info") val deviceInfo: String,
    @SerialName("version") val version: String,
)
