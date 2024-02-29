@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
  id("convention-kotlin")
  alias(libs.plugins.buildConfig)
  alias(libs.plugins.dokka)
  alias(libs.plugins.publish)
  `java-gradle-plugin`
}

dependencies {
  compileOnly(gradleApi())
  api(projects.blueprintCore)
  api(libs.plugin.dokka)
  api(libs.plugin.publish)
}

gradlePlugin {
  plugins {
    create("blueprint-publish") {
      id = "dev.jonpoulton.blueprint.publish"
      displayName = "Publish Blueprint"
      description = "Gradle plugin to apply and configure publishing"
      implementationClass = "blueprint.publish.PublishBlueprintPlugin"
    }
  }
}

val home: String? = System.getProperty("user.home")
if (home.isNullOrBlank()) {
  logger.warn("Invalid home directory!")
}

val mavenLocalRepo = File(File(home, ".m2"), "repository")

buildConfig {
  packageName("blueprint.publish")
  buildConfigField("String", "MAVEN_LOCAL_PATH", "\"${mavenLocalPath()}\"")
  useKotlinOutput { internalVisibility = true }
}

fun mavenLocalPath(): String {
  // Handles windows paths, where backslashes would otherwise introduce unintended escape characters
  return mavenLocalRepo.absolutePath.replace("\\", "\\\\")
}
