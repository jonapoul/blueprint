plugins {
  id("blueprint.convention")
  `kotlin-dsl`
  `java-gradle-plugin` // only needed for gradleTestKit
}

kotlin { compilerOptions { freeCompilerArgs.add("-Xcontext-receivers") } }

dependencies {
  compileOnly(gradleApi())
  compileOnly(kotlin("gradle-plugin"))

  testCompileOnly(libs.junit.api)
  testImplementation(kotlin("stdlib"))
  testImplementation(kotlin("test"))
  testImplementation(libs.assertk)
  testImplementation(project(":blueprint-test-assertk"))
  testImplementation(project(":blueprint-test-runtime"))
  testPluginClasspath(kotlin("gradle-plugin"))
  testRuntimeOnly(libs.junit.launcher)
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
