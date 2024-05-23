package com.logixmates.sniffle.conventions.utils

import org.gradle.api.JavaVersion
import org.gradle.api.Project

fun Project.getJavaVersion(): JavaVersion {
    val jvmTargetVersion = libs.findVersion("jvm-target").get().toString()
    return JavaVersion.toVersion(jvmTargetVersion).also {
        printMessage("Setting up JDK to: $it")
    }
}
