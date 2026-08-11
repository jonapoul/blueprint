import blueprint.gradle.findOptionalProperty

plugins {
  id("blueprint.convention")
  `java-gradle-plugin`
}

dependencies {
  compileOnly(gradleApi())
  testImplementation(kotlin("stdlib"))
  testImplementation(kotlin("test"))
  testCompileOnly(libs.junit.api)
  testRuntimeOnly(libs.junit.launcher)
}

gradlePlugin {
  vcsUrl = "https://github.com/jonapoul/blueprint.git"
  website = "https://github.com/jonapoul/blueprint"
  plugins.register("blueprint") {
    id = "dev.jonpoulton.blueprint.test"
    description = findOptionalProperty("POM_DESCRIPTION")
    implementationClass = "blueprint.test.BlueprintTestPlugin"
    displayName = "Blueprint Test"
    tags.addAll("gradle", "blueprint", "utilities", "test")
  }
}

buildConfig {
  sourceSets.named("main") {
    packageName.set("blueprint.test")
    useKotlinOutput { topLevelConstants = true }
    buildConfigField("BLUEPRINT_VERSION", providers.gradleProperty("VERSION_NAME"))
  }
}
