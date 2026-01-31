# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Blueprint is a Gradle library (published to Maven Central as `dev.jonpoulton.blueprint:blueprint-core`) that provides type-safe Kotlin DSL extensions and utilities for Gradle build scripts. The library focuses on improving developer experience through convenience functions, type safety, and support for Gradle's configuration cache.

## Commands

### Build & Verification
```bash
./gradlew build              # Build and test the project
./gradlew check              # Run all verification tasks (tests, detekt)
./gradlew test               # Run tests only
./gradlew clean              # Clean build directory
```

### Code Quality
```bash
./gradlew detektCheck        # Run static code analysis
scripts/ktlintCheck.sh       # Run ktlint checks (requires ktlint installed)
scripts/ktlintFormat.sh      # Format code with ktlint
```

### Publishing
```bash
./gradlew publishToMavenLocal                 # Publish to local Maven repository for testing
./gradlew publishAndReleaseToMavenCentral     # Publish to Maven Central (requires credentials)
```

### Dependency Management
```bash
scripts/dependencyUpdates.sh  # Check for available dependency updates
./gradlew dependencyGuard     # Verify dependency checksums
```

## Architecture

### Module Structure

The project uses a multi-module Gradle setup with composite builds:

- **blueprint-core**: The main library module containing all public APIs and utilities
- **build-logic**: A composite build that defines the `blueprint.convention` Gradle plugin used by blueprint-core itself

The `build-logic` module is included via `includeBuild()` in `settings.gradle.kts`, making it a separate build that provides plugins to the main build. This pattern allows the convention plugin to configure its own build while being used by other modules.

### Core Abstractions (blueprint-core/src/main/kotlin/blueprint/core/)

The library provides nine main utility files:

1. **Delegates.kt**: Kotlin property delegates for Gradle's `NamedDomainObjectCollection` and `NamedDomainObjectProvider`. Enables syntax like `val myTask: SomeTask by tasks`.

2. **TypedProperties.kt**: Type-safe property accessors via `ProviderFactory`:
   - Primitive types: `intProperty()`, `floatProperty()`, `boolProperty()`, `doubleProperty()`
   - Collections: `stringListProperty()` (parses comma-delimited strings)
   - All return `Provider<T>` for lazy evaluation

3. **VersionCatalogs.kt**: Extensions for accessing version catalogs:
   - `Project.libs` property for accessing the "libs" catalog
   - Operator overloading for catalog access: `libs["alias"]`
   - `VersionCatalog.version()` for version constraints

4. **LocalProperties.kt**: Configuration-cache-compatible `local.properties` access using custom `ValueSource` implementations. Provides both `Project.localProperties()` and `Settings.localProperties()`.

5. **SystemProperties.kt**: JVM system property utilities, including `ProviderFactory.isIntellijSyncing` for detecting IDE sync state.

6. **Git.kt**: Git information providers using command execution:
   - `gitVersionHash()`: Short commit hash (8 chars)
   - `gitVersionCode()`: Unix timestamp of commit
   - `gitVersionDate()`: Formatted date (YYYY.MM.DD)

7. **Plugins.kt**: `PluginContainer.withAnyId()` for applying actions to multiple plugins by ID.

8. **Ksp.kt**: `Project.kspAllConfigs()` for adding dependencies to all KSP configurations.

9. **Multiplatform.kt**: Kotlin Multiplatform DSL helpers for source set dependencies (`commonMainDependencies()`, `jvmMainDependencies()`, etc.).

### Convention Plugin (build-logic/src/main/kotlin/Convention.kt)

The convention plugin demonstrates best practices for Gradle plugin development. It applies and configures:

- **Kotlin JVM** with explicit API mode and SAM conversions
- **Testing** with enhanced logging (shows passed/skipped/failed tests, full exception format)
- **Detekt** with custom config from `/config/detekt.yml`
- **Dokka** for Javadoc generation
- **Maven Publish** for artifact publication
- **IDEA plugin** with automatic source/javadoc downloads

The plugin uses modular private functions for each configuration concern and lazy task configuration.

### Key Design Patterns

1. **Provider Pattern**: All properties return `Provider<T>` for lazy evaluation and configuration cache support.

2. **ValueSource Pattern**: Custom `ValueSource` implementations (e.g., for Git commands, local properties) ensure configuration cache compatibility.

3. **Extension Functions**: The library extends Gradle APIs (Project, ProviderFactory, VersionCatalog, etc.) with Kotlin extension functions rather than custom DSLs.

4. **Type Safety**: Explicit API mode (`-Xexplicit-api=strict`) enforced for all public APIs.

## Development Guidelines

### Kotlin Compilation

- Target: Java 17 (defined in `gradle.properties` as `blueprint.javaVersion`)
- Explicit API mode is enforced (all public APIs must have explicit visibility and return types)
- SAM conversions use class generation (`-Xsam-conversions=class`)
- Kotlin stdlib default dependency is disabled (`kotlin.stdlib.default.dependency=false`)

### Gradle Configuration

The project uses these Gradle feature previews:
- `STABLE_CONFIGURATION_CACHE`: Configuration cache for faster builds
- `TYPESAFE_PROJECT_ACCESSORS`: Type-safe project dependency accessors

Configuration from `gradle.properties`:
- Configuration cache: enabled and parallel
- Build cache: enabled
- Parallel execution: disabled (`org.gradle.parallel=false`)
- JVM args: `-Xmx4096M -Dfile.encoding=UTF-8 -XX:+UseParallelGC`

### Code Quality Standards

All code must pass:
- **Detekt**: Static analysis with config at `/config/detekt.yml`
- **ktlint**: Kotlin linting via scripts

The `check` task runs all verifications including tests, detekt.

### Publishing Workflow

Version is defined in `gradle.properties` as `VERSION_NAME=2.0.0-SNAPSHOT`. For releases:

1. Update `VERSION_NAME` to release version (e.g., `2.0.0`)
2. Create a matching git tag (e.g., `v2.0.0`)
3. The publish workflow verifies tag matches version and publishes to Maven Central
4. Artifacts are signed with GPG (configured via `RELEASE_SIGNING_ENABLED=true`)

### CI/CD

The PR workflow (`.github/workflows/pr.yml`) runs:
1. Gitleaks secret scanning
2. ktlint checks
3. `./gradlew check` (tests, detekt)
4. Publishes test reports and uploads build artifacts

Java 21 is used in CI (via Zulu distribution) despite the library targeting Java 17.

### Adding New Utilities

When adding new utility functions to `blueprint-core`:

1. Create or modify a file in `blueprint-core/src/main/kotlin/blueprint/core/`
2. Use extension functions on existing Gradle types (Project, ProviderFactory, etc.)
3. Return `Provider<T>` for values that should be lazily evaluated
4. Use `ValueSource` for operations that need configuration cache support (e.g., executing external commands)
5. Add explicit visibility modifiers and return types (explicit API mode)
6. The library compiles against `gradleApi()` and `kotlin("gradle-plugin")` as `compileOnly` dependencies

### Dependency Guard

The project uses dependency-guard to track and validate dependencies. Checksums are maintained for:
- Root classpath: `dependencyGuard { configuration("classpath") }`
- blueprint-core: compileClasspath and runtimeClasspath

Run `./gradlew dependencyGuard` after dependency changes to update checksums.
