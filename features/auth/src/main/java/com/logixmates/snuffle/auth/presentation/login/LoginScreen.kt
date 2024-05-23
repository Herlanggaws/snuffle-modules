package com.logixmates.snuffle.auth.presentation.login

import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.logixmates.snuffle.auth.presentation.register.RegisterScreen
import com.logixmates.snuffle.core.presentation.intentToDefaultBrowser
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
        val googleSignInResult = rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (result.data != null) {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(intent)
                    screenModel.onEvent(LoginUiEvent.Domain.OnGoogleLoginSuccess(task.result.email.orEmpty()))
                }
            }
        }

        LaunchedEffect(Unit) {
            googleSignInClient.signOut()
        }

        LaunchedEffect(Unit) {
            screenModel.uiEvent.flowWithLifecycle(lifecycleOwner.lifecycle)
                .filterIsInstance<Presentation>()
                .onEach { navigator.onEvent(context, it) }
                .filterIsInstance<Presentation.DoGoogleLogin>()
                .onEach { googleSignInResult.launch(googleSignInClient.signInIntent) }
                .launchIn(this)
        }
        LoginScreenContent(uiState, onEvent = screenModel::onEvent)
    }

    @Composable
    private fun LoginScreenContent(
        state: State<LoginUiState>,
        modifier: Modifier = Modifier,
        onEvent: (LoginUiEvent) -> Unit = {}
    ) {
        Column(
            modifier
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
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                textAlign = TextAlign.Center
            )
        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    private fun LoginScreenPreview() {
        val state = remember {
            mutableStateOf(LoginUiState())
        }
        LoginScreenContent(state = state)
    }

    private fun Navigator.onEvent(context: Context, event: Presentation) {
        when (event) {
            Presentation.NavigateToSignUp -> push(RegisterScreen())
            Presentation.OnCookiePolicyClick -> COOKIES_URL.intentToDefaultBrowser(context)
            Presentation.OnSnufflePrivacyClick -> PRIVACY_POLICY_URL.intentToDefaultBrowser(context)
            Presentation.OnTermAndConditionClick -> TERM_URL.intentToDefaultBrowser(context)
            Presentation.NavigateToForgotPassword -> push(ForgotPasswordScreen())
            else -> Unit
        }
    }

    companion object {
        const val PRIVACY_POLICY_URL = "https://snuffle.app/privacy-policy/"
        const val TERM_URL = "https://snuffle.app/terms-of-service/"
        const val COOKIES_URL = "https://snuffle.app/cookies/"
    }
}
