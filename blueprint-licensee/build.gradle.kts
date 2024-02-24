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
  api(libs.plugin.licensee)
}

gradlePlugin {
  plugins {
    create("blueprint-licensee") {
      id = "dev.jonpoulton.blueprint.licensee"
      displayName = "Licensee Blueprint"
      description = "Gradle plugin to apply and configure Licensee"
      implementationClass = "blueprint.licensee.LicenseeBlueprintPlugin"
    }
  }
}
