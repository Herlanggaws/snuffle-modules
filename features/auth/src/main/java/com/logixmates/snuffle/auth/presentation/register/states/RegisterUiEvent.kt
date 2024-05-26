package com.logixmates.snuffle.auth.presentation.register.states

import androidx.annotation.Keep
import com.logixmates.snuffle.auth.domain.model.LoginDomain
import com.logixmates.snuffle.auth.presentation.login.states.LoginUiEvent.Domain
import com.logixmates.snuffle.auth.presentation.login.states.LoginUiEvent.Presentation

sealed interface RegisterUiEvent {
    sealed interface Domain : RegisterUiEvent {
        data object DoSignUp : Domain

        data class OnGoogleSignUpSuccess(
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

        @Keep
        data class OnSignUpFailed(val message: String?) : Presentation

        @Keep
        data class OnSignUpSuccess(val data: LoginDomain) : Presentation
    }
}
