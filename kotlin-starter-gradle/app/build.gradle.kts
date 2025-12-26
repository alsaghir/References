plugins {
    id("buildlogic.kotlin-application-conventions")
}

dependencies {
    implementation(libs.commonsText)
    implementation(project(":utilities"))
}

application {
    // Define the main class for the application.
    mainClass = "example.pname.app.AppKt"
}
