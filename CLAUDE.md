# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Blueprint is a Gradle library (published to Maven Central as `dev.jonpoulton.blueprint:blueprint-core`) providing type-safe Kotlin DSL extensions and utilities for Gradle build scripts, with a focus on configuration-cache support.

## Commands

```bash
./gradlew build              # Build and test
./gradlew check              # All verification (tests, detekt)
./gradlew test               # Tests only
./gradlew compileAll         # Compile everything, no tests
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

A handful of small utility files, each adding extension functions to existing Gradle types. They cover: dependency notation from plugin catalogs, type-safe Gradle property accessors, version catalog access, `local.properties` and `.java-version` reading, JVM system properties, git info, plugin-container helpers, KSP configuration, and KMP source-set helpers. Values are exposed as `Provider<T>` and external commands go through `ValueSource` so everything stays configuration-cache safe.

### Convention Plugin (build-logic/src/main/kotlin/blueprint/gradle/Convention.kt)

Applies and configures the standard setup for every module: Kotlin JVM with explicit API mode, testing, Detekt, Dokka, Maven Publish, and Dependency Guard. Java version comes from the root `.java-version`. Also registers a `compileAll` task.

### Testing Infrastructure

A framework for testing Gradle plugins with TestKit, split across three modules:

- **blueprint-test-runtime**: the core abstractions. `ScenarioTest` is the JUnit 5 base class (temp dirs, Gradle version, declarative project setup). A `FileTree` DSL builds the test project's files and directories, with shortcut helpers for the common ones (`settings.gradle.kts`, `build.gradle.kts`, `gradle.properties`, etc.). `Scenario` wraps a `GradleRunner`.
- **blueprint-test-assertk**: fluent AssertK assertions over build results - whether the build passed or failed, individual task outcomes, and build output. Also runner-tweaking helpers (config cache on/off, extra properties/args) applied before execution.
- **blueprint-test-plugin**: a Gradle plugin that auto-wires the above into any project using `java-gradle-plugin` - test classpath, dependencies, and injecting the right Blueprint version.

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
- **publish.yml**: single workflow gating both snapshot and release publishing behind a shared `check-version` job (reads `VERSION_NAME` via a sparse checkout, verifies tags match it and land on `main`). Pushes to `main` with a `-SNAPSHOT` version publish a snapshot; tag pushes with a non-snapshot version publish a release and create a GitHub release. Both jobs share the `publish` concurrency group so they never run in parallel.

### Adding New Utilities

Add extension functions in `blueprint-core/src/main/kotlin/blueprint/core/`. Return `Provider<T>` for lazily-evaluated values, use `ValueSource` for external commands (config-cache), and add explicit visibility + return types.

### Dependency Guard

Baseline files listing the resolved dependency classpaths are tracked for the root classpath and blueprint-core's compile/runtime classpaths. `./gradlew dependencyGuard` verifies the current classpaths match those baselines (and fails CI on drift); after an intentional dependency change, run `./gradlew dependencyGuardBaseline` to regenerate them and commit the result.

### Writing Tests

Tests for blueprint-core utilities extend `ScenarioTest`, declare a `fileTree` for the test project structure, run the build with `runScenario { }`, and verify behavior with `assertThatTask()` and its fluent assertions. See the existing `*Scenario` tests in `blueprint-core/src/test` for the pattern.

### Build Features

- **Gradle Develocity**: configured, build scans disabled
- **Foojay Resolver**: automatic JDK provisioning via toolchains
- **BuildConfig**: test plugin generates version constants from `VERSION_NAME`
