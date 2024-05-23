package com.logixmates.sniffle.conventions.utils

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.applyNetwork() {
    dependencies {
        findLibs("ktorfit-lib")?.let { implementationWithLog(it) }
        findLibs("ktor-okhttp")?.let { implementationWithLog(it) }
        findLibs("ktor-content-negotiation")?.let { implementationWithLog(it) }
        findLibs("ktor-kotlin-serialization")?.let { implementationWithLog(it) }
        findLibs("ktorfit-ksp")?.let { kspWithLog(it) }
        findLibs("chucker")?.let { add("debugImplementation", it) }
        findLibs("chucker-no-op")?.let { add("releaseImplementation", it) }
    }
}