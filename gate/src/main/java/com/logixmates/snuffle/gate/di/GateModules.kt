package com.logixmates.snuffle.gate.di

import com.logixmates.snuffle.core.di.SnuffleModules
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.ksp.generated.module

@ComponentScan("com.logixmates.snuffle.gate")
@Module
object GateModules: SnuffleModules {
    override fun all(): List<org.koin.core.module.Module> {
        return listOf(module)
    }
}