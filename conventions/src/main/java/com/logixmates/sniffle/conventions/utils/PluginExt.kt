package com.logixmates.sniffle.conventions.utils

import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import kotlin.jvm.optionals.getOrNull

fun Project.isApp(): Boolean {
    return pluginManager.hasPlugin("com.android.application")
}

fun Project.isLib(): Boolean {
    return pluginManager.hasPlugin("com.android.library")
}

fun Project.isAppOrLib(): Boolean {
    val isApp = isApp()
    val isLib = isLib()
    return isApp || isLib
}

fun Project.applyKotlinPlugins() {
    with(pluginManager) {
        applyPluginsWithLog("kotlin-android")
        applyPluginsWithLog("kotlin-parcelize")
    }
    dependencies {
        findLibs("kotlin-serialization")?.let { implementationWithLog(it) }
    }
}

fun PluginManager.applyPluginsWithLog(pluginId: String) {
    apply(pluginId)
    printMessage("Applying plugins: $pluginId")
}

fun Project.applyKspPlugins() {
    val kspPluginId = findPlugin("ksp", true) ?: return
    runCatching {
        pluginManager.apply(kspPluginId)
        the<KotlinAndroidProjectExtension>().apply {
            sourceSets.named("main") {
                kotlin.srcDir("build/generated/ksp/main/kotlin")
            }
            sourceSets.named("debug") {
                kotlin.srcDir("build/generated/ksp/debug/kotlin")
            }
            sourceSets.named("release") {
                kotlin.srcDir("build/generated/ksp/release/kotlin")
            }
        }
    }.onFailure {
        printMessage("KSP Plugins is not applied, please add ksp on root build.gradle.kts first")
        printMessage("use this: alias(libs.plugins.ksp) apply false", true)
    }.onSuccess {
        printMessage("Applying plugins: KSP")
    }
}

fun Project.findPlugin(
    alias: String,
    isPrintError: Boolean = false,
): String? {
    val kspPluginId = libs.findPlugin(alias).getOrNull()?.orNull?.pluginId
    if (kspPluginId == null) {
        if (isPrintError) printMessage("Cannot find $alias on version catalog, please check version")
        return null
    }
    return kspPluginId
}

fun Project.applyKoin() {
    dependencies {
        findLibs("koin-core")?.let { apiWithLog(it) }
        findLibs("koin-android")?.let { apiWithLog(it) }
        findLibs("koin-android-compat")?.let { apiWithLog(it) }
        findLibs("koin-androidx-workmanager")?.let { apiWithLog(it) }
        findLibs("koin-compose")?.let { apiWithLog(it) }
        findLibs("koin-androidx-compose")?.let { apiWithLog(it) }
        findLibs("koin-ktor")?.let { apiWithLog(it) }
        findLibs("koin-annotations")?.let { apiWithLog(it) }
        findLibs("koin-ksp-compiler")?.let { kspWithLog(it) }
        findLibs("koin-annotations-bom")?.let { apiPlatformWithLog(it) }
    }
}