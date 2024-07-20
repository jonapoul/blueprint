plugins {
  id("convention-kotlin")
  id("convention-publish")
}

dependencies {
  compileOnly(gradleApi())
  compileOnly(gradleKotlinDsl())
  compileOnly(libs.plugin.agp)
  compileOnly(libs.plugin.kotlin)

  api(libs.plugin.detekt)
  api(libs.plugin.kover)
  api(libs.plugin.ktlint)
  api(libs.plugin.spotless)
  api(projects.blueprintCore)

  testImplementation(gradleTestKit())
  testImplementation(libs.test.junit)
  testImplementation(libs.test.kotlin.common)
  testImplementation(libs.test.kotlin.junit)
}
