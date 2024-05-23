package com.logixmates.snuffle.auth.presentation.login.states

import androidx.annotation.Keep

sealed interface LoginUiEvent {
    sealed interface Presentation : LoginUiEvent {
        data object DoGoogleLogin : Presentation
        data object NavigateToSignUp : Presentation
        data object NavigateToForgotPassword: Presentation
        data object OnSnufflePrivacyClick : Presentation
        data object OnCookiePolicyClick : Presentation
        data object OnTermAndConditionClick : Presentation
    }

    sealed interface Domain : LoginUiEvent {
        data object DoLogin : Domain

        data class OnGoogleLoginSuccess(val email: String): Domain

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
