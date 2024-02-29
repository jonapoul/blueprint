@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
  id("convention-kotlin")
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
