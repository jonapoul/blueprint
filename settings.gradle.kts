@file:Suppress("UnstableApiUsage")

rootProject.name = "blueprint"

pluginManagement {
  includeBuild("build-logic")
  repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
  id("com.gradle.develocity") version "4.3.2"
}

develocity {
  buildScan.publishing.onlyIf { false }
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

include(
  ":blueprint-core",
  ":blueprint-test-assertk",
  ":blueprint-test-plugin",
  ":blueprint-test-runtime",
)
