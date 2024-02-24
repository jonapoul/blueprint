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
  api(libs.plugin.detekt)
}

gradlePlugin {
  plugins {
    create("blueprint-detekt") {
      id = "dev.jonpoulton.blueprint.detekt"
      displayName = "Detekt Blueprint"
      description = "Gradle plugin to apply and configure Detekt"
      implementationClass = "blueprint.detekt.DetektBlueprintPlugin"
    }
  }
}
