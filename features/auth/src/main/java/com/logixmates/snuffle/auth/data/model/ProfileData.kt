package com.logixmates.snuffle.auth.data.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class ProfileData(
    @SerialName("id") val id: Int?,
    @SerialName("user_id") val userId: Int?,
    @SerialName("dog_name") val dogName: String?,
    @SerialName("is_fav") val isFav: Boolean?,
    @SerialName("dog_gender") val dogGnder: String?,
    @SerialName("dog_age") val dogAge: String?,
    @SerialName("dog_type") val dogType: String? = null,
    @SerialName("fav_pets") var favPets: String? = null,
    @SerialName("created_at") val createdAt: String?,
    @SerialName("updated_at") val updatedAt: String?
)
