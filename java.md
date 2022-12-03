# Java

## JVM Monitoring Tips

VM limits (for Java services)
- JMX HeapMemoryAfterGCUse - memory usage within the JVM allocation. Using the AFTER garbage collection metric is important to get an accurate reading. It is important to monitor heap usage because Java services that run out of heap space spend a lot of time garbage collecting. This causes increases in service latency or makes the service stop entirely.
- JMX FileDescriptorUse - includes network sockets because the socket implementation uses a file descriptor. If the service runs out of file descriptors, it cannot write additional log files, and you may lose logs and metrics for the duration of the event. The service will also be unable to open any additional sockets, which will manifest as connection failures visible to customers.
- Snitch PercentSpaceInUse - Logging and metrics implementations write to disk, so you will lose logs and metrics if the service runs out of disk space. Unless you have tried it and caught all of the exceptions, running out of disk space will likely also cause IO or Monitoring exceptions to bubble up as service errors, which customers may see.
- Snitch CPU Utilization - Many services have CPU as the limiting factor in their scaling. High CPU utilization will eventually result in additional service latency, as the operating system will not be able to schedule all processes to run immediately. High CPU utilization can be mitigated by reducing load or adding hardware.

## JShell

```shell
# Use -v to start in verbose mode
# Disable verbose mode
/set feedback normal
/set feedback verbose

# Start with class path or module path
jshell --class-path f:/classes
/env --class-path f:/classes
/env --module-path f:/modules
jshell --add-modules java.logging
/env --add-modules java.logging

# List declared variables
/vars
# List of imports
/imports
# List declared methods
/methods
# List declared classes
/types
# Exit
/exit
# Reset everything
/reset
# Save snippets to file
/save f:/movie.jshell
# load from file
/open f:/movie.jshell
# Print typed commands
/history
/list
# Execute number one in the list
/1
# Execute range of snippets (1 to 2 then 7)
/1-2 7
# Execute last snippet again
/!
```

## Build Tools

### Maven

##### Examples

`mvn help:describe -Dplugin=archetype` - To get help about a plugin which is `archetype` in this case

`mvn help:effective-pom -Doutput=effective-pom.xml` - Show the merge of real final effective POM for your project. Look at the default configurations of the super POM. You will find default phase and plugin bindings. `output` option to write the output in file instead of the console.

`mvn install` - Here `install` is a phase in a life cycle which will invoke several plugins

`mvn dependency:tree -Dincludes=antlr` - Search for dependency using dependency plugin

`mvn dependency:resolve` - Download dependency. Use -U for forcing the update

`mvn clean install` - Resolve and organize updating references

`mvn dependency:sources` - Download sources jars javadoc

`mvn dependency:resolve -Dclassifier=javadoc` - Download javadoc for all dependencies

`mvn dependency:resolve` - If you want to only download dependencies without doing anything else

`mvn dependency:get -Dartifact=groupId:artifactId:version` - to download a single dependency

`mvn package --projects schedule-job -am` - Package sub-module and make other modules as well. To exclude projects use `--projects !schedule-job,!beans`

`mvn compile dependency:tree` - Instead of looking at local repository for submodule, update  the current one

`mvn archetype:generate -Dfilter=maven-archetype=quickstart` - Use [archetype](https://maven.apache.org/archetype/maven-archetype-plugin/) plugin to create new project filtering specific archetype

`mvn archetype:generate -DgroupId=com.example -DartifactId=sampleApp -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false` - Notice the parameter [`-DarchetypeArtifactId=maven-archetype-quickstart`](https://maven.apache.org/archetypes/maven-archetype-quickstart/dependency-info.html) which identifies already existing archetype so we create a sample built on it.

##### Repository

- search.maven.org
- Local repository in ~/.m2

##### Archiva

Archiva could be used for deployment directly in the folder and scan the jars with its POM. This also could be done from command line like this

```powershell
mvn org.apache.maven.plugins:maven-deploy-plugin:3.0.0-M1:deploy-file -Durl=http://10.0.10.107:8088/repository/arrow/ -DrepositoryId=arrow -Dfile=dfc.jar -Dfiles=dfc.jar -Dclassifiers=debug -Dtypes=jar -DgroupId=com.documentum -DartifactId=dfc -Dversion=16.4.0000.0185 -Dpackaging=jar
```

##### References

[Plugins](https://maven.apache.org/plugins/index.html)  
[Lifecycles Reference](https://maven.apache.org/maven-core/lifecycles.html) & [Introduction to Lifecycle Reference](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#Lifecycle_Reference)  
[POM.xml Reference](https://maven.apache.org/pom.html)  
[Settings Reference](https://maven.apache.org/settings.html)  
[Configure Plugins](https://maven.apache.org/guides/mini/guide-configuring-plugins.html)  
[Dependency Management](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html)  
[Full Guides & References Links](https://maven.apache.org/guides/index.html)

### Gradle

#### References

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

#### Commands

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

#### Notes

- Settings file executed during initialization phase. `settings.gradle.kts` file in the root project of the multi-project hierarchy.
- Build phases: A Gradle build has three distinct phases.
    - **Initialization**: Gradle supports single and multi-project builds. During the initialization phase, Gradle determines which projects are going to take part in the build, and creates a Project instance for each of these projects.
    - **Configuration**: During this phase the project objects are configured. The build scripts of all projects which are part of the build are executed.
    - **Execution**: Gradle determines the subset of the tasks, created and configured during the configuration phase, to be executed. The subset is determined by the task name arguments passed to the gradle command and the current directory. Gradle then executes each of the selected tasks.
- Maven phases to Gradle tasks
- `script` object is created from any `.gradle` script then delegates a domain object depending on the current phase.
  - Initialization:
    - `script` (`settings.gradle`) > delegates to > `settings` > `project`s objects (prepared for execution phase)
    - `script` (`init.gradle`) > delegates to > `gradle`
  - Configuration: script (`build.gradle`) > delegates to > project
  - Execution: script () > delegates > project
- Domain Objects
  - [Main api package](https://docs.gradle.org/current/javadoc/org/gradle/api/package-summary.html) contains critical interfaces for build cycles
  - [Gradle](https://docs.gradle.org/current/javadoc/org/gradle/api/invocation/Gradle.html) object represents invocation of the build. 
  - [Project](https://docs.gradle.org/current/javadoc/org/gradle/api/Project.html) domain object represents the main entry point of a build. `project` n<-->1 `gradle`.
  - [Task](https://docs.gradle.org/current/javadoc/org/gradle/api/Task.html) represents unit of work with potential dependencies. `task` n<-->1 `project`.

    ```kotlin
      // Example of retrieving gradle version used
      project.gradle.gradleVersion
    ```
- Action is actual work  performed during execution phase. `action` n<-->0 `task`. Examples are `doLast` and `doFirst` actions.
- Plugin provides reusable logic for a project. It configures domain objects as necessary and has access to them by name or type.


#### Examples

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

// Makes the myProperty project property available 
// via a myProperty delegated property
// the project property must exist in this case
// otherwise the build will fail when the build script 
// attempts to use the myProperty value
val myProperty: String by project

// Extra properties are available on any 
// object that implements the ExtensionAware interface
// Creates a new extra property called myNewProperty
// in the current context (the project in this case)
// and initializes it with the value "initial value",
// which also determines the property’s type
val myNewProperty by extra("initial value")
```

## Java Log Concept

### Loggers

Loggers are responsible for capturing events (called LogRecords)
and passing them to the appropriate Appender.

Loggers are objects that trigger log events.
Loggers are created and called in the code of your Java application, where they generate events
before passing them to an Appender. A class can have multiple independent Loggers
responding to different events, and you can nest Loggers under other Loggers to create a hierarchy.

Loggers provide several methods for triggering log events. However, before you can log an event, you need
to assign a level. Log levels determine the severity of the log and can be used to filter the event or send
it to a different Appender(for more information on log levels, see the Log Levels section).
The Logger.log() method requires a level in addition to a message.

logger.log(Level.WARNING, “This is a warning!”);

Most logging frameworks provide shorthand methods for logging at a particular level. For example, the following statement produces the same output as the previous statement.

logger.warning(“This is a warning!”);

### Appenders

Appenders (also called Handlers in some logging frameworks) are responsible for recording
log events to a destination. Appenders use Layouts to format events before sending them to an output.

Appenders forward logs from Loggers to an output destination.
During this process, log messages are formatted using a Layout before being
delivered to their final destination. Multiple Appenders can be combined to write log events to multiple
destinations. For instance, a single event can be simultaneously displayed in a console and written to a file.

Note that java.util.logging refers to Appenders as Handlers.

### Layouts

Layouts (also called Formatters in some logging frameworks)
are responsible for converting and formatting the data in a log event.
Layouts determine how the data looks when it appears in a log entry.

Layouts convert the contents of a log entry from one data type into another. Logging frameworks
provide Layouts for plain text, HTML, syslog, XML, JSON, serialized, and other logs.
Note that java.util.logging refers to Layouts as Formatters.
For example, java.util.logging provides two Layouts: the SimpleFormatter and the XMLFormatter.
SimpleFormatter, the default Layout for ConsoleHandlers, outputs plain text log entries

When your application makes a logging call, the Logger records the event in a LogRecord and forwards it
to the appropriate Appender. The Appender then formats the record using a Layout before
sending it a destination such as the console, a file, or another application.
Additionally, you can use one or more Filters to specify which Appenders should be used for which events.
Filters aren’t required, but they give you greater control over the flow of your log messages.

`APPLICATION >> LOGGER (FILTER OPTIONALLY) >> HANDLER (FILTER OPTIONALLY) >> OUTSIDE WORLD`

### Log Levels

SEVERE(HIGHEST LEVEL)
WARNING
INFO
CONFIG
FINE
FINER
FINEST(LOWEST LEVEL)

### More Info

<https://www.loggly.com/ultimate-guide/java-logging-basics>
