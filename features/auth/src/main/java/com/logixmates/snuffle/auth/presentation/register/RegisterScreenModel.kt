package com.logixmates.snuffle.auth.presentation.register

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.logixmates.snuffle.auth.data.model.LoginRequest
import com.logixmates.snuffle.auth.data.model.RegisterRequest
import com.logixmates.snuffle.auth.domain.usecase.LoginUseCase
import com.logixmates.snuffle.auth.domain.usecase.RegisterUseCase
import com.logixmates.snuffle.auth.presentation.forgotpassword.states.ForgotPasswordUiEvent
import com.logixmates.snuffle.auth.presentation.register.states.RegisterUiEvent
import com.logixmates.snuffle.auth.presentation.register.states.RegisterUiEvent.Domain
import com.logixmates.snuffle.auth.presentation.register.states.RegisterUiEvent.Presentation
import com.logixmates.snuffle.auth.presentation.register.states.RegisterUiState
import com.logixmates.snuffle.core.presentation.utils.checkEmail
import com.logixmates.snuffle.core.presentation.utils.checkPassword
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
class RegisterScreenModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    @Named("io") private val io: CoroutineDispatcher
) : ScreenModel {

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
            Domain.DoSignUp -> doRegister()
            is Domain.OnConfirmPasswordChanged -> onConfirmPasswordChange(event.password)
            is Domain.OnConfirmPasswordVisibilityChanged -> onConfirmPasswordVisibilityChanged(event.isPasswordVisible)
            is Domain.OnEmailChanged -> onEmailChanged(event.email)
            is Domain.OnGoogleSignUpSuccess -> doGoogleLogin(
                event.name,
                event.providerId,
                event.email
            )

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

    private fun doRegister() {
        requireNotNull(_uiState.value.email)
        requireNotNull(_uiState.value.password)
        requireNotNull(_uiState.value.confirmPassword)
        screenModelScope.launch(io) {
            registerUseCase.stream(
                StoreReadRequest.fresh(
                    RegisterRequest(
                        userName = _uiState.value.email,
                        password = _uiState.value.password,
                    )
                )
            ).onEach { state ->
                _uiState.update {
                    it.copy(isLoading = state is StoreReadResponse.Loading)
                }
                if (state is StoreReadResponse.Data) {
                    onEvent(Presentation.OnSignUpSuccess(state.value))
                }
                if (state is StoreReadResponse.Error) {
                    onEvent(Presentation.OnSignUpFailed(state.errorMessageOrNull()))
                }
            }.launchIn(this)
        }
    }

    private fun doGoogleLogin(name: String?, providerId: String?, email: String?) {
        streamLogin(
            LoginRequest(
                name = name,
                providerId = providerId,
                email = email,
                provider = "google"
            )
        )
    }

    private fun streamLogin(loginRequest: LoginRequest) = screenModelScope.launch(io) {
        loginUseCase.stream(StoreReadRequest.fresh(loginRequest)).onEach {
            when (it) {
                is StoreReadResponse.Loading -> _uiState.update { oldState ->
                    oldState.copy(isLoading = true)
                }

                is StoreReadResponse.Initial -> _uiState.update { oldState ->
                    oldState.copy(isLoading = false)
                }

                is StoreReadResponse.Error -> {
                    _uiState.update { oldState ->
                        oldState.copy(isLoading = false)
                    }
                    onEvent(Presentation.OnSignUpFailed(it.errorMessageOrNull()))
                }

                is StoreReadResponse.Data -> {
                    _uiState.update { oldState ->
                        oldState.copy(isLoading = false)
                    }
                    onEvent(Presentation.OnSignUpSuccess(it.value))
                }

                else -> Unit
            }
        }.launchIn(this)
    }
}
