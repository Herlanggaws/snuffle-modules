package com.logixmates.snuffle.auth.data.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class ProviderRole(
    @SerialName("id") val id: Int,
    @SerialName("first_name") val firstName: String?,
    @SerialName("last_name") val lastName: String?,
    @SerialName("dob") val dob: String,
    @SerialName("gender") val gender: String,
    @SerialName("contact_number") val contactNumber: String,
    @SerialName("email") val email: String,
    @SerialName("iban_number") val ibanNumber: String,
    @SerialName("profile_picture") val profilePicture: String?,
    @SerialName("passport_photo") val passportPhoto: String,
    @SerialName("public_description") val publicDescription: String,
    @SerialName("pictures") val pictures: List<String>?,
    @SerialName("user_id") val userId: Int,
    @SerialName("status") val status: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("average_rating") val averageRating: String?,
    @SerialName("completed_jobs") val completedJobs: Int?,
    @SerialName("total_earning") val totalEarning: String?,
    @SerialName("stripe_conecnt_account") val stripeConecntAccount: String,
    @SerialName("latest_reviews") val latestReviews: List<LatestReviews>,
    @SerialName("verification_status") val verificationStatus: String?
) {
    fun isVerified(): Boolean {
        return when (verificationStatus) {
            VerificationStatus.VERIFIED_STATUS.value -> true
            else -> false
        }
    }
}

@Serializable
@Keep
data class LatestReviews(
    @SerialName("id") val id: Int,
    @SerialName("rating") val rating: Int,
    @SerialName("feedback") val feedback: String?,
    @SerialName("logs") val logs: Logs,
    @SerialName("job_user_id") val jobUserId: Int,
    @SerialName("laravel_through_key") val laravelThroughLaravelThroughKey: Int,
    @SerialName("user") val user: ReviewUser
)


@Serializable
@Keep
data class ReviewUser(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String?,
    @SerialName("user_name") val userName: String?,
    @SerialName("email") val email: String?,
    @SerialName("picture") val picture: ArrayList<String>?,
    @SerialName("lat") val lat: Double = 0.0,
    @SerialName("lng") val lng: Double = 0.0
)

@Serializable
@Keep
data class Logs(
    @SerialName("interested_by_provider_at") val interestedByProviderAt: String,
    @SerialName("cancel_by_user_at") val cancelByUserAt: String,
    @SerialName("rejected_by_user_at") val rejectedByUserAt: String,
    @SerialName("on_my_way_at") val onMyWayAt: String?,
    @SerialName("confirm_by_user_at") val confirmByUserAt: String,
    @SerialName("arrived_by_provider_at") val arrivedByProviderAt: String,
    @SerialName("pet_handover_by_user_at") val petHandoverByUserAt: String,
    @SerialName("completed_by_provider_at") val completedByProviderAt: String,
    @SerialName("pet_handover_back_by_user_at") val petHandoverBackByUserAt: String,
    @SerialName("review_and_rate_by_user_at") val reviewAndRateByUserAt: String
)
