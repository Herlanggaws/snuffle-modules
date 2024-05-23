package com.logixmates.snuffle.gate.presentation

import cafe.adriel.voyager.core.registry.ScreenRegistry
import com.logixmates.snuffle.auth.presentation.modules.authScreenModules

fun initGateScreenRegistry() {
    ScreenRegistry {
        authScreenModules()
    }
}
