@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
  alias(libs.plugins.dependencyAnalysis)
  alias(libs.plugins.dependencyGuard)
  alias(libs.plugins.dokka) apply false
  alias(libs.plugins.kotlin) apply false
  alias(libs.plugins.publish) apply false
}

dependencyGuard {
  configuration("classpath")
}

tasks.wrapper {
  version = libs.versions.gradle.get()
  distributionType = Wrapper.DistributionType.ALL
}
