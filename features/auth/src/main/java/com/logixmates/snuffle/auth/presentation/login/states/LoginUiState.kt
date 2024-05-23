package com.logixmates.snuffle.auth.presentation.login.states

import android.os.Parcelable
import androidx.annotation.Keep
import com.logixmates.snuffle.auth.domain.model.LoginDomain
import com.logixmates.snuffle.core.presentation.checkEmail
import com.logixmates.snuffle.core.presentation.checkPassword
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
@Keep
data class LoginUiState(
    val isLoading: Boolean = false,
    val loginErrorMessage: String? = null,
    val loginData: LoginDomain? = null,
    val email: String = "",
    val emailError: String? = "",
    val password: String = "",
    val passwordError: String? = "",
    val isPasswordVisible: Boolean = false,
    val isPrivacyConsentChecked: Boolean = false
) : Parcelable
