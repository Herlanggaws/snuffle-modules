package com.logixmates.snuffle.auth.presentation.login.states

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
@Keep
data class LoginUiState(
    val isLoading: Boolean = false,
    val email: String = "",
    val emailError: String? = "",
    val password: String = "",
    val passwordError: String? = "",
    val isPasswordVisible: Boolean = false,
    val isPrivacyConsentChecked: Boolean = false
) : Parcelable
