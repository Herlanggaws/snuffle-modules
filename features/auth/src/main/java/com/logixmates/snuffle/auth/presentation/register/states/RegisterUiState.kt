package com.logixmates.snuffle.auth.presentation.register.states

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class RegisterUiState(
    val isLoading: Boolean = false,
    val email: String = "",
    val emailError: String? = "",
    val password: String = "",
    val passwordError: String? = "",
    val isPasswordVisible: Boolean = false,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = "",
    val isConfirmPasswordVisible: Boolean = false,
    val isPrivacyConsentChecked: Boolean = false
)
