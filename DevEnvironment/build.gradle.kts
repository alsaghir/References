import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.notExists

plugins {
    base
}

object Constants {
    // Must be the same one in the dev-environment.ini file
    const val CONTAINER_NAME = "java-dev-box"
    const val CONFIG_FILE = "dev-environment.ini"
    const val INIT_SCRIPT = "init.sh"

}

fun volumeDir(): java.nio.file.Path {
    val userHome = System.getProperty("user.home")
    return Paths.get(userHome, "distrobox", "java-dev-box-volume")
}

fun miseDir(): java.nio.file.Path {
    val userHome = System.getProperty("user.home")
    return volumeDir().resolve("mise")
}

tasks.register<Exec>("createDistrobox") {
    group = "distrobox"
    description =
        "Creates/Replaces the development container if it doesn't exist. Should be run once at the start of the setup."

    environment("TERM", "dumb")

    if(volumeDir().notExists()){
        Files.createDirectory(volumeDir())
    }

    if(miseDir().notExists()){
        Files.createDirectory(miseDir())
    }

    commandLine("bash", "-lc", "distrobox assemble create --file ${Constants.CONFIG_FILE} -- nvidia")

    standardOutput = System.out
    errorOutput = System.err
    isIgnoreExitValue = false
}

tasks.register<Exec>("verifyContainerCreated") {
    group = "distrobox"
    description = "Verifies that the container was created and capture its name."
    environment("TERM", "dumb")
    dependsOn("createDistrobox")

    commandLine("distrobox", "ls", "--no-color")
    standardOutput = ByteArrayOutputStream()

    doLast {
        val outText = standardOutput.toString()
        val containerName = outText.lines().drop(1).firstOrNull { it.trim().isNotEmpty() }
            ?.split("|")
            ?.get(1)?.trim()
            ?: Constants.CONTAINER_NAME
        logger.lifecycle("Container created: $containerName")
        project.ext.set("containerName", containerName)
    }
}

tasks.register<Exec>("setupDevTools") {
    group = "distrobox"
    description = "Installs tools inside the running container."
    dependsOn("verifyContainerCreated")

    val containerName = Constants.CONTAINER_NAME

    commandLine(
        "distrobox", "enter", containerName, "--",
        "bash", "/tmp/${Constants.INIT_SCRIPT}"
    )
}

tasks.register("devEnvUp") {
    group = "distrobox"
    description = "Runs the full setup and provisioning sequence."
    dependsOn("setupDevTools")
}
