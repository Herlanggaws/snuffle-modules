package com.logixmates.snuffle.auth.presentation.register.components

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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.logixmates.snuffle.auth.R
import com.logixmates.snuffle.auth.presentation.login.components.GoogleAuthButton
import com.logixmates.snuffle.auth.presentation.register.states.RegisterUiEvent
import com.logixmates.snuffle.auth.presentation.register.states.RegisterUiEvent.Domain
import com.logixmates.snuffle.auth.presentation.register.states.RegisterUiEvent.Presentation
import com.logixmates.snuffle.auth.presentation.register.states.RegisterUiState
import com.logixmates.snuffle.core.presentation.ANNOTATED_CLICKABLE
import com.logixmates.snuffle.core.presentation.themes.SnuffleColors
import com.logixmates.snuffle.core.presentation.toAnnotatedClickableText

@Composable
fun RegisterScreenInput(
    state: RegisterUiState,
    modifier: Modifier = Modifier,
    onEvent: (RegisterUiEvent) -> Unit = {}
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        TextField(
            value = state.email,
            onValueChange = {
                onEvent(Domain.OnEmailChanged(it))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            label = { Text(text = stringResource(R.string.email)) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(unfocusedContainerColor = SnuffleColors.Transparent),
            isError = !state.emailError.isNullOrBlank(),
            supportingText = { Text(text = state.emailError.orEmpty()) }
        )
        TextField(
            value = state.password,
            singleLine = true,
            onValueChange = {
                onEvent(Domain.OnPasswordChanged(it))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
            visualTransformation = if (state.isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(onClick = {
                    onEvent(Domain.OnPasswordVisibilityChanged(!state.isPasswordVisible))
                }) {
                    Icon(
                        if (state.isPasswordVisible) Icons.Rounded.RemoveRedEye else Icons.Rounded.HideSource,
                        contentDescription = "show/hide password"
                    )
                }
            },
            label = { Text(text = stringResource(R.string.password)) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(unfocusedContainerColor = SnuffleColors.Transparent),
            isError = !state.passwordError.isNullOrBlank(),
            supportingText = { Text(text = state.passwordError.orEmpty()) }
        )
        TextField(
            value = state.confirmPassword,
            singleLine = true,
            onValueChange = {
                onEvent(Domain.OnConfirmPasswordChanged(it))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            visualTransformation = if (state.isConfirmPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(onClick = {
                    onEvent(Domain.OnPasswordVisibilityChanged(!state.isConfirmPasswordVisible))
                }) {
                    Icon(
                        if (state.isConfirmPasswordVisible) Icons.Rounded.RemoveRedEye else Icons.Rounded.HideSource,
                        contentDescription = "show/hide password"
                    )
                }
            },
            label = { Text(text = stringResource(R.string.password)) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(unfocusedContainerColor = SnuffleColors.Transparent),
            isError = !state.confirmPasswordError.isNullOrBlank(),
            supportingText = { Text(text = state.confirmPasswordError.orEmpty()) }
        )
        Row(
            modifier = Modifier.padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(state.isPrivacyConsentChecked, onCheckedChange = {
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
            enabled = state.emailError == null &&
                    state.passwordError == null &&
                    state.confirmPasswordError == null &&
                    state.isPrivacyConsentChecked,
            onClick = {
                onEvent(Domain.DoSignUp)
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

        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(thickness = 1.dp, modifier = Modifier.weight(1f))
            Text("Or", modifier = Modifier.padding(horizontal = 16.dp))
            Divider(thickness = 1.dp, modifier = Modifier.weight(1f))
        }


        Text(
            text = "Sign up with",
            modifier = Modifier.padding(bottom = 16.dp)
        )
        GoogleAuthButton(onClick = { onEvent(Presentation.DoGoogleSignUp) })
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenInputPreview() {
    RegisterScreenInput(RegisterUiState())
}
