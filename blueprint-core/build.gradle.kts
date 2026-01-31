plugins {
  id("blueprint.convention")
  `kotlin-dsl`
  `java-gradle-plugin` // only needed for gradleTestKit
}

kotlin {
  compilerOptions {
    freeCompilerArgs.add("-Xcontext-receivers")
  }
}

dependencies {
  compileOnly(gradleApi())
  compileOnly(kotlin("gradle-plugin"))
  testImplementation(project(":blueprint-test-assertk"))
  testImplementation(project(":blueprint-test-runtime"))
  testPluginClasspath(kotlin("gradle-plugin"))
}

gradlePlugin {
  vcsUrl = "https://github.com/jonapoul/blueprint.git"
  website = "https://github.com/jonapoul/blueprint"
  plugins.register("blueprint") {
    id = "dev.jonpoulton.blueprint"
    description = properties["POM_DESCRIPTION"]?.toString()
    implementationClass = "blueprint.core.BlueprintPlugin"
    displayName = "Blueprint"
    tags.addAll("gradle", "blueprint", "utilities")
  }
}
