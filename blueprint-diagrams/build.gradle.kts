plugins {
  id("convention-kotlin")
  id("convention-publish")
  `java-gradle-plugin`
}

dependencies {
  compileOnly(gradleApi())
  api(projects.blueprintCore)
  api(libs.plugin.dependencyGraph)
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
