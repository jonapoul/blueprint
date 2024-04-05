@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
  id("convention-kotlin")
  alias(libs.plugins.dokka)
  alias(libs.plugins.publish)
}

dependencies {
  compileOnly(gradleApi())
  compileOnly(gradleKotlinDsl())

  api(libs.plugin.turtle)

  testImplementation(gradleTestKit())
  testImplementation(libs.test.junit)
  testImplementation(libs.test.kotlin.common)
  testImplementation(libs.test.kotlin.junit)
  testImplementation(libs.test.testParameterInjector)
}

publishing {
  repositories {
    mavenLocal()
  }
}

val blueprintVersion = properties["VERSION_NAME"]?.toString() ?: error("No version")
tasks.withType<Test> {
  systemProperty("blueprintVersion", blueprintVersion)
  systemProperty("kotlinVersion", libs.versions.kotlin.get())
  systemProperty("line.separator", "\n")
  dependsOn("publishToMavenLocal")
}
