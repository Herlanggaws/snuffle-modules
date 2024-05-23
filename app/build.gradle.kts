plugins {
    id("com.logixmates.sniffle.conventions.app")
    id("com.logixmates.sniffle.conventions.compose")
}

android {
    namespace = "com.logixmates.snuffle"

    defaultConfig {
        applicationId = "com.logixmates.snuffle"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":gate"))
    implementation(project(":features:auth"))
}