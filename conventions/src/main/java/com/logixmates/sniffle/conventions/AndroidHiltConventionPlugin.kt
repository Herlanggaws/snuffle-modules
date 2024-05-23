package com.logixmates.sniffle.conventions

import com.logixmates.sniffle.conventions.utils.applyPluginsWithLog
import com.logixmates.sniffle.conventions.utils.findLibs
import com.logixmates.sniffle.conventions.utils.findPlugin
import com.logixmates.sniffle.conventions.utils.implementationWithLog
import com.logixmates.sniffle.conventions.utils.kspWithLog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                val hiltPluginId = findPlugin("hilt", true) ?: return
                applyPluginsWithLog(hiltPluginId)
            }
            dependencies {
                findLibs("dagger-hilt")?.let { implementationWithLog(it) }
                findLibs("dagger-hilt-android-compiler")?.let { kspWithLog(it) }
                findLibs("dagger-hilt-compiler")?.let { kspWithLog(it) }
                findLibs("dagger-hilt-compose-navigation")?.let { implementationWithLog(it) }
            }
        }
    }
}
