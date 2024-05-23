package com.logixmates.sniffle.conventions

import com.logixmates.sniffle.conventions.utils.applyKoin
import com.logixmates.sniffle.conventions.utils.applyKotlinPlugins
import com.logixmates.sniffle.conventions.utils.applyKspPlugins
import com.logixmates.sniffle.conventions.utils.applyNetwork
import com.logixmates.sniffle.conventions.utils.applyPluginsWithLog
import com.logixmates.sniffle.conventions.utils.commonDependencies
import com.logixmates.sniffle.conventions.utils.findLibs
import com.logixmates.sniffle.conventions.utils.implementationWithLog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidAppConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                applyPluginsWithLog("com.android.application")
                applyKotlinPlugins()
                applyKspPlugins()
                applyKoin()
                applyNetwork()
                applyPluginsWithLog("com.logixmates.sniffle.conventions.target")
                commonDependencies()
            }
        }
    }
}
