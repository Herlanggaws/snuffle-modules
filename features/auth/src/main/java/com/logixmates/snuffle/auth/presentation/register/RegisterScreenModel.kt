package com.logixmates.snuffle.auth.presentation.register

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.logixmates.snuffle.auth.presentation.register.states.RegisterUiEvent
import com.logixmates.snuffle.auth.presentation.register.states.RegisterUiEvent.Domain
import com.logixmates.snuffle.auth.presentation.register.states.RegisterUiState
import com.logixmates.snuffle.core.presentation.utils.checkEmail
import com.logixmates.snuffle.core.presentation.utils.checkPassword
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
class RegisterScreenModel : ScreenModel {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState get() = _uiState.asStateFlow()

    private val _uiEvent = Channel<RegisterUiEvent>()
    val uiEvent
        get() = _uiEvent.receiveAsFlow()
            .onEach { if (it is Domain) onDomainEvent(it) }

    fun onEvent(event: RegisterUiEvent) = screenModelScope.launch {
        _uiEvent.send(event)
    }

    private fun onDomainEvent(event: Domain) {
        when (event) {
            Domain.DoSignUp -> TODO()
            is Domain.OnConfirmPasswordChanged -> onConfirmPasswordChange(event.password)
            is Domain.OnConfirmPasswordVisibilityChanged -> onConfirmPasswordVisibilityChanged(event.isPasswordVisible)
            is Domain.OnEmailChanged -> onEmailChanged(event.email)
            is Domain.OnGoogleSignUpSuccess -> TODO()
            is Domain.OnPasswordChanged -> onPasswordChanged(event.password)
            is Domain.OnPasswordVisibilityChanged -> onPasswordVisibilityChanged(event.isPasswordVisible)
            is Domain.OnPrivacyConsentChanged -> onPrivacyConsentChanged(event.isChecked)
        }
    }

    private fun onEmailChanged(email: String) = screenModelScope.launch {
        _uiState.update { it.copy(email = email, emailError = email.checkEmail()) }
    }

    private fun onPasswordChanged(password: String) = screenModelScope.launch {
        _uiState.update {
            it.copy(
                password = password,
                passwordError = password.checkPassword(
                    it.confirmPassword
                ),
                confirmPasswordError = password.checkPassword(it.confirmPassword, true)
            )
        }
    }

    private fun onPasswordVisibilityChanged(isVisible: Boolean) = screenModelScope.launch {
        _uiState.update { it.copy(isPasswordVisible = isVisible) }
    }

    private fun onConfirmPasswordChange(password: String) = screenModelScope.launch {
        _uiState.update {
            it.copy(
                confirmPassword = password,
                confirmPasswordError = password.checkPassword(
                    it.password
                ),
                passwordError = password.checkPassword(it.password, true)
            )
        }
    }

    private fun onConfirmPasswordVisibilityChanged(isVisible: Boolean) = screenModelScope.launch {
        _uiState.update { it.copy(isConfirmPasswordVisible = isVisible) }
    }

    private fun onPrivacyConsentChanged(isChecked: Boolean) = screenModelScope.launch {
        _uiState.update { it.copy(isPrivacyConsentChecked = isChecked) }
    }
}
