@file:Suppress("UnstableApiUsage")

rootProject.name = "blueprint"

pluginManagement {
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
    mavenLocal()
  }
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

includeBuild("build-logic")

include(":blueprint-android")
include(":blueprint-core")
include(":blueprint-dependencyAnalysis")
include(":blueprint-dependencyGuard")
include(":blueprint-detekt")
include(":blueprint-doctor")
include(":blueprint-git")
include(":blueprint-kotlin")
include(":blueprint-ktlint")
include(":blueprint-licensee")
include(":blueprint-publish")
include(":blueprint-spotless")
include(":blueprint-versions")
