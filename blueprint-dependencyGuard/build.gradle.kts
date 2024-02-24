@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
  id("convention-kotlin")
  alias(libs.plugins.dokka)
  alias(libs.plugins.publish)
  `java-gradle-plugin`
}

dependencies {
  compileOnly(gradleApi())
  compileOnly(libs.plugin.kotlin)
  api(projects.blueprintCore)
  api(libs.plugin.dependencyGuard)
}

gradlePlugin {
  plugins {
    create("blueprint-guard") {
      id = "dev.jonpoulton.blueprint.guard"
      displayName = "Dependency Guard Blueprint"
      description = "Gradle plugin to apply and configure Dependency Guard "
      implementationClass = "blueprint.guard.DependencyGuardBlueprintPlugin"
    }
  }
}
