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
  api(libs.plugin.versions)
}

gradlePlugin {
  plugins {
    create("blueprint-versions") {
      id = "dev.jonpoulton.blueprint.versions"
      displayName = "Versions Blueprint"
      description = "Gradle plugin to apply and configure Versions"
      implementationClass = "blueprint.versions.VersionsBlueprintPlugin"
    }
  }
}
