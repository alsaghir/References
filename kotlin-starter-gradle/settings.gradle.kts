/**
 * settings.gradle.kts specifies what projects included
 * in the build
 */
pluginManagement {

    // for gradle itself
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    // Include 'plugins build' to define convention plugins.
    includeBuild("build-logic")
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {

    // for the modules/projects source code
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }
}

plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}


rootProject.name = "gradleInitialized"
include("app", "list", "utilities")
