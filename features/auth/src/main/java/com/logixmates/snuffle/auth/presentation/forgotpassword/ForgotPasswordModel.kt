package com.logixmates.snuffle.auth.presentation.forgotpassword

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.logixmates.snuffle.auth.domain.usecase.ForgotPasswordUseCase
import com.logixmates.snuffle.auth.presentation.forgotpassword.states.ForgotPasswordUiEvent
import com.logixmates.snuffle.auth.presentation.forgotpassword.states.ForgotPasswordUiEvent.Domain
import com.logixmates.snuffle.auth.presentation.forgotpassword.states.ForgotPasswordUiState
import com.logixmates.snuffle.core.data.model.Response
import com.logixmates.snuffle.core.presentation.checkEmail
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
import org.mobilenativefoundation.store.store5.Validator

@Factory
class ForgotPasswordModel(
    @Named("io") private val io: CoroutineDispatcher,
    private val useCase: ForgotPasswordUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState get() = _uiState.asStateFlow()

    private val _uiEvent = Channel<ForgotPasswordUiEvent>()
    val uiEvent
        get() = _uiEvent.receiveAsFlow()
            .onEach { if (it is Domain) onDomainEvent(it) }

    fun onEvent(event: ForgotPasswordUiEvent) = screenModelScope.launch {
        _uiEvent.send(event)
    }

    private fun onDomainEvent(event: Domain) {
        when (event) {
            is Domain.OnEmailChanged -> onEmailChanged(event.email)
            Domain.SubmitRequest -> onRequestSubmitted()
        }
    }

    private fun onRequestSubmitted() = screenModelScope.launch(io) {
        requireNotNull(_uiState.value.email)
        useCase.stream(
            StoreReadRequest.fresh(_uiState.value.email)
        ) {
            validator(object: Validator<Response> {
                override suspend fun isValid(item: Response): Boolean {
                    return item.error.isNullOrEmpty()
                }
            })
        }
            .onEach { state ->
                _uiState.update {
                    it.copy(isLoading = state is StoreReadResponse.Loading)
                }
                if (state is StoreReadResponse.Data) {
                    onEvent(ForgotPasswordUiEvent.Presentation.ShowSuccess)
                }
                if (state is StoreReadResponse.Error) {
                    val error = state.errorOrNull<Error>()
                    onEvent(ForgotPasswordUiEvent.Presentation.ShowError(error?.message.toString()))
                }
            }.launchIn(this)
    }

    private fun onEmailChanged(email: String) = screenModelScope.launch {
        _uiState.update { it.copy(email = email, emailError = email.checkEmail()) }
    }
}
