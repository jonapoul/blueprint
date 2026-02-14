@file:Suppress("UnstableApiUsage")

rootProject.name = "blueprint"

apply(from = "gradle/repositories.gradle.kts")

pluginManagement { includeBuild("build-logic") }

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
  id("com.gradle.develocity") version "4.3.2"
}

develocity { buildScan.publishing.onlyIf { false } }

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

include(
  ":blueprint-core",
  ":blueprint-test-assertk",
  ":blueprint-test-plugin",
  ":blueprint-test-runtime",
)
