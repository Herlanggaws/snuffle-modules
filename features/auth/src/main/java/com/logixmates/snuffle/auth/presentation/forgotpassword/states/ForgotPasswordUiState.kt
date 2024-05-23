package com.logixmates.snuffle.auth.presentation.forgotpassword.states

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
@Keep
data class ForgotPasswordUiState(
    val email: String = "",
    val emailError: String? = "",
    val isLoading: Boolean = false
) : Parcelable
