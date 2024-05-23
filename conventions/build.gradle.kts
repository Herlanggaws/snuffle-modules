plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    alias(libs.plugins.ksp)
}

dependencies {
    compileOnly(libs.plugin.agp)
    compileOnly(libs.plugin.kgp)
    compileOnly(libs.plugin.ksp)
    compileOnly(libs.plugin.compose)
}

gradlePlugin {
    val androidTarget by plugins.creating {
        id = "com.logixmates.sniffle.conventions.target"
        implementationClass = "com.logixmates.sniffle.conventions.AndroidTargetConventionPlugin"
    }
    val app by plugins.creating {
        id = "com.logixmates.sniffle.conventions.app"
        implementationClass = "com.logixmates.sniffle.conventions.AndroidAppConventionPlugin"
    }
    val lib by plugins.creating {
        id = "com.logixmates.sniffle.conventions.lib"
        implementationClass = "com.logixmates.sniffle.conventions.AndroidLibConventionPlugin"
    }
    val compose by plugins.creating {
        id = "com.logixmates.sniffle.conventions.compose"
        implementationClass = "com.logixmates.sniffle.conventions.AndroidComposeConventionPlugin"
    }
    val hilt by plugins.creating {
        id = "com.logixmates.sniffle.conventions.hilt"
        implementationClass = "com.logixmates.sniffle.conventions.AndroidHiltConventionPlugin"
    }
}