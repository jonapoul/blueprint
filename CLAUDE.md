# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Blueprint is a Gradle library (published to Maven Central as `dev.jonpoulton.blueprint:blueprint-core`) providing type-safe Kotlin DSL extensions and utilities for Gradle build scripts, with a focus on configuration-cache support.

## Commands

```bash
./gradlew build              # Build and test
./gradlew check              # All verification (tests, detekt)
./gradlew test               # Tests only
./gradlew detektCheck        # Static analysis
scripts/ktfmt.sh check       # Check formatting (add --force for all files)
scripts/ktfmt.sh format      # Format Kotlin files (Google style)
scripts/dependencyUpdates.sh # Check for dependency updates
./gradlew dependencyGuard    # Verify dependency classpath files
./gradlew dependencyGuardBaseline            # Update dependency classpath files
./gradlew publishToMavenLocal                # Publish locally for testing
./gradlew publishAndReleaseToMavenCentral    # Publish to Maven Central (needs credentials)
```

`scripts/profile.sh` benchmarks/profiles the build via [gradle-profiler](https://github.com/gradle/gradle-profiler); run it with no args for available scenarios and help.

## Architecture

### Module Structure

Multi-module Gradle setup with composite builds: `blueprint-core` holds the public library, a set of `blueprint-test-*` modules provide the Gradle TestKit testing framework, and `build-logic` is a composite build supplying the convention plugin that blueprint-core builds itself with.

### Core Abstractions (blueprint-core/src/main/kotlin/blueprint/core/)

Ten utility files, each extending existing Gradle types:

- **Dependencies.kt**: `Provider<PluginDependency>.toDependency()` → `Provider<String>` dependency notation
- **TypedProperties.kt**: Type-safe property accessors (`intProperty()`, `boolProperty()`, `stringListProperty()`, …), all returning `Provider<T>`
- **VersionCatalogs.kt**: `Project.libs`, `libs["alias"]`, `VersionCatalog.version()`
- **LocalProperties.kt**: Config-cache-compatible `local.properties` access (`Project`/`Settings` extensions) via `ValueSource`
- **SystemProperties.kt**: JVM system property utilities, incl. `ProviderFactory.isIntellijSyncing`
- **Git.kt**: Git info providers shared via a `GitInfoService` BuildService wrapping `ValueSource` command execution (`gitVersionHash()`, `gitVersionCode()`, `gitVersionDate()`)
- **Plugins.kt**: `PluginContainer.withAnyId()`
- **Ksp.kt**: `Project.kspAllConfigs()`
- **Multiplatform.kt**: KMP source-set dependency helpers (`commonMainDependencies()`, etc.)
- **JavaVersion.kt**: Config-cache-compatible `.java-version` readers (`javaVersion()`, `jvmTarget()`, …) via `ValueSource`

### Convention Plugin (build-logic/src/main/kotlin/Convention.kt)

Applies and configures Kotlin JVM (explicit API mode, SAM-as-class, ABI validation), testing, Detekt (`/config/detekt.yml`), Dokka, Maven Publish, and Dependency Guard. Java version is read from the root `.java-version` file.

### Testing Infrastructure

The project includes a sophisticated testing framework for Gradle plugin development:

#### blueprint-test-runtime

Provides core testing abstractions:

- **ScenarioTest**: Abstract base class for Gradle TestKit tests using JUnit 5
  - `@TempDir` integration for isolated test directories
  - `gradleVersion` property to specify Gradle version for tests
  - `fileTree` property for declarative project setup
  - `runScenario()` method to execute tests with GradleRunner

- **FileTree DSL**: Declarative DSL for building test project structures
  ```kotlin
  fileTree {
    "settings.gradle.kts"(DEFAULT_REPOSITORIES_KTS)
    "build.gradle.kts"("""
      plugins { id("my.plugin") }
    """.trimIndent())
    "src/main/kotlin" {
      "MyClass.kt"("class MyClass")
    }
  }
  ```
  - Uses operator overloading: `String.invoke(String)` for files, `String.invoke(Builder.() -> Unit)` for directories
  - Automatically handles path separators and directory nesting

- **Scenario**: Interface wrapping GradleRunner with helper methods
  - `runTask(task, *args)`: Executes tasks with `--configuration-cache` by default

#### blueprint-test-assertk

Fluent AssertK extensions for Gradle TestKit assertions:

```kotlin
assertThatTask(":myTask", "-Pkey=value")
  .buildsSuccessfully()
  .taskSucceeded(":myTask")
  .outputContainsLine("expected output")
  .outputDoesNotContain("error")
```

Provides chainable assertions:
- `buildsSuccessfully()` / `failsBuild()`: Execute and verify build result
- `taskSucceeded()`, `taskFailed()`, `taskSkipped()`, `taskUpToDate()`: Verify task outcomes
- `outputContains()`, `outputContainsLine()`, `outputDoesNotContain()`, `outputContainsMatch()`: Verify build output

#### blueprint-test-plugin

Gradle plugin that automates test setup for plugin development:

- Applies to projects using `java-gradle-plugin`
- Registers `testPluginClasspath` configuration
- Automatically adds `blueprint:test-runtime` to `testImplementation`
- Configures `PluginUnderTestMetadata` tasks to include test plugin classpath
- Uses BuildConfig to inject the correct Blueprint version

### Key Design Patterns

- **Provider Pattern**: values return `Provider<T>` for lazy evaluation / config-cache support
- **ValueSource Pattern**: custom `ValueSource` implementations for config-cache-safe external commands
- **Extension Functions**: extend Gradle APIs rather than defining custom DSLs
- **Type Safety**: explicit API mode (`-Xexplicit-api=strict`) on all public APIs

## Development Guidelines

### Build Configuration

- Target Java 21 (root `.java-version`); explicit API mode enforced; SAM-as-class; `kotlin.stdlib.default.dependency=false`
- Feature previews: `STABLE_CONFIGURATION_CACHE`, `TYPESAFE_PROJECT_ACCESSORS`
- `gradle.properties`: configuration cache + build cache enabled, `org.gradle.parallel=false`
- blueprint-core compiles against `gradleApi()` and `kotlin("gradle-plugin")` as `compileOnly`

### Code Quality Standards

All code must pass Detekt (`/config/detekt.yml`) and ktfmt (Google style, via `scripts/ktfmt.sh`). The `check` task runs all verifications.

### Publishing Workflow

Version lives in `gradle.properties` as `VERSION_NAME` (currently `2.3.0`). For a release: bump `VERSION_NAME`, push a matching `v*` git tag; the publish workflow verifies the tag matches and publishes signed artifacts to Maven Central.

### CI/CD

- **pr.yml**: main PR validation, runs on `pull_request`/`merge_group` as many parallel jobs (ktfmt, detekt, tests, dependency-guard, betterleaks secret scanning, actionlint, shellcheck, gradle-wrapper, check-build-logic, …). Builds are set up via the local composite actions `./.github/actions/setup-java` (Zulu JDK 21) and `./.github/actions/setup-gradle` (wraps `gradle/actions/setup-gradle@v6`, read-only cache off `main`).
- **publish-snapshot.yml**: publishes `-SNAPSHOT` versions on push to `main`.
- **publish-release.yml**: verifies the git tag matches `VERSION_NAME`, then publishes and creates a GitHub release.

### Adding New Utilities

Add extension functions in `blueprint-core/src/main/kotlin/blueprint/core/`. Return `Provider<T>` for lazily-evaluated values, use `ValueSource` for external commands (config-cache), and add explicit visibility + return types.

### Dependency Guard

Baseline files listing the resolved dependency classpaths are tracked for the root classpath and blueprint-core's compile/runtime classpaths. `./gradlew dependencyGuard` verifies the current classpaths match those baselines (and fails CI on drift); after an intentional dependency change, run `./gradlew dependencyGuardBaseline` to regenerate them and commit the result.

### Writing Tests

Tests for blueprint-core utilities follow this pattern:

1. Extend `ScenarioTest` and specify `gradleVersion`
2. Define a `fileTree` with test project structure (typically includes `settings.gradle.kts`, `build.gradle.kts`, and `gradle.properties`)
3. Use `runScenario { }` to execute test logic
4. Use `assertThatTask()` with fluent assertions to verify behavior

Example:
```kotlin
internal class MyUtilityScenario : ScenarioTest() {
  override val gradleVersion = GRADLE_VERSION

  override val fileTree = fileTree {
    "settings.gradle.kts"(DEFAULT_REPOSITORIES_KTS)
    "build.gradle.kts"("""
      import blueprint.core.*
      plugins { id("dev.jonpoulton.blueprint") }

      tasks.register("myTask") {
        doLast { println("Hello") }
      }
    """.trimIndent())
  }

  @Test
  fun `My test`() = runScenario {
    assertThatTask(":myTask")
      .buildsSuccessfully()
      .taskSucceeded(":myTask")
      .outputContainsLine("Hello")
  }
}
```

### Build Features

- **Gradle Develocity**: configured, build scans disabled
- **Foojay Resolver**: automatic JDK provisioning via toolchains
- **BuildConfig**: test plugin generates version constants from `VERSION_NAME`
