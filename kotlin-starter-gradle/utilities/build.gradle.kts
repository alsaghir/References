plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    implementation(libs.bundles.kotlinxEcosystem)
    api(project(":list"))
}
