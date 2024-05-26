package com.logixmates.snuffle.auth.presentation.navigation

import androidx.activity.ComponentActivity

interface AuthExtNavigator {
    fun with(activity: ComponentActivity): AuthExtNavigator
    fun onSignInSuccess()
}
