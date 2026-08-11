import blueprint.gradle.findOptionalProperty

plugins {
  id("blueprint.convention")
  `java-gradle-plugin` // only needed for gradleTestKit
}

dependencies {
  compileOnly(gradleApi())
  compileOnly(kotlin("gradle-plugin"))
  testImplementation(project(":blueprint-test-assertk"))
  testImplementation(project(":blueprint-test-runtime"))
  testImplementation(kotlin("stdlib"))
  testImplementation(kotlin("test"))
  testImplementation(libs.assertk)
  testCompileOnly(libs.junit.api)
  testRuntimeOnly(libs.junit.launcher)
  testPluginClasspath(kotlin("gradle-plugin"))
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
