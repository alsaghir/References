import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    kotlin("jvm")
}

val libs = the<LibrariesForLibs>()

dependencies {
    constraints {
        implementation("org.apache.commons:commons-text:1.13.0")
        implementation(libs.commonsText)
        implementation("org.springframework.boot:spring-boot-dependencies:3.5.6")
    }

    testImplementation(kotlin("test"))
}

tasks.withType<Test>().configureEach {

    useJUnitPlatform()

    // Log information about all test results, not only the failed ones.
    testLogging {
        events(
            TestLogEvent.FAILED,
            TestLogEvent.PASSED,
            TestLogEvent.SKIPPED
        )
    }
}


// Selects the JDK for Java tasks: compileJava, javadoc, test (runtime), JavaExec, etc.
// Gradle will provision that JDK via toolchains if needed.
// Does not affect the Kotlin compiler unless the Kotlin plugin inherits it.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

// If a Java version specified,
// Selects the JDK used to run Kotlin/JVM tasks: compileKotlin, KAPT/KSP, etc.
// Does not control the JDK used to run tests or Java compilation tasks.
kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}
