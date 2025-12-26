plugins {
    id("buildlogic.kotlin-common-conventions")

    id("org.jetbrains.kotlin.plugin.serialization")

    // Apply the java-library plugin for API and implementation separation.
    `java-library`
}
