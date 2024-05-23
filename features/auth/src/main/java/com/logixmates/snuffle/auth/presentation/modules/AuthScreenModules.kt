package com.logixmates.snuffle.auth.presentation.modules

import cafe.adriel.voyager.core.registry.screenModule
import com.logixmates.snuffle.auth.presentation.login.LoginScreen
import com.logixmates.snuffle.core.presentation.SnuffleScreens

val authScreenModules = screenModule {
    register<SnuffleScreens.Auth.Login> {
        LoginScreen()
    }
}
