plugins {
  id("blueprint.convention")
  `java-gradle-plugin`
}

dependencies {
  compileOnly(gradleApi())

  testCompileOnly(libs.junit.api)
  testImplementation(kotlin("stdlib"))
  testImplementation(kotlin("test"))
  testImplementation(libs.assertk)
  testRuntimeOnly(libs.junit.launcher)
}

gradlePlugin {
  vcsUrl = "https://github.com/jonapoul/blueprint.git"
  website = "https://github.com/jonapoul/blueprint"
  plugins.register("blueprint") {
    id = "dev.jonpoulton.blueprint.test"
    description = properties["POM_DESCRIPTION"]?.toString()
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
