package com.logixmates.snuffle.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.logixmates.snuffle.core.R
import com.logixmates.snuffle.core.presentation.themes.SnuffleColors

@Composable
fun Loader(isVisible: Boolean, modifier: Modifier = Modifier) {
    AnimatedVisibility(isVisible, enter = fadeIn(), exit = fadeOut()) {
        Column(
            modifier = modifier
                .background(SnuffleColors.Overlay)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottieloader))
            val progress by animateLottieCompositionAsState(composition)

            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(200.dp)
            )
            Text(
                stringResource(R.string.please_wait),
                color = Color.White
            )
        }
    }
}
