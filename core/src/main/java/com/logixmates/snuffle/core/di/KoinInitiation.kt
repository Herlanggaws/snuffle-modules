package com.logixmates.snuffle.core.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module

fun Application.initKoin(modules: List<Module>) {
    startKoin {
        printLogger()
        androidLogger()
        androidContext(this@initKoin)
        modules(CoreModule.all() + modules)
    }
}
