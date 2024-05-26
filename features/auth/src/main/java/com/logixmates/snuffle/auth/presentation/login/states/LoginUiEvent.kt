package com.logixmates.snuffle.auth.presentation.login.states

import androidx.annotation.Keep
import com.logixmates.snuffle.auth.domain.model.LoginDomain

sealed interface LoginUiEvent {
    sealed interface Presentation : LoginUiEvent {
        @Keep
        data class OnLoginFailed(val message: String?) : Presentation

        @Keep
        data class OnLoginSuccess(val data: LoginDomain) : Presentation
        data object DoGoogleLogin : Presentation
        data object NavigateToSignUp : Presentation
        data object NavigateToForgotPassword : Presentation
        data object OnSnufflePrivacyClick : Presentation
        data object OnCookiePolicyClick : Presentation
        data object OnTermAndConditionClick : Presentation
    }

    sealed interface Domain : LoginUiEvent {
        data object DoLogin : Domain

        @Keep
        data class OnGoogleLoginSuccess(
            val name: String?,
            val providerId: String?,
            val email: String?
        ) : Domain

        @Keep
        data class OnEmailChanged(val email: String) : Domain

        @Keep
        data class OnPasswordChanged(val password: String) : Domain

        @Keep
        data class OnPasswordVisibilityChanged(val isPasswordVisible: Boolean) : Domain

        @Keep
        data class OnPrivacyConsentChanged(val isChecked: Boolean) : Domain
    }
}
