plugins {
  alias(libs.plugins.detekt) apply false
  alias(libs.plugins.dokka) apply false
  alias(libs.plugins.kotlin) apply false
  alias(libs.plugins.publish) apply false
  alias(libs.plugins.dependencyAnalysis)
  alias(libs.plugins.dependencyGuard)
  alias(libs.plugins.publishReport)
}

dependencyGuard {
  configuration("classpath")
}

dependencyAnalysis {
  issues {
    all {
      onAny { severity("fail") }
    }
  }
}
