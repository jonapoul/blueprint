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
  api(libs.plugin.spotless)
}

gradlePlugin {
  plugins {
    create("blueprint-spotless") {
      id = "dev.jonpoulton.blueprint.spotless"
      displayName = "Spotless Blueprint"
      description = "Gradle plugin to apply and configure Spotless"
      implementationClass = "blueprint.spotless.SpotlessBlueprintPlugin"
    }
  }
}
