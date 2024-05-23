package com.logixmates.snuffle.auth.presentation.forgotpassword.states

import androidx.annotation.Keep
import java.lang.Exception

sealed interface ForgotPasswordUiEvent {
    sealed interface Presentation : ForgotPasswordUiEvent {
        data object NavigateUp : Presentation
        data object ShowSuccess : Presentation

        @Keep
        data class ShowError(val msg: String?) : Presentation
    }

    sealed interface Domain : ForgotPasswordUiEvent {
        @Keep
        data class OnEmailChanged(val email: String) : Domain
        data object SubmitRequest : Domain
    }
}
