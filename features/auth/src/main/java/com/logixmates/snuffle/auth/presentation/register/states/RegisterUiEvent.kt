package com.logixmates.snuffle.auth.presentation.register.states

import androidx.annotation.Keep

sealed interface RegisterUiEvent {
    sealed interface Domain : RegisterUiEvent {
        data object DoSignUp : Domain

        data class OnGoogleSignUpSuccess(val email: String) : Domain

        @Keep
        data class OnEmailChanged(val email: String) : Domain

        @Keep
        data class OnPasswordChanged(val password: String) : Domain

        @Keep
        data class OnPasswordVisibilityChanged(val isPasswordVisible: Boolean) : Domain

        @Keep
        data class OnConfirmPasswordChanged(val password: String) : Domain

        @Keep
        data class OnConfirmPasswordVisibilityChanged(val isPasswordVisible: Boolean) : Domain

        @Keep
        data class OnPrivacyConsentChanged(val isChecked: Boolean) : Domain
    }

    sealed interface Presentation : RegisterUiEvent {
        data object DoGoogleSignUp : Presentation
        data object OnSnufflePrivacyClick : Presentation
        data object OnCookiePolicyClick : Presentation
        data object OnTermAndConditionClick : Presentation
    }
}
