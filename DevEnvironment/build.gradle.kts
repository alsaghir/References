import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.notExists

plugins {
    base
}

fun volumeDir(): java.nio.file.Path {
    val userHome = System.getProperty("user.home")
    return Paths.get(userHome, providers.gradleProperty("distroboxRoot").get(), "${providers.gradleProperty("contaienrName").get()}-volume")
}

fun miseDir(): java.nio.file.Path {
    val userHome = System.getProperty("user.home")
    return volumeDir().resolve("mise")
}

tasks.register<Exec>("verifyContainerCreated") {
    group = "distrobox"
    description = "Verifies that the container was created and capture its name."
    environment("TERM", "dumb")

    commandLine("distrobox", "ls", "--no-color")
    standardOutput = ByteArrayOutputStream()

    doLast {
        val outText = standardOutput.toString()
        val containerName = outText.lines().drop(1).firstOrNull { it.trim().isNotEmpty() }
            ?.split("|")
            ?.get(1)?.trim()
            ?: providers.gradleProperty("contaienrName")
        logger.lifecycle("Container created: $containerName")
        project.ext.set("containerName", containerName)
    }
}

tasks.register<Exec>("setupDevTools") {
    group = "distrobox"
    description = "Installs tools inside the running container."
    dependsOn("verifyContainerCreated")

    val containerName = providers.gradleProperty("contaienrName")

    commandLine(
        "distrobox", "enter", containerName.get(), "--",
        "bash", "/tmp/${providers.gradleProperty("initScript").get()}"
    )
}

tasks.register<Exec>("cleanDesktopEntry") {
    group = "distrobox"
    description = "Clean desktop entry on the host"
    dependsOn("setupDevTools")

    val containerName = providers.gradleProperty("contaienrName")

    commandLine("distrobox", "generate-entry", containerName, "--delete")

    doLast {
        logger.lifecycle("Container Desktop entry cleaned")
    }
}

tasks.register("devEnvUp") {
    group = "distrobox"
    description = "Runs the full setup and provisioning sequence."
    dependsOn("cleanDesktopEntry")
}
