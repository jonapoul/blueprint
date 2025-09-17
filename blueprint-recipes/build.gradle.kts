plugins {
  alias(libs.plugins.convention.kotlin)
  alias(libs.plugins.convention.publish)
}

dependencies {
  compileOnly(gradleApi())
  compileOnly(gradleKotlinDsl())
  compileOnly(libs.plugin.agp)
  compileOnly(libs.plugin.androidCacheFix)
  compileOnly(libs.plugin.compose)
  compileOnly(libs.plugin.dependencySort)
  compileOnly(libs.plugin.detekt)
  compileOnly(libs.plugin.kotlin)
  compileOnly(libs.plugin.kover)
  compileOnly(libs.plugin.ktlint)
  compileOnly(libs.plugin.licensee)
  compileOnly(libs.plugin.powerAssert)
  compileOnly(libs.plugin.spotless)
  implementation(projects.blueprintCore)
  testImplementation(gradleTestKit())
  testImplementation(libs.test.junit)
  testImplementation(libs.test.kotlin.common)
  testImplementation(libs.test.kotlin.junit)
}
