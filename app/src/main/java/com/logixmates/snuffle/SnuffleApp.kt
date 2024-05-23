package com.logixmates.snuffle

import android.app.Application
import com.logixmates.snuffle.auth.di.AuthModules
import com.logixmates.snuffle.core.di.initKoin
import com.logixmates.snuffle.gate.di.GateModules
import com.logixmates.snuffle.gate.presentation.initGateScreenRegistry

class SnuffleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(AuthModules.all() + GateModules.all())
        initGateScreenRegistry()
    }
}
