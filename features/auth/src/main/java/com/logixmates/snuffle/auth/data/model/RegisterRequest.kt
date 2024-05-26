package com.logixmates.snuffle.auth.data.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class RegisterRequest(
    @SerialName("user_name") val userName: String? = null,
    @SerialName("password") val password: String? = null,
    @SerialName("date_of_birth") val dob: String? = null,
    @SerialName("user_gender") val gender: String? = null,
    @SerialName("register_with") val registerWith: String? = null,
    @SerialName("notify_token") val notifyToken: String? = null,
    @SerialName("timezone") val timezone: String? = null,
    @SerialName("device_info") val deviceInfo: String? = null,
    @SerialName("version") val version: String? = null,
)
