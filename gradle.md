# Gradle

## References

- [Getting Started](https://docs.gradle.org/current/userguide/getting_started.html)
- [Organizing Gradle Projects](https://docs.gradle.org/current/userguide/organizing_gradle_projects.html#organizing_gradle_projects)
- [Authoring Tasks](https://docs.gradle.org/current/userguide/more_about_tasks.html#sec:passing_arguments_to_a_task_constructor)
- [DSL Reference](https://docs.gradle.org/current/dsl/index.html)
- [Gradle Task Tree](https://github.com/dorongold/gradle-task-tree)
- [Using Gradle Plugins](https://docs.gradle.org/current/userguide/plugins.html#sec:types_of_plugins) & [Create a plugin](https://docs.gradle.org/current/userguide/custom_plugins.html)
- [Apply plugin using `plugins` DSL](https://docs.gradle.org/current/userguide/plugins.html#sec:plugins_block)
- [Plugins Portal](https://plugins.gradle.org/)
- [Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html#java_plugin)
- [Base Plugin](https://docs.gradle.org/current/userguide/base_plugin.html#header)
- [Maven phases to Gradle tasks](https://docs.gradle.org/current/userguide/migrating_from_maven.html#migmvn:build_lifecycle)
- [Task Configuration Avoidance](https://docs.gradle.org/current/userguide/task_configuration_avoidance.html#task_configuration_avoidance)
- [Responding to the lifecycle in the build script](https://docs.gradle.org/current/userguide/build_lifecycle.html#build_lifecycle_events)
- [Configuring the Build Environment](https://docs.gradle.org/current/userguide/build_environment.html#sec:gradle_configuration_properties)
- [Listing dependencies in a project](https://docs.gradle.org/current/userguide/dependency_management.html#sec:listing_dependencies)
- [Spring boot Gradle plugin](https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/htmlsingle/)
- [Init scripts](https://docs.gradle.org/current/userguide/init_scripts.html)
- [Dependency configurations (implementation, runtimeOnly, testImplementation..etc)](https://docs.gradle.org/current/userguide/declaring_dependencies.html#sec:what-are-dependency-configurations)

## Commands

```powershell
# Do not execute tasks but show what would be executed
gradle helloWorld --dry-run

# List all tasks
gradle tasks --all

# initialize inside new project folder
gradle init --dsl kotlin

# List dependencies
# https://docs.gradle.org/current/dsl/org.gradle.api.tasks.diagnostics.DependencyReportTask.html
gradle dependencies
gradle :app:dependencies
gradle dependencies --configuration runtime

# implemented in DependencyInsightReportTask
# It allows to limit a dependencies tree only to selected dependency (also transitive).
# https://docs.gradle.org/current/dsl/org.gradle.api.tasks.diagnostics.DependencyInsightReportTask.html
gradle :app:dependencyInsight --configuration testRuntimeClassPath --dependency spring-core
gradle :app:dependencyInsight --configuration testRuntimeClassPath --dependency org.slf4j:slf4j-simple:1.7.7 
```

## Notes

- Settings file executed during initialization phase. `settings.gradle.kts` file in the root project of the multi-project hierarchy.
- Build phases: A Gradle build has three distinct phases.
	- **Initialization**: Gradle supports single and multi-project builds. During the initialization phase, Gradle determines which projects are going to take part in the build, and creates a Project instance for each of these projects.
	- **Configuration**: During this phase the project objects are configured. The build scripts of all projects which are part of the build are executed.
	- **Execution**: Gradle determines the subset of the tasks, created and configured during the configuration phase, to be executed. The subset is determined by the task name arguments passed to the gradle command and the current directory. Gradle then executes each of the selected tasks.
- Maven phases to Gradle tasks


## Examples

```kotlin
// Apply script plugin from external script file
apply(from = "other.gradle.kts")
```

```kotlin
// Plugins
plugins {
	id("org.springframework.boot") version "2.6.6"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("java")
}

// build metadata
group = "org.goodat"
version = "0.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

// Where to find dependencies
repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("mysql:mysql-connector-java")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.named<Test>("test") {
	useJUnitPlatform()
}
```

```kotlin
// Default tasks are executed if no other tasks are specified
defaultTasks("clean", "run")

// Using Kotlin delegated properties to register new tasks
// Task named hello and referenced with hello variable
val hello by tasks.registering {
    doLast {
        println("hello")
    }
}

val copy by tasks.registering(Copy::class) {
    from(file("srcDir"))
    into(buildDir)
}

// Accessing task by name
println(tasks.named("hello").get().name) // or just 'tasks.hello' if the task was added by a plugin
println(hello.get().name) // or just 'tasks.hello' if the task was added by a plugin
println(tasks.named<Copy>("copy").get().destinationDir)

// Configuring task already exists by name
tasks.named<Copy>("myCopy") {
    from("resources")
    into("target")
    include("**/*.txt", "**/*.xml", "**/*.properties")
}

// Accessing tasks with type
tasks.withType<Tar>().configureEach {
    enabled = false
}

// new task by name that depends on other task by type
tasks.register("test") {
    dependsOn(tasks.withType<Copy>())
}

// Accessing tasks by path
tasks.register("hello") // project-a/build.gradle.kts
tasks.register("hello") // build.gradle.kts

// Output of
// gradle -q hello
// :hello
// :hello
// :project-a:hello
// :project-a:hello
println(tasks.getByPath("hello").path)
println(tasks.getByPath(":hello").path)
println(tasks.getByPath("project-a:hello").path)
println(tasks.getByPath(":project-a:hello").path)

// Configure task using Kotlin delegated properties and a lambda
// first reference existing task by type
val myCopy by tasks.existing(Copy::class) {
    from("resources")
    into("target")
}
// configure the task by referenced variable
myCopy {
    include("**/*.txt", "**/*.xml", "**/*.properties")
}

// Configure task added by plugin
tasks.test {
	// whatever
}

// Register new task called allDeps for all projects
// Running gradle allDeps with execute 'dependencies' task
// which is coming from 'DependencyReportTask' on all projects
allprojects {
    val allDeps by tasks.registering(DependencyReportTask::class) {}
}
```

## Notes about domain Objects

- [Main api package](https://docs.gradle.org/current/javadoc/org/gradle/api/package-summary.html) contains critical interfaces for build cycles

- [Gradle](https://docs.gradle.org/current/javadoc/org/gradle/api/invocation/Gradle.html) object represents invocation of the build.

- [Project](https://docs.gradle.org/current/javadoc/org/gradle/api/Project.html) domain object represents the main entry point of a build. `project` n<-->1 `gradle`.

- [Task](https://docs.gradle.org/current/javadoc/org/gradle/api/Task.html) represents unit of work with potential dependencies. `task` n<-->1 `project`.

- Action is actual work  performed during execution phase. `action` n<-->0 `task`. Examples are `doLast` and `doFirst` actions.

- Plugin provides reusable logic for a project. It configures domain objects as necessary and has access to them by name or type.

```kotlin
// Example of retrieving gradle version used
project.gradle.gradleVersion
```

