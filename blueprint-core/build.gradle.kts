plugins {
  alias(libs.plugins.convention.kotlin)
  alias(libs.plugins.convention.publish)
}

dependencies {
  compileOnly(gradleApi())
  compileOnly(gradleKotlinDsl())
  compileOnly(libs.plugin.agp)

  api(libs.plugin.turtle)

  testImplementation(gradleTestKit())
  testImplementation(libs.test.junit)
  testImplementation(libs.test.kotlin.common)
  testImplementation(libs.test.kotlin.junit)
  testImplementation(libs.test.testParameterInjector)
}

val blueprintVersion = properties["VERSION_NAME"]?.toString() ?: error("No version")
tasks.withType<Test> {
  systemProperty("blueprintVersion", blueprintVersion)
  systemProperty("kotlinVersion", libs.versions.kotlin.get())
  systemProperty("line.separator", "\n")
  dependsOn("publishToMavenLocal")
}
