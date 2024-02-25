@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
  id("convention-kotlin")
  alias(libs.plugins.buildConfig)
  alias(libs.plugins.dokka)
  alias(libs.plugins.publish)
  `java-gradle-plugin`
}

dependencies {
  compileOnly(gradleApi())
  api(projects.blueprintCore)
  api(libs.plugin.kover)
}

gradlePlugin {
  plugins {
    create("blueprint-kover") {
      id = "dev.jonpoulton.blueprint.kover"
      displayName = "Kover Blueprint"
      description = "Gradle plugin to apply and configure Kover"
      implementationClass = "blueprint.kover.KoverBlueprintPlugin"
    }
  }
}

buildConfig {
  packageName("blueprint.kover")
  useKotlinOutput { internalVisibility = true }

  fun String.stringField(value: String) = buildConfigField(type = "String", name = this, value = "\"$value\"")
  "VERSION_ALAKAZAM".stringField(libs.versions.alakazam.get())
  "VERSION_ANDROIDX_ARCH".stringField(libs.versions.test.androidx.arch.get())
  "VERSION_ANDROIDX_COREKTX".stringField(libs.versions.test.androidx.coreKtx.get())
  "VERSION_ANDROIDX_JUNIT".stringField(libs.versions.test.androidx.junit.get())
  "VERSION_ANDROIDX_RULES".stringField(libs.versions.test.androidx.rules.get())
  "VERSION_ANDROIDX_RUNNER".stringField(libs.versions.test.androidx.runner.get())
  "VERSION_COROUTINES".stringField(libs.versions.kotlinx.coroutines.get())
  "VERSION_JUNIT".stringField(libs.versions.test.junit.get())
  "VERSION_KOTLIN".stringField(libs.versions.kotlin.get())
  "VERSION_MOCKK".stringField(libs.versions.test.mockk.get())
  "VERSION_ROBOLECTRIC".stringField(libs.versions.test.robolectric.get())
  "VERSION_TURBINE".stringField(libs.versions.test.turbine.get())
}
