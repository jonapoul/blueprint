plugins {
  alias(libs.plugins.convention.kotlin)
  alias(libs.plugins.convention.publish)
  `java-gradle-plugin`
}

dependencies {
  compileOnly(gradleApi())
  api(projects.blueprintCore)
  api(libs.plugin.dependencyGraph) {
    // this dep brings in 1.9.25, we need 1.8.22
    exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
  }
}

gradlePlugin {
  plugins {
    create("blueprint-diagrams") {
      id = "dev.jonpoulton.blueprint.diagrams"
      displayName = "Diagrams Blueprint"
      description = "Gradle plugin to apply and configure module diagrams"
      implementationClass = "blueprint.diagrams.DiagramsBlueprintPlugin"
    }
  }
}
