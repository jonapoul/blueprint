import blueprint.gradle.plugin

plugins {
  id("blueprint.convention")
}

dependencies {
  compileOnly(gradleApi())
  compileOnly(gradleKotlinDsl())
  compileOnly(libs.plugin.agp)
  compileOnly(libs.plugin.kotlin)
  compileOnly(libs.plugin.kover)
  compileOnly(libs.plugin.powerAssert)
  compileOnly(plugin(libs.plugins.androidCacheFix))
  compileOnly(plugin(libs.plugins.compose))
  compileOnly(plugin(libs.plugins.dependencySort))
  compileOnly(plugin(libs.plugins.detekt))
  compileOnly(plugin(libs.plugins.ktlint))
  compileOnly(plugin(libs.plugins.licensee))
  compileOnly(plugin(libs.plugins.spotless))
  implementation(projects.blueprintCore)
  testImplementation(gradleTestKit())
  testImplementation(libs.test.junit)
  testImplementation(libs.test.kotlin.common)
  testImplementation(libs.test.kotlin.junit)
}
