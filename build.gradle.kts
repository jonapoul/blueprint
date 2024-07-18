buildscript {
  repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
    maven { url = uri("https://jitpack.io") }
  }

  dependencies {
    classpath(libs.plugin.publish)
  }
}

plugins {
  alias(libs.plugins.dependencyAnalysis)
  alias(libs.plugins.dependencyGuard)
  alias(libs.plugins.dokka) apply false
  alias(libs.plugins.kotlin) apply false
//   alias(libs.plugins.publish) apply false
  alias(libs.plugins.versions)
}

dependencyGuard {
  configuration("classpath")
}

tasks.wrapper {
  version = libs.versions.gradle.get()
  distributionType = Wrapper.DistributionType.ALL
}

tasks.dependencyUpdates {
  rejectVersionIf { !candidate.version.isStable() && currentVersion.isStable() }
}

fun String.isStable(): Boolean = listOf("alpha", "beta", "rc").none { lowercase().contains(it) }
