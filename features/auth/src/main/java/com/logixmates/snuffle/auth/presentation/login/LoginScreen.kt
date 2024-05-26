package com.logixmates.snuffle.auth.presentation.login

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.logixmates.snuffle.auth.R
import com.logixmates.snuffle.auth.presentation.forgotpassword.ForgotPasswordScreen
import com.logixmates.snuffle.auth.presentation.login.components.LoginScreenHeader
import com.logixmates.snuffle.auth.presentation.login.components.LoginScreenInput
import com.logixmates.snuffle.auth.presentation.login.states.LoginUiEvent
import com.logixmates.snuffle.auth.presentation.login.states.LoginUiEvent.Presentation
import com.logixmates.snuffle.auth.presentation.login.states.LoginUiState
import com.logixmates.snuffle.auth.presentation.navigation.AuthExtNavigator
import com.logixmates.snuffle.auth.presentation.register.RegisterScreen
import com.logixmates.snuffle.core.presentation.components.Loader
import com.logixmates.snuffle.core.presentation.utils.intentToDefaultBrowser
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class LoginScreen : Screen, KoinComponent {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<LoginScreenModel>()
        val uiState = screenModel.uiState.collectAsStateWithLifecycle()
        val lifecycleOwner = LocalLifecycleOwner.current
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current
        val googleSignInClient by inject<GoogleSignInClient> { parametersOf(context as Activity) }
        val snackbarHostState = remember { SnackbarHostState() }
        val googleSignInResult = rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (result.data != null) {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(intent)
                    val account = task.result
                    screenModel.onEvent(
                        LoginUiEvent.Domain.OnGoogleLoginSuccess(
                            name = account.displayName,
                            providerId = account.id,
                            email = account.email
                        )
                    )
                }
            }
        }

        LaunchedEffect(Unit) {
            googleSignInClient.signOut()
        }

        LaunchedEffect(Unit) {
            screenModel.uiEvent.flowWithLifecycle(lifecycleOwner.lifecycle)
                .filterIsInstance<Presentation>()
                .onEach { navigator.onEvent(context, it, snackbarHostState) }
                .filterIsInstance<Presentation.DoGoogleLogin>()
                .onEach {
                    googleSignInResult.launch(googleSignInClient.signInIntent)
                }
                .launchIn(this)
        }
        LoginScreenContent(
            state = uiState,
            snackbarHostState = snackbarHostState,
            onEvent = screenModel::onEvent
        )
    }

    @Composable
    private fun LoginScreenContent(
        state: State<LoginUiState>,
        snackbarHostState: SnackbarHostState,
        modifier: Modifier = Modifier,
        onEvent: (LoginUiEvent) -> Unit = {}
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets.ime,
            modifier = modifier.imePadding(),
            snackbarHost = {
                SnackbarHost(snackbarHostState)
            }
        ) { padding ->
            Column(
                Modifier
                    .consumeWindowInsets(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                LoginScreenHeader(onEvent = onEvent)
                LoginScreenInput(
                    modifier = Modifier.padding(top = 24.dp),
                    state = state,
                    onEvent = onEvent
                )
                Text(
                    text = stringResource(R.string.login_disclaimer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
            Loader(isVisible = state.value.isLoading)
        }
    }

    private suspend fun Navigator.onEvent(
        context: Context,
        event: Presentation,
        snackbarHostState: SnackbarHostState
    ) {
        when (event) {
            Presentation.NavigateToSignUp -> push(RegisterScreen())
            Presentation.OnCookiePolicyClick -> COOKIES_URL.intentToDefaultBrowser(context)
            Presentation.OnSnufflePrivacyClick -> PRIVACY_POLICY_URL.intentToDefaultBrowser(context)
            Presentation.OnTermAndConditionClick -> TERM_URL.intentToDefaultBrowser(context)
            Presentation.NavigateToForgotPassword -> push(ForgotPasswordScreen())
            is Presentation.OnLoginFailed -> snackbarHostState.showSnackbar(
                event.message.toString(),
            )

            is Presentation.OnLoginSuccess -> {
                val extNavigator by inject<AuthExtNavigator>()
                (context as? ComponentActivity)?.run {
                    extNavigator.with(this).onSignInSuccess()
                    finish()
                }
            }

            else -> Unit
        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    private fun LoginScreenPreview() {
        val state = remember {
            mutableStateOf(LoginUiState())
        }
        val snackbarHostState = remember {
            SnackbarHostState()
        }
        LoginScreenContent(state = state, snackbarHostState = snackbarHostState)
    }

    companion object {
        const val PRIVACY_POLICY_URL = "https://snuffle.app/privacy-policy/"
        const val TERM_URL = "https://snuffle.app/terms-of-service/"
        const val COOKIES_URL = "https://snuffle.app/cookies/"
    }
}
