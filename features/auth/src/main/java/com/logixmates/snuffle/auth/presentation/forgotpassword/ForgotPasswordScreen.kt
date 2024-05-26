package com.logixmates.snuffle.auth.presentation.forgotpassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.logixmates.snuffle.auth.R
import com.logixmates.snuffle.auth.presentation.forgotpassword.states.ForgotPasswordUiEvent
import com.logixmates.snuffle.auth.presentation.forgotpassword.states.ForgotPasswordUiEvent.Domain
import com.logixmates.snuffle.auth.presentation.forgotpassword.states.ForgotPasswordUiEvent.Presentation
import com.logixmates.snuffle.auth.presentation.forgotpassword.states.ForgotPasswordUiState
import com.logixmates.snuffle.core.presentation.components.Loader
import com.logixmates.snuffle.core.presentation.themes.SnuffleColors
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent

class ForgotPasswordScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<ForgotPasswordModel>()
        val uiState = screenModel.uiState.collectAsStateWithLifecycle()
        val lifecycleOwner = LocalLifecycleOwner.current
        val navigator = LocalNavigator.currentOrThrow
        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(Unit) {
            screenModel.uiEvent.flowWithLifecycle(lifecycleOwner.lifecycle)
                .filterIsInstance<Presentation>()
                .onEach { navigator.onEvent(it, snackbarHostState) }
                .launchIn(this)
        }

        ForgotPasswordScreenContent(
            state = uiState,
            snackbarHostState = snackbarHostState,
            onEvent = screenModel::onEvent
        )
    }

    @Composable
    private fun ForgotPasswordScreenContent(
        state: State<ForgotPasswordUiState>,
        snackbarHostState: SnackbarHostState,
        modifier: Modifier = Modifier,
        onEvent: (ForgotPasswordUiEvent) -> Unit = {}
    ) {
        val focusManager = LocalFocusManager.current
        Scaffold(
            contentWindowInsets = WindowInsets.ime,
            snackbarHost = {
                SnackbarHost(snackbarHostState)
            },
            modifier = modifier.imePadding()
        ) { padding ->
            Column(
                Modifier
                    .consumeWindowInsets(padding)
                    .padding(16.dp)
            ) {
                IconButton({
                    onEvent(Presentation.NavigateUp)
                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate Up",
                        tint = SnuffleColors.RoyalBlue
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_splash_name),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .height(60.dp),
                    alignment = Alignment.Center,
                    contentDescription = "logo"
                )
                TextField(
                    value = state.value.email,
                    onValueChange = {
                        onEvent(Domain.OnEmailChanged(it))
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    label = { Text(text = stringResource(R.string.email)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = SnuffleColors.Transparent),
                    isError = !state.value.emailError.isNullOrBlank(),
                    supportingText = { Text(text = state.value.emailError.orEmpty()) }
                )

                Button(
                    enabled = state.value.emailError == null,
                    onClick = {
                        focusManager.clearFocus()
                        onEvent(Domain.SubmitRequest)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SnuffleColors.RoyalBlue
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text(stringResource(R.string.request_reset_password))
                }
            }
            Loader(isVisible = state.value.isLoading)
        }
    }

    private suspend fun Navigator.onEvent(
        event: Presentation,
        snackbarHostState: SnackbarHostState
    ) {
        when (event) {
            Presentation.NavigateUp -> pop()
            is Presentation.ShowError -> event.msg?.let { snackbarHostState.showSnackbar(message = it) }

            Presentation.ShowSuccess -> snackbarHostState.showSnackbar(message = "Success sent email")
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun ForgotPasswordScreenPreview() {
        val state = remember {
            mutableStateOf(ForgotPasswordUiState())
        }
        val snackbarHostState = remember {
            SnackbarHostState()
        }
        ForgotPasswordScreenContent(state = state, snackbarHostState = snackbarHostState)
    }
}
