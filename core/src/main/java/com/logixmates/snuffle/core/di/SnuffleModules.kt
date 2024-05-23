package com.logixmates.snuffle.core.di

import org.koin.core.module.Module

fun interface SnuffleModules {
    fun all(): List<Module>
}