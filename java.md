# Java

## JVM Monitoring

VM limits (for Java services)

- JMX HeapMemoryAfterGCUse - memory usage within the JVM allocation. Using the AFTER garbage collection metric is important to get an accurate reading. It is important to monitor heap usage because Java services that run out of heap space spend a lot of time garbage collecting. This causes increases in service latency or makes the service stop entirely.
- JMX FileDescriptorUse - includes network sockets because the socket implementation uses a file descriptor. If the service runs out of file descriptors, it cannot write additional log files, and you may lose logs and metrics for the duration of the event. The service will also be unable to open any additional sockets, which will manifest as connection failures visible to customers.
- Snitch PercentSpaceInUse - Logging and metrics implementations write to disk, so you will lose logs and metrics if the service runs out of disk space. Unless you have tried it and caught all of the exceptions, running out of disk space will likely also cause IO or Monitoring exceptions to bubble up as service errors, which customers may see.
- Snitch CPU Utilization - Many services have CPU as the limiting factor in their scaling. High CPU utilization will eventually result in additional service latency, as the operating system will not be able to schedule all processes to run immediately. High CPU utilization can be mitigated by reducing load or adding hardware.
- Tools to keep in mind are JFR, JCMD & VisualVM

### Monitoring & Analyzing

Tools

- [VisualVM](https://visualvm.github.io/) (jvisualvm.exe) for data visualization
- JConsole (jconsole.exe)
- Java Mission Control (jmc.exe) for data visualization.
- Diagnostic Command Tool (jcmd.exe)
- jps - which is useful for getting the process id of the app you want to inspect with the other tools
- jmap histogram

#### JFR

JFR, jcmd and JMC — form a complete suite for collecting low-level runtime information of a running Java program. Two ways to use JFR

- when starting a Java application
- passing diagnostic commands of the jcmd tool when a Java application is already running

JFR Events

- an instant event is logged immediately once it occurs
- a duration event is logged if its duration succeeds a specified threshold
- a sample event is used to sample the system activity

JFR Dataflow

JFR saves data about the events in a single output file, flight.jfr.

Commands

```powershell
# on startup
java -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:StartFlightRecording=duration=200s,filename=flight.jfr path-to-class-file
# Another example for on startup recording
java -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:StartFlightRecording=duration=200s,filename=flight.jfr -cp ./out/ com.baeldung.flightrecorder.FlightRecorder

# start an application then get PID then start registering the events by using the jcmd tool
java -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -cp ./out/ com.baeldung.Main
jps
jcmd 1234 JFR.start duration=100s filename=flight.jfr

# Heap dump

# live: if set, it only prints objects which have active references and discards the ones that are ready to be garbage collected. This parameter is optional.
# format=b: specifies that the dump file will be in binary format. If not set, the result is the same.
# file: the file where the dump will be written to
# pid: id of the Java process
jps
jmap -dump:[live],format=b,file=<file-path> <pid>
jmap -dump:live,format=b,file=/tmp/dump.hprof 12587

jps
jcmd <pid> GC.heap_dump <file-path>
jcmd 12587 GC.heap_dump /tmp/dump.hprof

# On java.lang.OutOfMemoryError error
java -XX:+HeapDumpOnOutOfMemoryError
java -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=<file-or-dir-path>

# Thread Dump

# -F option forces a thread dump; handy to use when jstack pid doesn’t respond (the process is hung)
# -l option instructs the utility to look for ownable synchronizers in the heap and locks
# -m option prints native stack frames (C & C++) in addition to the Java stack frames
jstack [-F] [-l] [-m] <pid>
jstack 17264 > /tmp/threaddump.txt

jcmd 17264 Thread.print
```

#### jcmd

Once the application is running, we use its process id in order to execute various commands `jcmd <pid|MainClass> <command> [parameters]`

- JFR.start – starts a new JFR recording
- JFR.check – checks running JFR recording(s)
- JFR.stop – stops a specific JFR recording
- JFR.dump – copies contents of a JFR recording to file

Each command has a series of parameters. For example, the JFR.start command has the following parameters:

- name – the name of the recording; it serves to be able to reference this recording later with other commands
- delay – dimensional parameter for a time delay of recording start, the default value is 0s
- duration – dimensional parameter for a time interval of the duration of the recording; the default value is 0s, which means unlimited
- filename – the name of a file that contains the collected data
- maxage – dimensional parameter for the maximum age of collected data; the default value is 0s, which means unlimited
- maxsize – the maximum size of buffers for collected data in bytes; the default value is 0, which means no max size

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

#### Build Performance

- Use build cache extension - https://maven.apache.org/extensions/index.html

- Measuring without cache

```sh
# https://gradle.com/blog/five-ways-to-speed-up-your-apache-maven-builds
# Add the extension to the project
mvn com.gradle:develocity-maven-extension:1.22.2:init

# Disable build caching
# It will be re-enabled at the end of the post!
export MAVEN_OPTS="-Ddevelocity.cache.local.enabled=false"

# Run a build
mvn clean install
```

- Enable parallel execution using Junit 5

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-surefire-plugin</artifactId>
  <configuration>
    <properties>
      <!-- JUnit 5 specific configuration -->
      <!-- Also supported for Failsafe plugin -->
      <!-- https://junit.org/junit5/docs/snapshot/user-guide/#writing-tests-parallel-execution-config -->
      <configurationParameters>
        junit.jupiter.execution.parallel.enabled = true
        junit.jupiter.execution.parallel.mode.default = concurrent
      </configurationParameters>
    </properties>
  </configuration>
</plugin>
```

- Parallel builds should be enabled by creating a `.mvn/maven.config` file and add `--threads=1.5C` or  `-T=1.5C`.

- Additional option is to customize clean phase

```xml
<properties>
    <timestamp>${maven.build.timestamp}</timestamp>
    <maven.build.timestamp.format>yyyy-MM-dd-HH-mm</maven.build.timestamp.format>
    <trashdir>trash/target-${maven.build.timestamp}</trashdir>
</properties>

    <profile>
        <id>quickclean</id>
        <build>
            <plugins>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-antrun-plugin</artifactId>
                    <executions>
                        <execution>
                            <id>rename_target</id>
                            <phase>pre-clean</phase>
                            <goals>
                                <goal>run</goal>
                            </goals>
                            <configuration>
                                <tasks>
                                    <move todir="${trashdir}" failonerror="false">
                                        <fileset dir="target/"/>
                                    </move>
                                </tasks>
                            </configuration>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>
    </profile>
    <profile>
        <id>trashclean</id>
        <build>
            <plugins>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-antrun-plugin</artifactId>
                    <executions>
                        <execution>
                            <id>clean_trash</id>
                            <phase>clean</phase>
                            <goals>
                                <goal>run</goal>
                            </goals>
                            <configuration>
                                <tasks>
                                    <delete dir="trash/" failonerror="false"/>
                                </tasks>
                            </configuration>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>
    </profile>
```

#### Examples

`mvn help:describe -Dplugin=archetype` - To get help about a plugin which is `archetype` in this case

`mvn help:effective-pom -Doutput=effective-pom.xml` - Show the merge of real final effective POM for your project. Look at the default configurations of the super POM. You will find default phase and plugin bindings. `output` option to write the output in file instead of the console.

`mvn install` - Here `install` is a phase in a life cycle which will invoke several plugins

`mvn dependency:tree -Dincludes=antlr` - Search for dependency using dependency plugin

`mvn dependency:resolve` - Download dependency. Use -U for forcing the update

`mvn clean install` - Resolve and organize updating references

`mvn clean package -Dmaven.test.skip=true` - Compiles all except tests. `=true` could be removed.

`mvn clean package -DskipTests` - Compiles everything, includes tests but does NOT execute unit tests.

`mvn dependency:sources` - Download sources jars javadoc

`mvn dependency:resolve -Dclassifier=javadoc` - Download javadoc for all dependencies

`mvn dependency:resolve` - If you want to only download dependencies without doing anything else

`mvn dependency:get -Dartifact=groupId:artifactId:version` - to download a single dependency

`mvn package --projects schedule-job -am` - Package sub-module and make other modules as well. To exclude projects use `--projects !schedule-job,!beans`

`mvn compile dependency:tree` - Instead of looking at local repository for submodule, update  the current one

`mvn archetype:generate -Dfilter=maven-archetype=quickstart` - Use [archetype](https://maven.apache.org/archetype/maven-archetype-plugin/) plugin to create new project filtering specific archetype

`mvn archetype:generate -DgroupId=com.example -DartifactId=sampleApp -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false` - Notice the parameter [`-DarchetypeArtifactId=maven-archetype-quickstart`](https://maven.apache.org/archetypes/maven-archetype-quickstart/dependency-info.html) which identifies already existing archetype so we create a sample built on it.

##### Scripting with dependencies

```powershell
# Example of downloading and copying package to local folder
mvn dependency:get -D remoteRepositories=repo.maven.apache.org -D artifact=org.apache.ant:ant:1.8.1
mvn dependency:copy -D artifact=commons-io:commons-io:2.11.0 -D outputDirectory=./lib

# Execute JShell script with all jars in the lib folder in classpath
jshell --class-path lib/* script.jsh
```

#### Repository

- search.maven.org
- Local repository in ~/.m2

#### Archiva

Archiva could be used for deployment directly in the folder and scan the jars with its POM. This also could be done from command line like this

```powershell
mvn org.apache.maven.plugins:maven-deploy-plugin:3.0.0-M1:deploy-file -Durl=http://10.0.10.107:8088/repository/arrow/ -DrepositoryId=arrow -Dfile=dfc.jar -Dfiles=dfc.jar -Dclassifiers=debug -Dtypes=jar -DgroupId=com.documentum -DartifactId=dfc -Dversion=16.4.0000.0185 -Dpackaging=jar
```

#### References for Maven

[Plugins](https://maven.apache.org/plugins/index.html)  
[Lifecycles Reference](https://maven.apache.org/maven-core/lifecycles.html) & [Introduction to Lifecycle Reference](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#Lifecycle_Reference)  
[POM.xml Reference](https://maven.apache.org/pom.html)  
[Settings Reference](https://maven.apache.org/settings.html)  
[Configure Plugins](https://maven.apache.org/guides/mini/guide-configuring-plugins.html)  
[Dependency Management](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html)  
[Full Guides & References Links](https://maven.apache.org/guides/index.html)

### Gradle

#### References for Gradle

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

#### Examples for Gradle

- Check [starter project](kotlin-starter-gradle) for multi-module kotlin/jvm sample

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

## Spring Security

- `DelegatingFilterProxy` allows bridging between the Servlet container’s lifecycle and Spring’s `ApplicationContext`.
- Spring Security’s Servlet support is contained within `FilterChainProxy`. `FilterChainProxy` is a special Filter provided by Spring Security that allows delegating to many Filter instances through `SecurityFilterChain`. Since `FilterChainProxy` is a Bean, it is typically wrapped in a `DelegatingFilterProxy`.
- `SecurityFilterChain` is used by `FilterChainProxy` to determine which Spring Security Filter instances should be invoked for the current request.
- If you try to troubleshoot Spring Security’s Servlet support, adding a debug point in `FilterChainProxy` is a great place to start.

### SecurityFilterChain

The Security Filters in [SecurityFilterChain](https://docs.spring.io/spring-security/reference/servlet/architecture.html#servlet-securityfilterchain) are typically Beans, but they are registered with `FilterChainProxy` instead of `DelegatingFilterProxy`. `FilterChainProxy` provides a number of advantages to registering directly with the Servlet container or `DelegatingFilterProxy`. First, it provides a starting point for all of Spring Security’s Servlet support. For that reason, if you try to troubleshoot Spring Security’s Servlet support, ***adding a debug point*** in `FilterChainProxy` is a great place to start.

Second, since `FilterChainProxy` is central to Spring Security usage, it can perform tasks that are not viewed as optional. For example, it clears out the `SecurityContext` to avoid memory leaks. It also applies Spring Security’s `HttpFirewall` to protect applications against certain types of attacks.

In addition, it provides more flexibility in determining when a `SecurityFilterChain` should be invoked. In a Servlet container, Filter instances are invoked based upon the URL alone. However, `FilterChainProxy` can determine invocation based upon anything in the `HttpServletRequest` by using the `RequestMatcher` interface.

## Java Platform Module System / JPMS / Modules

### Descriptor aspects

- Name – the name of our module
- Dependencies – a list of other modules that this module depends on
- Public Packages – a list of all packages we want accessible from outside the module
- Services Offered – we can provide service implementations that can be consumed by other modules
- Services Consumed – allows the current module to be a consumer of a service
- Reflection Permissions – explicitly allows other classes to use reflection to access the private members of a package

### Module Types

- System Modules – These are the modules listed when we run  `java --list-modules` and they include the Java SE and JDK modules.
- Application Modules – What we define in module-info.class file included in the assembled JAR.
- Automatic Modules – Unofficial modules derived from the name of the JAR and have full read access to every other module loaded by the path.
- Unnamed Module – When a class or JAR is loaded onto the classpath, but not the module path, it’s automatically added to the unnamed module. It’s a catch-all module to maintain backward compatibility with previously-written Java code.

---

## General References

Monitoring & Analyzing

- [JMX Guide](https://www.baeldung.com/java-management-extensions)
- [Dump Heap Capture](https://www.baeldung.com/java-heap-dump-capture)
- [Thread Dump Capture](https://www.baeldung.com/java-thread-dump)
- [Analyze Thread Dumps](https://www.baeldung.com/java-analyze-thread-dumps)
- [JFR Guide](https://www.baeldung.com/java-flight-recorder-monitoring)
- [JFR Guide](https://www.javatpoint.com/java-flight-recorder)
- [How to use JDK Flight Recorder (JFR)](https://access.redhat.com/solutions/662203)
- [JCMD](https://docs.oracle.com/en/java/javase/11/tools/jcmd.html#GUID-59153599-875E-447D-8D98-0078A5778F05)
- [Java Profilers](https://www.baeldung.com/java-profilers)
