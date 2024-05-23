package com.logixmates.snuffle.auth.data.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class UserData(
    @SerialName("id") var id: Int?,
    @SerialName("name") val name: String?,
    @SerialName("email") val email: String?,
    @SerialName("phone_number") val phoneNumber: String?,
    @SerialName("user_gender") val userGender: String?,
    @SerialName("date_of_birth") val dateOfBirth: String?,
    @SerialName("looking_for") val lookingFor: String?,
    @SerialName("avatar") val avatar: String?,
    @SerialName("avatar_url") val avatarUrl: String?,
    @SerialName("role") val role: String?,
//	@SerialName("provider_id") val provider_id : Double,
//	@SerialName("mood_request") var mood_request : MoodRequests?,
    @SerialName("fav_pets") val favPets: String? = null,
//	@SerialName("provider") val provider : String,
    @SerialName("is_fav") var isFav: Boolean?,
    @SerialName("status") val status: Int?,
//	@SerialName("last_login") val last_login : String?,
    @SerialName("lat") val lat: Double? = 0.0,
    @SerialName("lng") val lng: Double? = 0.0,
//	@SerialName("email_verified_at") val email_verified_at : String?,
    @SerialName("created_at") val createdAt: String?,
    @SerialName("updated_at") val updatedAt: String?,
//	@SerialName("distance") var distance : Long?,
    @SerialName("online_before") val onlineBefore: String?,
    @SerialName("request_before") val requestBefore: String?,
    @SerialName("dog") val dog: ProfileData?,
    @SerialName("provider_role") var providerRole: ProviderRole?,
    @SerialName("is_dog") val isDog: Boolean?,
    @SerialName("mood_request_by_me") var moodRequestByMe: MoodRequests?,
    @SerialName("mood_request_to_me") var moodRequestToMe: MoodRequests?,
    @SerialName("insta_id") var instaId: String?,
    @SerialName("description") val description: String? = null,
    @SerialName("picture") val picture: ArrayList<String>?,
    @SerialName("unread_messages_count") var unreadMessagesCount: Int?,
    @SerialName("last_message") val lastMessage: LastMessage?,
    @SerialName("loast_and_found") val loastAndFound: LostAndFoundPet?,
    @SerialName("followers_count") val followersCount: Int?,
    @SerialName("followed_count") val followedCount: Int?,
    @SerialName("liked_videos_count") var likedVideosCount: Int?,
    var isBanner: Boolean = false
)

@Serializable
@Keep
data class MoodRequests(
    @SerialName("id") val id: Int?,
    @SerialName("request_by_id") val requestById: Int?,
    @SerialName("request_to_id") val requestToId: Int?,
    @SerialName("mood_id") val moodId: Int?,
    @SerialName("created_at") val createdAt: String?,
    @SerialName("updated_at") val updatedAt: String?
)

@Serializable
@Keep
data class LastMessage(
    @SerialName("id") val id: Int?,
    @SerialName("sender_id") val senderId: Int?,
    @SerialName("reciever_id") val recieverId: Int?,
    @SerialName("seen_id") val seenId: String?,
    @SerialName("is_read") val isRead: Int?,
    @SerialName("message") var message: String?,
    @SerialName("status") val status: Int?,
    @SerialName("msg_type") var msgType: String?,
//	@SerialName("chatable_id") val chatable_id : Int?,
    @SerialName("chatable_type") val chatableType: String?,
    @SerialName("read_at") val readAt: String?,
    @SerialName("deleted_at") val deletedAt: String?,
    @SerialName("created_at") var createdAt: String?,
    @SerialName("updated_at") val updatedAt: String?
)

@Serializable
@Keep
data class LostAndFoundPet(
    @SerialName("id") var id: Int?,
    @SerialName("user_id") val userId: Int?,
    @SerialName("image") val image: String?,
    @SerialName("description") val description: String?,
    @SerialName("address") val address: String?,
    @SerialName("status") val status: Int?,
    @SerialName("created_at") val createdAt: String?,
    @SerialName("updated_at") val updatedAt: String?
)
