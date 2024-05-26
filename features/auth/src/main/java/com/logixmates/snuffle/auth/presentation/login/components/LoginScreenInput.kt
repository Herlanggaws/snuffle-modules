package com.logixmates.snuffle.auth.presentation.login.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.HideSource
import androidx.compose.material.icons.rounded.RemoveRedEye
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.logixmates.snuffle.auth.R
import com.logixmates.snuffle.auth.presentation.login.states.LoginUiEvent
import com.logixmates.snuffle.auth.presentation.login.states.LoginUiEvent.Domain
import com.logixmates.snuffle.auth.presentation.login.states.LoginUiEvent.Presentation
import com.logixmates.snuffle.auth.presentation.login.states.LoginUiState
import com.logixmates.snuffle.core.presentation.themes.SnuffleColors
import com.logixmates.snuffle.core.presentation.utils.ANNOTATED_CLICKABLE
import com.logixmates.snuffle.core.presentation.utils.toAnnotatedClickableText

@Composable
fun LoginScreenInput(
    state: State<LoginUiState>,
    modifier: Modifier = Modifier,
    onEvent: (LoginUiEvent) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        TextField(
            value = state.value.email,
            onValueChange = {
                onEvent(Domain.OnEmailChanged(it))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            label = { Text(text = stringResource(R.string.email)) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(unfocusedContainerColor = SnuffleColors.Transparent),
            isError = !state.value.emailError.isNullOrBlank(),
            supportingText = { Text(text = state.value.emailError.orEmpty()) }
        )
        TextField(
            value = state.value.password,
            onValueChange = {
                onEvent(Domain.OnPasswordChanged(it))
            },
            visualTransformation = if (state.value.isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                IconButton(onClick = {
                    onEvent(Domain.OnPasswordVisibilityChanged(!state.value.isPasswordVisible))
                }) {
                    Icon(
                        if (state.value.isPasswordVisible) Icons.Rounded.RemoveRedEye else Icons.Rounded.HideSource,
                        contentDescription = "show/hide password"
                    )
                }
            },
            label = { Text(text = stringResource(R.string.password)) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(unfocusedContainerColor = SnuffleColors.Transparent),
            isError = !state.value.passwordError.isNullOrBlank(),
            supportingText = { Text(text = state.value.passwordError.orEmpty()) }
        )
        Row(
            modifier = Modifier.padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(state.value.isPrivacyConsentChecked, onCheckedChange = {
                onEvent(Domain.OnPrivacyConsentChanged(it))
            })
            val actions = remember {
                mapOf(
                    "Snuffle Privacy" to { onEvent(Presentation.OnSnufflePrivacyClick) },
                    "Snuffle aceptas las Políticas" to { onEvent(Presentation.OnSnufflePrivacyClick) },
                    "Cookie Policy" to { onEvent(Presentation.OnCookiePolicyClick) },
                    "Cookies de Snuffle" to { onEvent(Presentation.OnCookiePolicyClick) },
                    "Terms and Conditions Policy" to { onEvent(Presentation.OnTermAndConditionClick) },
                    "Términos y Condiciones." to { onEvent(Presentation.OnTermAndConditionClick) },
                )
            }
            val annotatedString =
                stringResource(R.string.term_and_condition).toAnnotatedClickableText(
                    actions.map { it.key }
                )
            ClickableText(annotatedString) { offset ->
                annotatedString.getStringAnnotations(
                    tag = ANNOTATED_CLICKABLE,
                    start = offset,
                    end = offset
                ).firstOrNull()?.let { annotation ->
                    actions[annotation.item]?.invoke()
                }
            }
        }
        Button(
            enabled = state.value.emailError == null &&
                    state.value.passwordError == null &&
                    state.value.isPrivacyConsentChecked,
            onClick = {
                focusManager.clearFocus()
                onEvent(Domain.DoLogin)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = SnuffleColors.RoyalBlue
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(stringResource(R.string.login))
        }

        val forgotPasswordModifier = remember {
            Modifier
                .clickable(role = Role.Button) {
                    onEvent(Presentation.NavigateToForgotPassword)
                }
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp)
        }

        Text(
            modifier = forgotPasswordModifier,
            textAlign = TextAlign.Center,
            text = stringResource(R.string.forgot_password)
        )

        HorizontalDivider(thickness = 1.dp)

        Text(
            text = stringResource(R.string.login_with),
            modifier = Modifier.padding(vertical = 16.dp)
        )
        GoogleAuthButton(onClick = { onEvent(Presentation.DoGoogleLogin) })
    }
}

@Preview(showBackground = true, locale = "es")
@Composable
fun LoginScreenInputPreview() {
    val state = remember {
        mutableStateOf(LoginUiState())
    }
    LoginScreenInput(state)
}
