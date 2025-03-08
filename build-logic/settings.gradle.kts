@file:Suppress("UnstableApiUsage")

rootProject.name = "build-logic"

dependencyResolutionManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
    maven { url = uri("https://jitpack.io") }
  }

  versionCatalogs {
    create("libs") {
      from(files("../gradle/libs.versions.toml"))
    }
  }
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
