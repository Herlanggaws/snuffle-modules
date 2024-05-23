plugins {
    id("com.logixmates.sniffle.conventions.lib")
    id("com.logixmates.sniffle.conventions.compose")
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.kotlin.serialization)
    `maven-publish`
}

android {
    namespace = "${rootProject.group}.core"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    publishing {
        singleVariant("debug") {
            withSourcesJar()
        }
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(libs.mmkv)
    api(libs.sealedx.core)
    ksp(libs.sealedx.processor)
}

publishing {
    publications {
        register<MavenPublication>("debug") {
            groupId = rootProject.group.toString()
            artifactId = "core"
            version = rootProject.version.toString()
            afterEvaluate {
                from(components["debug"])
            }
        }
        register<MavenPublication>("release") {
            groupId = rootProject.group.toString()
            artifactId = "core"
            version = rootProject.version.toString()
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}
