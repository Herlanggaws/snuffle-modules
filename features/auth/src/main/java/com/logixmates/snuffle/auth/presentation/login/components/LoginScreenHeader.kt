package com.logixmates.snuffle.auth.presentation.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.logixmates.snuffle.auth.R
import com.logixmates.snuffle.auth.presentation.login.states.LoginUiEvent
import com.logixmates.snuffle.auth.presentation.login.states.LoginUiEvent.Presentation
import com.logixmates.snuffle.core.presentation.themes.SnuffleColors

@Composable
fun LoginScreenHeader(modifier: Modifier = Modifier, onEvent: (LoginUiEvent) -> Unit = {}) {
    Column(modifier.fillMaxWidth()) {
        Row {
            val signUpButtonModifier = remember {
                Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .clickable { onEvent(Presentation.NavigateToSignUp) }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.sign_up),
                color = SnuffleColors.RoyalBlue,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = signUpButtonModifier
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
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenHeaderPreview() {
    LoginScreenHeader()
}
