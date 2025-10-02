plugins {
  alias(libs.plugins.detekt) apply false
  alias(libs.plugins.dokka) apply false
  alias(libs.plugins.kotlin) apply false
  alias(libs.plugins.publish) apply false
  alias(libs.plugins.spotless) apply false
  alias(libs.plugins.publishReport)
}
