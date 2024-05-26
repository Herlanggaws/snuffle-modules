package com.logixmates.snuffle.auth.presentation.register

import android.app.Activity
import android.content.Context
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.logixmates.snuffle.auth.presentation.login.LoginScreen.Companion.COOKIES_URL
import com.logixmates.snuffle.auth.presentation.login.LoginScreen.Companion.PRIVACY_POLICY_URL
import com.logixmates.snuffle.auth.presentation.login.LoginScreen.Companion.TERM_URL
import com.logixmates.snuffle.auth.presentation.login.states.LoginUiEvent
import com.logixmates.snuffle.auth.presentation.navigation.AuthExtNavigator
import com.logixmates.snuffle.auth.presentation.register.components.RegisterScreenInput
import com.logixmates.snuffle.auth.presentation.register.states.RegisterUiEvent
import com.logixmates.snuffle.auth.presentation.register.states.RegisterUiEvent.Domain
import com.logixmates.snuffle.auth.presentation.register.states.RegisterUiEvent.Presentation
import com.logixmates.snuffle.auth.presentation.register.states.RegisterUiState
import com.logixmates.snuffle.core.presentation.components.Loader
import com.logixmates.snuffle.core.presentation.themes.SnuffleColors
import com.logixmates.snuffle.core.presentation.utils.intentToDefaultBrowser
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class RegisterScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<RegisterScreenModel>()
        val uiState by screenModel.uiState.collectAsStateWithLifecycle()
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
                        Domain.OnGoogleSignUpSuccess(
                            name = account.displayName,
                            providerId = account.id,
                            email = account.email
                        )
                    )
                }
            }
        }
        LaunchedEffect(Unit) {
            screenModel.uiEvent.flowWithLifecycle(lifecycleOwner.lifecycle)
                .filterIsInstance<Presentation>()
                .onEach { navigator.onEvent(context, it, snackbarHostState) }
                .filterIsInstance<Presentation.DoGoogleSignUp>()
                .onEach { googleSignInResult.launch(googleSignInClient.signInIntent) }
                .launchIn(this)
        }
        RegisterScreenContent(
            state = uiState,
            snackbarHostState = snackbarHostState,
            onEvent = screenModel::onEvent
        )
    }

    @Composable
    private fun RegisterScreenContent(
        state: RegisterUiState,
        snackbarHostState: SnackbarHostState,
        modifier: Modifier = Modifier,
        onEvent: (RegisterUiEvent) -> Unit = {}
    ) {
        Scaffold(
            modifier = modifier.imePadding(),
            contentWindowInsets = WindowInsets.ime,
            snackbarHost = {
                SnackbarHost(snackbarHostState)
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .consumeWindowInsets(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(R.string.create_an_account),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = SnuffleColors.RoyalBlue
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                RegisterScreenInput(state, onEvent = onEvent)
                Text(
                    text = stringResource(R.string.login_disclaimer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
            Loader(state.isLoading)
        }
    }

    private suspend fun Navigator.onEvent(
        context: Context,
        event: Presentation,
        snackbarHostState: SnackbarHostState
    ) {
        when (event) {
            Presentation.OnCookiePolicyClick -> COOKIES_URL.intentToDefaultBrowser(context)
            Presentation.OnSnufflePrivacyClick -> PRIVACY_POLICY_URL.intentToDefaultBrowser(context)
            Presentation.OnTermAndConditionClick -> TERM_URL.intentToDefaultBrowser(context)
            is Presentation.OnSignUpFailed -> snackbarHostState.showSnackbar(message = event.message.toString())
            is Presentation.OnSignUpSuccess -> {
                val extNavigator by inject<AuthExtNavigator>()
                (context as? ComponentActivity)?.run {
                    extNavigator.with(this).onSignInSuccess()
                    finish()
                }
            }
            else -> Unit
        }
    }

    @Composable
    @Preview(showBackground = true)
    private fun Preview() {
        RegisterScreenContent(RegisterUiState(), SnackbarHostState())
    }
}
