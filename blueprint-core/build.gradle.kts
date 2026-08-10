import blueprint.gradle.findOptionalProperty

plugins {
  id("blueprint.convention")
  `java-gradle-plugin` // only needed for gradleTestKit
}

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
    description = findOptionalProperty("POM_DESCRIPTION")
    implementationClass = "blueprint.core.BlueprintPlugin"
    displayName = "Blueprint"
    tags.addAll("gradle", "blueprint", "utilities")
  }
}
