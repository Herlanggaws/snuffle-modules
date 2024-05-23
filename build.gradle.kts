plugins {
    alias(libs.plugins.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.com.android.library) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.gms.google.service) apply false
    alias(libs.plugins.crashlytics) apply false
}

group = "com.logixmates.snuffle"
version = "1.0.0"
