package com.logixmates.snuffle.core.presentation.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition

class SnuffleActivity : ComponentActivity() {
    @Suppress("DEPRECATION")
    private val startScreen by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(SCREEN_KEY, SnuffleScreens::class.java)
        } else {
            intent.getParcelableExtra(SCREEN_KEY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val targetScreen = rememberScreen(startScreen!!)
            Navigator(screen = targetScreen) {
                SlideTransition(it)
            }
        }
    }

    companion object {
        private const val SCREEN_KEY = "screen"
        fun start(context: Context, screen: SnuffleScreens) {
            context.startActivity(
                Intent(context, SnuffleActivity::class.java).apply {
                    putExtra(SCREEN_KEY, screen)
                }
            )
        }
    }
}
