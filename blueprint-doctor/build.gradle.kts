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
  api(libs.plugin.doctor)
}

gradlePlugin {
  plugins {
    create("blueprint-doctor") {
      id = "dev.jonpoulton.blueprint.doctor"
      displayName = "Doctor Blueprint"
      description = "Gradle plugin to apply and configure Doctor"
      implementationClass = "blueprint.doctor.DoctorBlueprintPlugin"
    }
  }
}
