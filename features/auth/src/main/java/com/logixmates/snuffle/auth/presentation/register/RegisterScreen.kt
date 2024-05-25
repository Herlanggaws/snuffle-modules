package com.logixmates.snuffle.auth.presentation.register

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.logixmates.snuffle.auth.presentation.register.components.RegisterScreenInput
import com.logixmates.snuffle.auth.presentation.register.states.RegisterUiEvent
import com.logixmates.snuffle.auth.presentation.register.states.RegisterUiEvent.Domain
import com.logixmates.snuffle.auth.presentation.register.states.RegisterUiEvent.Presentation
import com.logixmates.snuffle.auth.presentation.register.states.RegisterUiState
import com.logixmates.snuffle.core.presentation.utils.intentToDefaultBrowser
import com.logixmates.snuffle.core.presentation.themes.SnuffleColors
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
        val googleSignInResult = rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (result.data != null) {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(intent)
                    screenModel.onEvent(Domain.OnGoogleSignUpSuccess(task.result.email.orEmpty()))
                }
            }
        }
        LaunchedEffect(Unit) {
            screenModel.uiEvent.flowWithLifecycle(lifecycleOwner.lifecycle)
                .filterIsInstance<Presentation>()
                .onEach { navigator.onEvent(context, it) }
                .filterIsInstance<Presentation.DoGoogleSignUp>()
                .onEach { googleSignInResult.launch(googleSignInClient.signInIntent) }
                .launchIn(this)
        }
        RegisterScreenContent(state = uiState, onEvent = screenModel::onEvent)
    }

    @Composable
    private fun RegisterScreenContent(
        state: RegisterUiState,
        modifier: Modifier = Modifier,
        onEvent: (RegisterUiEvent) -> Unit = {}
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Create an account",
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = SnuffleColors.RoyalBlue
            )
            HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            RegisterScreenInput(state, onEvent = onEvent)
            Text(
                text = stringResource(R.string.login_disclaimer),
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                textAlign = TextAlign.Center
            )
        }
    }

    private fun Navigator.onEvent(context: Context, event: Presentation) {
        when (event) {
            Presentation.OnCookiePolicyClick -> COOKIES_URL.intentToDefaultBrowser(context)
            Presentation.OnSnufflePrivacyClick -> PRIVACY_POLICY_URL.intentToDefaultBrowser(context)
            Presentation.OnTermAndConditionClick -> TERM_URL.intentToDefaultBrowser(context)
            else -> Unit
        }
    }

    @Composable
    @Preview(showBackground = true)
    private fun Preview() {
        RegisterScreenContent(RegisterUiState())
    }
}
