import org.gradle.kotlin.dsl.plugins

plugins {
    // Support convention plugins written in Kotlin.
    // Convention plugins are build scripts in 'src/main' that automatically
    // become available as plugins in the main build.
    // Convention plugins are located in `src/main/kotlin`, with the file extension `.gradle.kts`,
    // and are applied in the project's `build.gradle.kts` files as required.
    `kotlin-dsl`
}

kotlin {
    jvmToolchain(21)
}

dependencies {

    // Plugins to be applied by the convention plugins must be declared as dependencies here.
    implementation(libs.kotlinGradlePlugin)

    // https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.serialization
    implementation(libs.kotlinPluginSerialization)

    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
