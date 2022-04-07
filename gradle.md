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

## Commands

```powershell
# Do not execute tasks but show what would be executed
gradle helloWorld --dry-run

# List all tasks
gradle tasks --all
```

## Examples

```kotlin
// Apply script plugin from external script file
apply(from = "other.gradle.kts")
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

