plugins {
    id("com.logixmates.sniffle.conventions.lib")
    id("com.logixmates.sniffle.conventions.compose")
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.kotlin.serialization)
    `maven-publish`
}

android {
    namespace = "${rootProject.group}.auth"

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
    implementation(project(":core"))
    implementation("com.google.android.gms:play-services-auth:20.4.0")
}

publishing {
    publications {
        register<MavenPublication>("debug") {
            groupId = rootProject.group.toString()
            artifactId = "features-auth"
            version = rootProject.version.toString()
            afterEvaluate {
                from(components["debug"])
            }
        }
        register<MavenPublication>("release") {
            groupId = rootProject.group.toString()
            artifactId = "features-auth"
            version = rootProject.version.toString()
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}
