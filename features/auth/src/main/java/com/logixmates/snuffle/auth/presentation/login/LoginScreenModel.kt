package com.logixmates.snuffle.auth.presentation.login

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.logixmates.snuffle.auth.data.model.LoginRequest
import com.logixmates.snuffle.auth.domain.usecase.LoginUseCase
import com.logixmates.snuffle.auth.presentation.login.states.LoginUiEvent
import com.logixmates.snuffle.auth.presentation.login.states.LoginUiEvent.Domain
import com.logixmates.snuffle.auth.presentation.login.states.LoginUiState
import com.logixmates.snuffle.core.presentation.checkEmail
import com.logixmates.snuffle.core.presentation.checkPassword
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse

@Factory
class LoginScreenModel(
    private val loginUseCase: LoginUseCase,
    @Named("io") private val io: CoroutineDispatcher
) : ScreenModel {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState get() = _uiState.asStateFlow()

    private val _uiEvent = Channel<LoginUiEvent>()
    val uiEvent
        get() = _uiEvent.receiveAsFlow()
            .onEach { if (it is Domain) onDomainEvent(it) }

    fun onEvent(event: LoginUiEvent) = screenModelScope.launch {
        _uiEvent.send(event)
    }

    private fun onDomainEvent(event: Domain) {
        when (event) {
            is Domain.DoLogin -> doLogin()
            is Domain.OnEmailChanged -> onEmailChanged(event.email)
            is Domain.OnPasswordChanged -> onPasswordChanged(event.password)
            is Domain.OnPasswordVisibilityChanged -> onPasswordVisibilityChanged(event.isPasswordVisible)
            is Domain.OnPrivacyConsentChanged -> onPrivacyConsentChanged(event.isChecked)
            is Domain.OnGoogleLoginSuccess -> TODO()
        }
    }

    private fun onEmailChanged(email: String) = screenModelScope.launch {
        _uiState.update { it.copy(email = email, emailError = email.checkEmail()) }
    }

    private fun onPasswordChanged(password: String) = screenModelScope.launch {
        _uiState.update { it.copy(password = password, passwordError = password.checkPassword()) }
    }

    private fun onPasswordVisibilityChanged(isVisible: Boolean) = screenModelScope.launch {
        _uiState.update { it.copy(isPasswordVisible = isVisible) }
    }

    private fun onPrivacyConsentChanged(isChecked: Boolean) = screenModelScope.launch {
        _uiState.update { it.copy(isPrivacyConsentChecked = isChecked) }
    }

    private fun doLogin() = screenModelScope.launch(io) {
        require(_uiState.value.email.isNotBlank())
        require(_uiState.value.password.isNotBlank())
        loginUseCase.stream(
            StoreReadRequest.fresh(
                LoginRequest(
                    "joseph",
                    "",
                    "",
                    "",
                    ""
                )
            )
        ).onEach {
            when (it) {
                is StoreReadResponse.Loading -> _uiState.update { oldState ->
                    oldState.copy(isLoading = true)
                }

                is StoreReadResponse.Initial -> _uiState.update { oldState ->
                    oldState.copy(isLoading = false)
                }

                is StoreReadResponse.Error -> _uiState.update { oldState ->
                    oldState.copy(isLoading = false, loginErrorMessage = it.errorMessageOrNull())
                }

                is StoreReadResponse.Data -> _uiState.update { oldState ->
                    oldState.copy(isLoading = false, loginData = it.value)
                }

                else -> Unit
            }
        }.launchIn(this)
    }
}