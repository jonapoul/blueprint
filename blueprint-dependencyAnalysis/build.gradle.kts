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
  api(libs.plugin.dependencyAnalysis)
}

gradlePlugin {
  plugins {
    create("blueprint-analysis") {
      id = "dev.jonpoulton.blueprint.analysis"
      displayName = "Dependency Analysis Blueprint"
      description = "Gradle plugin to apply and configure Dependency Analysis"
      implementationClass = "blueprint.analysis.DependencyAnalysisBlueprintPlugin"
    }
  }
}
