@file:Suppress("UnstableApiUsage")

pluginManagement {
  repositories {
    maven(url = "file://${settingsDir.absolutePath}/../../../../../build/mavenTest")
    mavenCentral()
    google()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositories {
    mavenCentral()
    google()
  }
}

include(":submodule")
