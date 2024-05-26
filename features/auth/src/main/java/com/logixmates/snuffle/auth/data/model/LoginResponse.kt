package com.logixmates.snuffle.auth.data.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class LoginResponse(
    @SerialName("data")
    val data: Data? = null,
    @SerialName("message")
    val message: String? = null,
    @SerialName("status_code")
    val statusCode: Int? = null,
    @SerialName("token")
    val token: String? = null
) {
    @Keep
    @Serializable
    data class Data(
        @SerialName("avatar")
        val avatar: String? = null,
        @SerialName("avatar_url")
        val avatarUrl: String? = null,
        @SerialName("chat_id")
        val chatId: String? = null,
        @SerialName("created_at")
        val createdAt: String? = null,
        @SerialName("date_of_birth")
        val dateOfBirth: String? = null,
        @SerialName("deleted_at")
        val deletedAt: String? = null,
        @SerialName("description")
        val description: String? = null,
        @SerialName("device_token")
        val deviceToken: String? = null,
        @SerialName("dog")
        val dog: ProfileData? = null,
        @SerialName("email")
        val email: String? = null,
        @SerialName("email_verified_at")
        val emailVerifiedAt: String? = null,
        @SerialName("fav_pets")
        val favPets: String? = null,
        @SerialName("id")
        val id: Int? = null,
        @SerialName("insta_id")
        val instaId: String? = null,
        @SerialName("is_dog")
        val isDog: Boolean? = null,
        @SerialName("last_login")
        val lastLogin: String? = null,
        @SerialName("lat")
        val lat: String? = null,
        @SerialName("lng")
        val lng: String? = null,
        @SerialName("looking_for")
        val lookingFor: String? = null,
        @SerialName("name")
        val name: String? = null,
        @SerialName("notify_token")
        val notifyToken: String? = null,
        @SerialName("phone_number")
        val phoneNumber: String? = null,
        @SerialName("picture")
        val picture: String? = null,
        @SerialName("provider")
        val provider: String? = null,
        @SerialName("provider_id")
        val providerId: String? = null,
        @SerialName("provider_role")
        val providerRole: String? = null,
        @SerialName("reset_password_token")
        val resetPasswordToken: String? = null,
        @SerialName("role")
        val role: String? = null,
        @SerialName("status")
        val status: String? = null,
        @SerialName("timezone")
        val timezone: String? = null,
        @SerialName("updated_at")
        val updatedAt: String? = null,
        @SerialName("user_gender")
        val userGender: String? = null,
        @SerialName("user_name")
        val userName: String? = null,
        @SerialName("version")
        val version: String? = null
    )
}