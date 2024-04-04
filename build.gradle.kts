@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
  alias(libs.plugins.dependencyAnalysis)
  alias(libs.plugins.dependencyGuard)
  alias(libs.plugins.dokka) apply false
  alias(libs.plugins.kotlin) apply false
  alias(libs.plugins.publish)
}

dependencyGuard {
  configuration("classpath")
}

tasks.wrapper {
  version = libs.versions.gradle.get()
  distributionType = Wrapper.DistributionType.ALL
}

mavenPublishing {
  val releaseSigningEnv = System.getenv("ORG_GRADLE_PROJECT_RELEASE_SIGNING_ENABLED")?.toBoolean()
  if (releaseSigningEnv != false) {
    signAllPublications()
  }
}
