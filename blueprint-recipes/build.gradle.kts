plugins {
  id("convention-kotlin")
  id("convention-publish")
}

dependencies {
  compileOnly(gradleApi())
  compileOnly(gradleKotlinDsl())
  compileOnly(libs.plugin.agp)
  compileOnly(libs.plugin.detekt)
  compileOnly(libs.plugin.kotlin)
  compileOnly(libs.plugin.kover)
  compileOnly(libs.plugin.ktlint)
  compileOnly(libs.plugin.spotless)

  api(projects.blueprintCore)

  testImplementation(gradleTestKit())
  testImplementation(libs.test.junit)
  testImplementation(libs.test.kotlin.common)
  testImplementation(libs.test.kotlin.junit)
}
