plugins {
  alias(libs.plugins.convention.kotlin)
  alias(libs.plugins.convention.publish)
  `java-gradle-plugin`
}

dependencies {
  api(libs.graphviz)
  compileOnly(gradleApi())
  implementation(projects.blueprintCore)
  implementation(libs.plugin.dependencyGraph)
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
