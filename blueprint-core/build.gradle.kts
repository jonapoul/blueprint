@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
  id("convention-kotlin")
  alias(libs.plugins.buildConfig)
  alias(libs.plugins.dokka)
  alias(libs.plugins.publish)
}

dependencies {
  compileOnly(gradleApi())
  compileOnly(gradleKotlinDsl())

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

buildConfig {
  packageName("blueprint.core")
  useKotlinOutput { internalVisibility = true }

  fun String.stringField(value: String) = buildConfigField(type = "String", name = this, value = "\"$value\"")
  "VERSION_ALAKAZAM".stringField(libs.versions.alakazam.get())
  "VERSION_ANDROIDX_ARCH".stringField(libs.versions.test.androidx.arch.get())
  "VERSION_ANDROIDX_COREKTX".stringField(libs.versions.test.androidx.coreKtx.get())
  "VERSION_ANDROIDX_JUNIT".stringField(libs.versions.test.androidx.junit.get())
  "VERSION_ANDROIDX_RULES".stringField(libs.versions.test.androidx.rules.get())
  "VERSION_ANDROIDX_RUNNER".stringField(libs.versions.test.androidx.runner.get())
  "VERSION_COMPOSE_BOM".stringField(libs.versions.androidx.compose.bom.get())
  "VERSION_COMPOSE_COMPILER".stringField(libs.versions.androidx.compose.compiler.get())
  "VERSION_COROUTINES".stringField(libs.versions.kotlinx.coroutines.get())
  "VERSION_DAGGER".stringField(libs.versions.dagger.get())
  "VERSION_DESUGARING".stringField(libs.versions.android.desugaring.get())
  "VERSION_JUNIT".stringField(libs.versions.test.junit.get())
  "VERSION_KOTLIN".stringField(libs.versions.kotlin.get())
  "VERSION_MOCKK".stringField(libs.versions.test.mockk.get())
  "VERSION_ROBOLECTRIC".stringField(libs.versions.test.robolectric.get())
  "VERSION_TURBINE".stringField(libs.versions.test.turbine.get())
}
