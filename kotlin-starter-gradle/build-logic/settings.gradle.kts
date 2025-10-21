dependencyResolutionManagement {

    // Separate from the root one as build-logic
    // is an independent project
    @Suppress("UnstableApiUsage")
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }


    // https://docs.gradle.org/current/userguide/version_catalogs.html#sec:buildsrc-version-catalog
    // until resolved, state dependencies and versions in convention plugins explicitly
    versionCatalogs {
        create("libs"){
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
