plugins {
  id("blueprint.convention")
}

dependencies {
  compileOnly(gradleApi())
  compileOnly(gradleKotlinDsl())
  compileOnly(libs.plugin.agp)
  compileOnly(libs.plugin.kotlin)
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
